package com.avoscloud.chat.myspace;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class UserMarkFragment extends Fragment implements View.OnClickListener {


    Toolbar toolbar;
    EditText edit_mark;
    Button btn_save;

    String str_mark;
    IMUser _user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mark, container, false);
        edit_mark = (EditText)view.findViewById(R.id.edit_txt);
        btn_save = (Button)view.findViewById(R.id.btn_save);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init(){

        toolbar.setTitle(R.string.signature_setting);
        _user = IMUser.getCurrentUser();
        str_mark = _user.getMark();
        if(StringUtils.isNullOrEmpty(str_mark)){
            str_mark = "";
        }
        edit_mark.setText(str_mark);

        btn_save.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.btn_save){
            str_mark = edit_mark.getText().toString();
            if(StringUtils.isNullOrEmpty(str_mark)){
                return ;
            }
            _user = IMUser.getCurrentUser();
            _user.setMark(str_mark);
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
