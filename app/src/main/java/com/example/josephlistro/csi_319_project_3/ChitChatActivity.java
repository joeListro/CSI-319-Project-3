package com.example.josephlistro.csi_319_project_3;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class ChitChatActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, ChitChatActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return ChitChatFragment.newInstance();
    }
}
