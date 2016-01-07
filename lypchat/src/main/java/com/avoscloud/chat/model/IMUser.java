package com.avoscloud.chat.model;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.avoscloud.chat.App;
import com.avoscloud.chat.R;
import com.avoscloud.chat.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by lyp 2016/1/5
 * 自定义的 AVUser
 */
public class IMUser extends AVUser {

  public static final String USERNAME = "username";//用户名
  public static final String AVATAR = "avatar"; //用户头像
  public static final String LOCATION = "location";
  public static final String INSTALLATION = "installation";
  public static final String CARD = "card";//二维码
  public static final String MARK = "mark";//IM号
  public static final String SIGNATURE = "signature";//个性签名
  public static final String AREA = "area";//个性签名
  public static final String SEX = "sex";//个性签名


  public String getSex(){
    int sex = getInt(SEX);
    String str_sex = App.ctx.getResources().getString(R.string.sex_female);
    if(sex==1){
      str_sex = App.ctx.getResources().getString(R.string.sex_male);
    }
    return str_sex;
  }
  public void setSex(String strSex){
         if(strSex==App.ctx.getResources().getString(R.string.sex_female)){
           put(SEX,0);
         }else{
           put(SEX,1);
         }
  }
  public String getSignature(){
    return getString(SIGNATURE);
  }

  public void setSignature(String strSignature){
     if(StringUtils.isNullOrEmpty(strSignature)){
       strSignature = "";
     }
    put(SIGNATURE,strSignature);
  }
  public String getArea(){
    return getString(SIGNATURE);
  }

  public void setArea(String strArea){
    if(StringUtils.isNullOrEmpty(strArea)){
      strArea = "";
    }
    put(AREA,strArea);
  }

  public void setMark(String mark){
      if(StringUtils.isNullOrEmpty(mark)){
        mark = "";
      }
      put(MARK,mark);
  }
  public String  getMark(){
    return     getString(MARK);
  }

  public static String getCurrentUserId () {
    IMUser currentUser = getCurrentUser(IMUser.class);
    return (null != currentUser ? currentUser.getObjectId() : null);
  }

  public String getCardUrl(){
    AVFile cardQR = getAVFile(CARD);
    if(cardQR != null){
      return cardQR.getUrl();
    }else{
      return null;
    }
  }
  /*
   *解释此段代码
   */
  public void saveCard(String path,final SaveCallback saveCallback){
    final AVFile avFile;
    try{
      avFile = AVFile.withAbsoluteLocalPath(path,path);
      put(CARD,avFile);
      avFile.saveInBackground(new SaveCallback() {
        @Override
        public void done(AVException e) {
          if(e != null){
            saveCallback.done(e);
          }else{
            saveInBackground(saveCallback);
          }
        }
      });
    }catch(IOException e){
      e.printStackTrace();
    }
  }

  public String getAvatarUrl() {
    AVFile avatar = getAVFile(AVATAR);
    if (avatar != null) {
      return avatar.getUrl();
    } else {
      return null;
    }
  }


  public void saveAvatar(String path, final SaveCallback saveCallback) {
    final AVFile file;
    try {
      file = AVFile.withAbsoluteLocalPath(getUsername(), path);
      put(AVATAR, file);
      file.saveInBackground(new SaveCallback() {
        @Override
        public void done(AVException e) {
          if (null == e) {
            saveInBackground(saveCallback);
          } else {
            if (null != saveCallback) {
              saveCallback.done(e);
            }
          }
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static IMUser getCurrentUser() {
    return getCurrentUser(IMUser.class);
  }

  public void updateUserInfo() {
    AVInstallation installation = AVInstallation.getCurrentInstallation();
    if (installation != null) {
      put(INSTALLATION, installation);
      saveInBackground();
    }
  }

  public AVGeoPoint getGeoPoint() {
    return getAVGeoPoint(LOCATION);
  }

  public void setGeoPoint(AVGeoPoint point) {
    put(LOCATION, point);
  }

  public static void signUpByNameAndPwd(String name, String password, SignUpCallback callback) {
    AVUser user = new AVUser();
    user.setUsername(name);
    user.setPassword(password);
    user.signUpInBackground(callback);
  }

  public void removeFriend(String friendId, final SaveCallback saveCallback) {
    unfollowInBackground(friendId, new FollowCallback() {
      @Override
      public void done(AVObject object, AVException e) {
        if (saveCallback != null) {
          saveCallback.done(e);
        }
      }
    });
  }

  public void findFriendsWithCachePolicy(AVQuery.CachePolicy cachePolicy, FindCallback<IMUser>
      findCallback) {
    AVQuery<IMUser> q = null;
    try {
      q = followeeQuery(IMUser.class);
    } catch (Exception e) {
    }
    q.setCachePolicy(cachePolicy);
    q.setMaxCacheAge(TimeUnit.MINUTES.toMillis(1));
    q.findInBackground(findCallback);
  }
}
