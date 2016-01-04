package com.avoscloud.chat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import com.avoscloud.chat.R;
import com.avoscloud.leanchatlib.activity.AVBaseActivity;

/**
 * Created by lzw on 14-9-17.
 */
public class UpdateContentActivity extends AVBaseActivity {
  public static final String FIELD_NAME = "fieldName";
  public static final String VALUE = "value";
  private TextView fieldNameView;
  private EditText valueEdit;

  public static void goActivityForResult(Activity activity, String fieldName, int requestCode) {
    Intent intent = new Intent(activity, UpdateContentActivity.class);
    intent.putExtra(FIELD_NAME, fieldName);
    activity.startActivityForResult(intent, requestCode);
  }

  public static String getResultValue(Intent data) {
    return data.getStringExtra(VALUE);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.update_content_layout);
    findView();
    init();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.update_content_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  private void init() {
    Intent intent = getIntent();
    String fieldName = intent.getStringExtra(FIELD_NAME);
    String editHint = getString(R.string.chat_common_please_input_hint);
    String changeTitle = getString(R.string.chat_common_change_title);
    editHint = editHint.replace("{0}", fieldName);
    changeTitle = changeTitle.replace("{0}", fieldName);
    fieldNameView.setText(fieldName);
    valueEdit.setHint(editHint);
    setTitle(changeTitle);
  }

  public void updateContent() {
    Intent i = new Intent();
    i.putExtra(VALUE, valueEdit.getText().toString());
    setResult(RESULT_OK, i);
    finish();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.sure) {
      updateContent();
    }
    return super.onOptionsItemSelected(item);
  }

  private void findView() {
    fieldNameView = (TextView) findViewById(R.id.fieldName);
    valueEdit = (EditText) findViewById(R.id.valueEdit);
  }
}
