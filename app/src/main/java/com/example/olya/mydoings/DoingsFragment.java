package com.example.olya.mydoings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.R.attr.data;


/**
 * Created by tyuly on 03.10.2016.
 */

public class DoingsFragment extends Fragment { //фрагмент на PagerActivity
    private Doings mDoings;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mDoneCheckBox;
    private Button mDeleteButton;

    public static final String DOINGS_ID="doings_id";
    public static final String  DIALOG_DATE="dialog_date";
    private static final int REQUEST_DATE = 0;

    public static DoingsFragment newInstance(UUID doingsId) {//создаем аргументы фрагментов(аналог дополнений в активити)
        Bundle bundle=new Bundle();
        bundle.putSerializable(DOINGS_ID,doingsId);
        DoingsFragment fragment=new DoingsFragment();
        fragment.setArguments(bundle); //присоединяем аргументы к фрагменту
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mDoings=new Doings();
        UUID doingsId=(UUID)getArguments().getSerializable(DOINGS_ID); //получени аргументов
        mDoings=DoingsLab.get(getActivity()).getDoings(doingsId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doings, container, false);
        mTitleField = (EditText)v.findViewById(R.id.doing_title);
        mTitleField.setText(mDoings.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() { //добавление слушателя. слушатель-интерфейсный класс TextWatcher
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) { //метод вызывается до изменений, чтобы уведомить,
                // что в строке с,
                // начиная с позиции start  будут заменены count символов, новыми after символами.
                // Изменение текста с в этом методе является ошибкой
                // Здесь намеренно оставлено пустое место
            }
            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) { //метод вызывается, чтобы уведомить,
                // что в строке c, начиная с позиции start, только что заменены after символов, новыми count символами.
                // Изменение текста c в этом методе является ошибкой.
                mDoings.setTitle(c.toString());
            }
            @Override
            public void afterTextChanged(Editable c) { //метод вызывается, чтобы уведомить,
                // что где-то в строке c, текст был изменен.
                // можно вносить изменения в текст c, любые изменения в s рекурсивно вызовут этот же метод.
                // И здесь тоже
                }         });

        mDateButton = (Button)v.findViewById(R.id.button_date);
        updateDate();
       // mDateButton.setText(mDoings.getDate().format(new Date()));
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog =  DatePickerFragment.newInstance(mDoings.getDate()); ;
                dialog.setTargetFragment(DoingsFragment.this, REQUEST_DATE); //установили целевой фрагмент. По коду запроса целевой фрагмент позднее может определить,
                // какой фрагмент возвращает информацию.
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mDoneCheckBox=(CheckBox) v.findViewById(R.id.done);
        mDoneCheckBox.setChecked(mDoings.getDone());
        mDoneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //изменение поля mDone при изменении чекбокса
                mDoings.setDone(isChecked);
            }
        });
        mDeleteButton=(Button) v.findViewById(R.id.delete);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoingsLab.get(getActivity()).deleteDoings(mDoings, mDoings.getId());
                getActivity().finish();

            }
        });
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        DoingsLab.get(getActivity()).updateDoings(mDoings);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.ARG_DATE);
            mDoings.setDate(date);
            updateDate();
        }
    }

   private void updateDate() {
       SimpleDateFormat format1 = new SimpleDateFormat("EEEE, dd MMMM yyyy");
       mDateButton.setText(format1.format(mDoings.getDate()));

    }
}
