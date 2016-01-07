package com.avoscloud.chat.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.avoscloud.chat.R;
import com.avoscloud.leanchatlib.activity.AVBaseActivity;

/**
 * Created by lzw on 14-9-24.
 */
public class ProfileNotifySettingActivity extends AVBaseActivity {

  Toolbar toolbar;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.profile_setting_notify_layout);
    toolbar = (Toolbar)findViewById(R.id.toolbar);
    toolbar.setTitle(R.string.profile_notifySetting);
  }
}
