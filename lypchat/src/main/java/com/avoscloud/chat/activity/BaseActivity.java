package com.avoscloud.chat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.avoscloud.chat.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/5 0005.
 */
public class BaseActivity extends AppCompatActivity {

    @Bind(R.id.blank_activity_layout)
    LinearLayout linearLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_activity);
        ButterKnife.bind(this);
    }
}
