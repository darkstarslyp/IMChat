package com.avoscloud.chat.myspace;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.avoscloud.chat.App;
import com.avoscloud.chat.R;
import com.avoscloud.chat.model.IMUser;
import com.avoscloud.chat.util.StringUtils;
import com.avoscloud.chat.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignatureAcivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.edit_signature)
    EditText edit_signature;
    @Bind(R.id.btn_save)
    Button btn_save;

    String str_signature;
    IMUser _user;

    public static void goSignatureActivity(Activity fromActivity){
        Intent intent = new Intent(fromActivity,SignatureAcivity.class);
        fromActivity.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signature_acivity);
        ButterKnife.bind(this);
        init();
    }

    private void init(){

        toolbar.setTitle(R.string.signature_setting);

        str_signature = IMUser.getCurrentUser().getSignature();
        if(StringUtils.isNullOrEmpty(str_signature)){
            str_signature = "";
        }
        edit_signature.setText(str_signature);

        btn_save.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.btn_save){
            str_signature = edit_signature.getText().toString();
            if(StringUtils.isNullOrEmpty(str_signature)){
                return ;
            }
            _user = IMUser.getCurrentUser();
            _user.setSignature(str_signature);
            _user.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if(e!=null){
                        e.printStackTrace();
                    }else {
                        Utils.toast(App.ctx.getString(R.string.toast_success));
                    }
                }
            });
        }
    }
}
