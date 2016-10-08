package com.example.olya.mydoings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import static com.example.olya.mydoings.DoingsFragment.DOINGS_ID;

/**
 * Created by tyuly on 03.10.2016.
 */

public class DoingsListFragment extends Fragment { //список дел
    private RecyclerView mDoingsRecyclerView;
    private DoingsAdapter mAdapter;
    private EmptyAdapter emptyAdapter;
    private int position;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doings_list, container, false);
        mDoingsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mDoingsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //для размещения элементов на экране
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {    //для правильного отображения заголовка в толбаре, так как при использовании иерархической
        //авигации активити создается заново
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onResume() { //обновление списка
        super.onResume();
        updateUI();
        updateSubtitle();
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_doings_list, menu);
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else {
            subtitleItem.setTitle(R.string.show_subtitle);    }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_doings:
                Doings doings = new Doings();
                DoingsLab.get(getActivity()).addDoings(doings);
                Intent intent = DoingsPagerActivity.newIntent(getActivity(), doings.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() { //генерирует строку подзаголовка c количеством записей
        DoingsLab doingsLab=DoingsLab.get(getActivity());
        int count=doingsLab.getDoings().size();
       // String subtitle=getString(R.string.subtitle_format,count);
        String subtitle=getResources().getQuantityString(R.plurals.subtitle_plural,count,count );
        if (!mSubtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity appCompatActivity=(AppCompatActivity)getActivity(); //активность, являющаяся хостом, преобразуется в AppCompatActivity
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);

    }


        private class DoingsHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{ //ViewHolder удерживает объект View
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mCheckBox;
        private Doings mDoings;




        public DoingsHolder(View itemView) {
            super(itemView); // представление
            itemView.setOnClickListener(this);
            //mTitleTextView = (TextView) itemView; //заголовок
            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_title_text_view);
            mDateTextView = (TextView)itemView.findViewById(R.id.list_item_date_text_view);
            mCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_check_box);
        }

        @Override
        public void onClick(View v) {//открывается окно конкретной заметки заметки. id  передается в newIntent
            Intent intent=DoingsPagerActivity.newIntent(getActivity(),mDoings.getId());
            position=getLayoutPosition();
            startActivity(intent);

        }

        public void bindCrime(Doings doings) {
           // Получив объект Doings, объект Holder обновляет TextView с  кратким описанием, T
            // extView с датой и CheckBox с признаком раскрытия преступления в соответствии с содержимым Doings.
            mDoings = doings;
            mTitleTextView.setText(mDoings.getTitle());
            //mDateTextView.setText(mDoings.getDate().toString());
            SetDateTextView();
            mCheckBox.setChecked(mDoings.getDone());
        }

        private void SetDateTextView() {
            SimpleDateFormat format1 = new SimpleDateFormat("EEEE dd.MM.yyyy");
            mDateTextView.setText(format1.format(mDoings.getDate()));

        }

        }

    private class DoingsAdapter extends RecyclerView.Adapter<DoingsHolder> { //адаптер создает ВьюХолдер, связывает его с данными модели.
        private List<Doings> mDoings;

        public DoingsAdapter(List<Doings> mDoings) {
            this.mDoings = mDoings;
        }

        @Override
        public DoingsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            //вызывается виджетом RecyclerView, когда ему потребуется новое представление для отображения элемента.
            // В этом методе создаем объект View и упаковываем его в ViewHolder.
            // RecyclerView пока не ожидает, что представление будет связано с какими-либо данными.
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            //Для получения представления заполняем макет

            view = layoutInflater.inflate(R.layout.item_list_doings, parent, false);

            return new DoingsHolder(view);
        }

        @Override
        public void onBindViewHolder(DoingsHolder holder, int position) {
            // связывает представление View объекта ViewHolder с объектом модели.
            // При вызове он получает ViewHolder и позицию в наборе данных.
            // Позиция используется для нахождения правильных данных модели, после чего View обновляется в соответствии с этими данными.
            Doings doings = mDoings.get(position); // позиция определяет индекс объекта Doings в массиве.
            //holder.mTitleTextView.setText(doings.getTitle()); // После получения нужного объекта связываем его с View,
            // присваивая его заголовок виджету TextView из ViewHolder.
            holder.bindCrime(doings);
        }

        @Override
        public int getItemCount() {
            return mDoings.size();
        }

        public void setDoings(List<Doings> doings) {
            mDoings = doings;
        }


    }

    private class EmptyAdapter extends RecyclerView.Adapter<DoingsHolder>  {

        private List<Doings> mDoings;

        public EmptyAdapter(List<Doings> mDoings) {
            this.mDoings = mDoings;
        }

        @Override
        public DoingsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.empty_list, parent, false);
            return new DoingsHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(DoingsHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            //return mDoings.size();
            return 1;
        }

    }


        private void updateUI() { //создает адаптер и назначает его ресайклервью
        DoingsLab doingsLab = DoingsLab.get(getActivity());
        List<Doings> doings = doingsLab.getDoings();
        if (mAdapter == null) {
            mAdapter = new DoingsAdapter(doings);
            mDoingsRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setDoings(doings);
            //mAdapter.notifyItemChanged(position); //обновление только одного элемента
            mAdapter.notifyDataSetChanged(); //обновление списка
        }
        if (doings.size()==0) {
            mDoingsRecyclerView.setAdapter(new EmptyAdapter(doings));
            return;
            }
    }
}
