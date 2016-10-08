package com.example.olya.mydoings;

import android.support.v4.app.Fragment;

/**
 * Created by tyuly on 03.10.2016.
 */

public class DoingsListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new DoingsListFragment();
    }

}
