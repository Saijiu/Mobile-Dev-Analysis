package com.example.administrator.viewanalysis;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {
   private  EasyView mEasyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEasyView = (EasyView) findViewById(R.id.easyView);

        mEasyView.setOnLeftButtonListener(new EasyView.OnLeftButtonListener() {
            @Override
            public void onclikLeftButton(View view) {
                Log.d("=========================",".................>>>>>>>>>>>>>");
            }
        });
    }


}
