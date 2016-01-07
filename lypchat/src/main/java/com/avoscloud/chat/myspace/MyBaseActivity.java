package com.avoscloud.chat.myspace;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.avoscloud.chat.R;

public class MyBaseActivity extends AppCompatActivity {

    public static final String EDIT_USER_NAME = "username";
    public static final String EDIT_USER_MARK = "usermark";
    public static final String EDIT_TYPE = "type";

    private String _editType;

    FragmentTransaction transaction;
    FragmentManager fragmentManager;
    UserNameFragment userNameFragment;
    UserMarkFragment userMarkFragment;


    public static void goMyBaseActivity(Activity fromActivity,Bundle bundle){
        Intent intent = new Intent(fromActivity,MyBaseActivity.class);
        intent.putExtras(bundle);
        fromActivity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_activity);
        init();
    }

    private void init(){
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();


        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey(EDIT_TYPE)){
            _editType = bundle.getString(EDIT_TYPE);
        }else{
            return ;
        }
        if(_editType.equals(EDIT_USER_NAME)){
            goUserNameFramgent();
        }else if (_editType.equals(EDIT_USER_MARK)){
            goUserMarkFragment();
        }
    }

    void goUserNameFramgent(){
        userNameFragment = new UserNameFragment();
        transaction.add(R.id.blank_activity_layout, userNameFragment);
        transaction.show(userNameFragment);
        transaction.commit();
    }

    void goUserMarkFragment(){
        userMarkFragment = new UserMarkFragment();
        transaction.add(R.id.blank_activity_layout, userNameFragment);
        transaction.show(userMarkFragment);
        transaction.commit();
    }
}
