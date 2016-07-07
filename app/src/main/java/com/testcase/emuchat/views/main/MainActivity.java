package com.testcase.emuchat.views.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.testcase.emuchat.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MainController mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainController = MainController.init(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainController.release();
    }
}
