package com.example.olya.mydoings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by tyuly on 04.10.2016.
 */

public class DoingsPagerActivity extends AppCompatActivity {

    public static final  String EXTRA_DOINGS_ID="dongs_id";

    private ViewPager mViewPager;
    private List<Doings> mDoings;

    public static Intent newIntent(Context context, UUID doingsId){ //передаем id для отображения конкретной заметки
        Intent intent=new Intent(context,DoingsPagerActivity.class);
        intent.putExtra(EXTRA_DOINGS_ID, doingsId);
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_activity);
        UUID doingsId = (UUID) getIntent().getSerializableExtra(EXTRA_DOINGS_ID);
        mViewPager = (ViewPager)findViewById(R.id.activity__pager_view_pager);
        mDoings=DoingsLab.get(this).getDoings();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
                @Override
            public Fragment getItem(int position) {
                Doings doings = mDoings.get(position);
                return DoingsFragment.newInstance(doings.getId());
            }
            @Override
            public int getCount() {
                return mDoings.size();
            }
        });

        for (int i = 0; i < mDoings.size(); i++) { //для получения выбранной заметки на вьюпэджере
            if (mDoings.get(i).getId().equals(doingsId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}

