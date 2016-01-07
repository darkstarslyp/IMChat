package com.avoscloud.chat.myspace;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.avoscloud.chat.R;
import com.avoscloud.chat.model.IMUser;
import com.avoscloud.chat.util.PathUtils;
import com.avoscloud.chat.util.StringUtils;
import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MySpaceActivity extends AppCompatActivity {
    //声明控件变量
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.username)
    TextView txt_username;
    @Bind(R.id.sex)
    TextView txt_sex;
    @Bind(R.id.signature)
    TextView txt_signature;
    @Bind(R.id.im_mark)
    TextView txt_mark;
    @Bind(R.id.area)
    TextView txt_area;
    @Bind(R.id.img_user_avatar)
    ImageView img_avatar;
    @Bind(R.id.card)
    ImageView img_card;
    @Bind(R.id.btn_signature)
    RelativeLayout btn_signature;
    @Bind(R.id.btn_area)
    RelativeLayout btn_area;
    @Bind(R.id.btn_username)
    RelativeLayout btn_username;
    @Bind(R.id.btn_card)
    RelativeLayout btn_card;
    @Bind(R.id.btn_mark)
    RelativeLayout btn_mark;
    @Bind(R.id.btn_sex)
    RelativeLayout btn_sex;
    @Bind(R.id.btn_avatar)
    RelativeLayout btn_avatar;

    IMUser _user;

    /* 定义 String 变量*/
    String str_username;
    String str_mark;
    String str_sex;
    String str_area;
    String str_card_url;
    String str_avatar_url;
    String str_signature;



    private static final int IMAGE_PICK_REQUEST = 1;
    private static final int CROP_REQUEST = 2;


    /*
    *  从另一个界面跳转至当前界面
     */
    public static void goMySpaceActivity(Activity fromActivity) {
        Intent intent = new Intent(fromActivity, MySpaceActivity.class);
        fromActivity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_space_activity);
        ButterKnife.bind(this);
        init();
    }
    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        //设置toolbar相关属性
        toolbar.setTitle(R.string.title_activity_my_setting);
        //初始化用户信息
        _user = IMUser.getCurrentUser();
        if (_user == null) {
            return;
        }
        str_area = _user.getArea();
        str_avatar_url = _user.getAvatarUrl();
        str_card_url = _user.getCardUrl();
        str_mark = _user.getMark();
        str_sex = _user.getSex();
        str_username = _user.getUsername();
        str_signature = _user.getSignature();

        if (StringUtils.isNullOrEmpty(str_username)) {
            str_username = "";
        }
        txt_username.setText(str_username);

        if (StringUtils.isNullOrEmpty(str_sex)) {
            str_sex = getString(R.string.sex_female);
        }
        txt_sex.setText(str_sex);

        if (StringUtils.isNullOrEmpty(str_area)) {
            str_area = "";
        }
        txt_area.setText(str_area);
        txt_area.setVisibility(View.GONE);

        if (StringUtils.isNullOrEmpty(str_mark)) {
            str_mark = "";
        }
        txt_mark.setText(str_mark);

        if (StringUtils.isNullOrEmpty(str_signature)) {
            str_signature = "";
        }
        txt_signature.setText(str_signature);

        if (StringUtils.isNullOrEmpty(str_avatar_url)) {

        } else {
            ImageLoader.getInstance().displayImage(str_avatar_url, img_avatar);
        }

        if (StringUtils.isNotNullOrEmpty(str_card_url)) {
            ImageLoader.getInstance().displayImage(str_card_url, img_card);
        }
    }

    @OnClick(R.id.btn_signature)
    void clickSignature(View view) {
        SignatureAcivity.goSignatureActivity(this);
    }

    @OnClick(R.id.btn_sex)
    void clickSex() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_choose_sex, null);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        dialog.setTitle(getString(R.string.sex));
        dialog.setContentView(view, params);
        dialog.show();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = radioGroup.getCheckedRadioButtonId();
                if (id == R.id.sex_male) {
                    str_sex = getString(R.string.sex_male);
                } else if (id == R.id.sex_female) {
                    str_sex = getString(R.string.sex_female);
                }
                _user.setSex(str_sex);
                _user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            //更新性别显示
                            txt_sex.setText(str_sex);
                        } else {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    @OnClick(R.id.btn_mark)
    void markClick() {
        str_mark = _user.getMark();
        if (StringUtils.isNotNullOrEmpty(str_mark)) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(MyBaseActivity.EDIT_TYPE, MyBaseActivity.EDIT_USER_MARK);
        MyBaseActivity.goMyBaseActivity(this, bundle);
    }

    @OnClick(R.id.btn_username)
    void userNameClick() {
        str_username = _user.getUsername();
        Bundle bundle = new Bundle();
        bundle.putString(MyBaseActivity.EDIT_TYPE, MyBaseActivity.EDIT_USER_NAME);
        MyBaseActivity.goMyBaseActivity(this, bundle);
    }

    @OnClick(R.id.btn_avatar)
    void avatarClick() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, IMAGE_PICK_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_REQUEST) {
                Uri uri = data.getData();
                startImageCrop(uri, 200, 200, CROP_REQUEST);
            } else if (requestCode == CROP_REQUEST) {
                final String path = saveCropAvatar(data);
                IMUser user = IMUser.getCurrentUser();
                user.saveAvatar(path, null);
            }
        }
    }

    public Uri startImageCrop(Uri uri, int outputX, int outputY,
                              int requestCode) {
        Intent intent = null;
        intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        String outputPath = PathUtils.getAvatarTmpPath();
        Uri outputUri = Uri.fromFile(new File(outputPath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false); // face detection
        startActivityForResult(intent, requestCode);
        return outputUri;
    }

    private String saveCropAvatar(Intent data) {
        Bundle extras = data.getExtras();
        String path = null;
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            if (bitmap != null) {
                bitmap = PhotoUtils.toRoundCorner(bitmap, 10);
                path = PathUtils.getAvatarCropPath();
                PhotoUtils.saveBitmap(path, bitmap);
                if (bitmap != null && bitmap.isRecycled() == false) {
                    bitmap.recycle();
                }
            }
        }
        return path;
    }
}
