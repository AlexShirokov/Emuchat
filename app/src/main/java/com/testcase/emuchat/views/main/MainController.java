package com.testcase.emuchat.views.main;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.testcase.emuchat.CommandCenter;
import com.testcase.emuchat.R;
import com.testcase.emuchat.views.fragments.ChatFragment;

/**
 * Created by AlexShredder on 06.07.2016.
 */
public class MainController {
    private static AppCompatActivity mActivity;
    private static MainController instance;
    private ChatFragment chatFragment;

    private int currentFragment;
    private final int CHAT = 0;
    private final int INFO = 1;

    private MainController(){

    }

    static MainController init(AppCompatActivity activity){
        if (instance==null) instance = new MainController();
        instance.restartActivity(activity);
        return instance;
    }

    void release(){
        instance = null;
    }

    private void restartActivity(AppCompatActivity activity) {
        mActivity = activity;

        Toolbar toolbar = (Toolbar) mActivity.findViewById(R.id.actionbar);
        mActivity.setSupportActionBar(toolbar);

        ImageButton trashButton = (ImageButton) mActivity.findViewById(R.id.imageButtonTrash);
        if (trashButton != null) {
            trashButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommandCenter.getInstance(mActivity.getApplicationContext()).clearBase(null);
                }
            });
        }
        switchToFragment(currentFragment);
    }

    private void switchToFragment(int fragment){
        Fragment cur=null;
        switch (fragment){
            case CHAT:
                if (chatFragment == null) chatFragment = new ChatFragment();
                cur = chatFragment;
                break;
            case INFO:
                break;
         }

        android.support.v4.app.FragmentTransaction fTrans = mActivity.getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.frameLayout, cur);
        fTrans.commit();
        currentFragment = fragment;
    }
}
