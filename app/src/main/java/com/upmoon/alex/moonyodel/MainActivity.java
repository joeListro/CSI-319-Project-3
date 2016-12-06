package com.upmoon.alex.moonyodel;

import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new YellLoudFragment();
    }
}
