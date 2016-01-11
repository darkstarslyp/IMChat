package com.avoscloud.chat.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avoscloud.chat.R;
import com.avoscloud.chat.activity.ProfileNotifySettingActivity;
import com.avoscloud.chat.myspace.MySpaceActivity;
import com.avoscloud.chat.service.PushManager;
import com.avoscloud.chat.service.UpdateService;
import com.avoscloud.chat.activity.EntryLoginActivity;
import com.avoscloud.chat.util.PathUtils;
import com.avoscloud.chat.util.StringUtils;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.chat.model.IMUser;
import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * Created by lzw on 14-9-17.
 */
public class ProfileFragment extends BaseFragment {
  private static final int IMAGE_PICK_REQUEST = 1;
  private static final int CROP_REQUEST = 2;

  @Bind(R.id.profile_avatar_view)
  ImageView avatarView;

  @Bind(R.id.profile_username_view)
  TextView userNameView;

  ChatManager chatManager;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.profile_fragment, container, false);
    ButterKnife.bind(this, view);
    EventBus.getDefault().register(this);
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if(toolbar==null){
      toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
    }
    toolbar.setTitle(R.string.title_activity_my_space);
    chatManager = ChatManager.getInstance();
  }

  @Override
  public void onResume() {
    super.onResume();
    refresh();
  }

  private void refresh() {
    IMUser curUser = IMUser.getCurrentUser();
    userNameView.setText(curUser.getUsername());
    String avatarUrl = curUser.getAvatarUrl();
    if(StringUtils.isNotNullOrEmpty(avatarUrl)){
      ImageLoader.getInstance().displayImage(curUser.getAvatarUrl(), avatarView, com.avoscloud.leanchatlib.utils.PhotoUtils.avatarImageOptions);
    }
  }

  @OnClick(R.id.profile_checkupdate_view)
  public void onCheckUpdateClick() {
    UpdateService updateService = UpdateService.getInstance(getActivity());
    updateService.showSureUpdateDialog();
  }

  @OnClick(R.id.profile_notifysetting_view)
  public void onNotifySettingClick() {
    Intent intent = new Intent(ctx, ProfileNotifySettingActivity.class);
    ctx.startActivity(intent);
  }

  @OnClick(R.id.profile_logout_btn)
  public void onLogoutClick() {
    chatManager.closeWithCallback(new AVIMClientCallback() {
      @Override
      public void done(AVIMClient avimClient, AVIMException e) {
      }
    });
    PushManager.getInstance().unsubscribeCurrentUserChannel();
    IMUser.logOut();
    getActivity().finish();
    Intent intent = new Intent(ctx, EntryLoginActivity.class);
    ctx.startActivity(intent);
  }

  /*
   *进入当前用户管理界面
   */
  @OnClick(R.id.profile_avatar_layout)
  public void onAvatarClick() {
//    Intent intent = new Intent(Intent.ACTION_PICK, null);
//    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//    startActivityForResult(intent, IMAGE_PICK_REQUEST);
    MySpaceActivity.goMySpaceActivity(this.getActivity());
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

  public void onEvent(IMUser user){
     ImageLoader.getInstance().displayImage(user.getAvatarUrl(),avatarView);
  }
}
