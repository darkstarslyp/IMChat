package com.avoscloud.chat.util;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avoscloud.chat.model.IMUser;
import com.avoscloud.leanchatlib.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wli on 15/9/30.
 * TODO
 * 1、本地存储
 * 2、避免内存、外存占用过多
 */
public class UserCacheUtils {

  private static Map<String, IMUser> userMap;

  static {
    userMap = new HashMap<String, IMUser>();
  }

  public static IMUser getCachedUser(String objectId) {
    return userMap.get(objectId);
  }

  public static boolean hasCachedUser(String objectId) {
    return userMap.containsKey(objectId);
  }

  public static void cacheUser(IMUser user) {
    if (null != user && !TextUtils.isEmpty(user.getObjectId())) {
      userMap.put(user.getObjectId(), user);
    }
  }

  public static void cacheUsers(List<IMUser> users) {
    if (null != users) {
      for (IMUser user : users) {
        cacheUser(user);
      }
    }
  }

  public static void fetchUsers(List<String> ids) {
    fetchUsers(ids, null);
  }

  public static void fetchUsers(final List<String> ids, final CacheUserCallback cacheUserCallback) {
    Set<String> uncachedIds = new HashSet<String>();
    for (String id : ids) {
      if (!userMap.containsKey(id)) {
        uncachedIds.add(id);
      }
    }

    if (uncachedIds.isEmpty()) {
      if (null != cacheUserCallback) {
        cacheUserCallback.done(getUsersFromCache(ids), null);
        return;
      }
    }

    AVQuery<IMUser> q = IMUser.getQuery(IMUser.class);
    q.whereContainedIn(Constants.OBJECT_ID, uncachedIds);
    q.setLimit(1000);
    q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
    q.findInBackground(new FindCallback<IMUser>() {
      @Override
      public void done(List<IMUser> list, AVException e) {
        if (null == e) {
          for (IMUser user : list) {
            userMap.put(user.getObjectId(), user);
          }
        }
        if (null != cacheUserCallback) {
          cacheUserCallback.done(getUsersFromCache(ids), e);
        }
      }
    });
  }

  public static List<IMUser> getUsersFromCache(List<String> ids) {
    List<IMUser> userList = new ArrayList<IMUser>();
    for (String id : ids) {
      if (userMap.containsKey(id)) {
        userList.add(userMap.get(id));
      }
    }
    return userList;
  }

  public static abstract class CacheUserCallback {
    public abstract void done(List<IMUser> userList, Exception e);
  }
}
