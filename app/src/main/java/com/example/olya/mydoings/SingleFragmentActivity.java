package com.example.olya.mydoings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by tyuly on 03.10.2016.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity { //абстрактный класс для создания экземпляров фрагментов

    protected  abstract Fragment createFragment(); //создание экземляра фрагмента
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager(); //получаем FragmentManager
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction() //транзакция
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }
}
