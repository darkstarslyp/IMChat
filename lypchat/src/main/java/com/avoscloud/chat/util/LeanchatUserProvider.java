package com.avoscloud.chat.util;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FindCallback;
import com.avoscloud.chat.friends.FriendsManager;
import com.avoscloud.chat.model.IMUser;
import com.avoscloud.leanchatlib.utils.ThirdPartUserUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wli on 15/12/4.
 */
public class LeanchatUserProvider implements ThirdPartUserUtils.ThirdPartDataProvider {
  @Override
  public ThirdPartUserUtils.ThirdPartUser getSelf() {
    return new ThirdPartUserUtils.ThirdPartUser("daweibayu", "daweibayu",
      "http://ac-x3o016bx.clouddn.com/CsaX0GuXL7gXWBkaBFXfBWZPlcanClEESzHxSq2T.jpg");
  }

  @Override
  public void getFriend(String userId, final ThirdPartUserUtils.FetchUserCallBack callBack) {
    if (UserCacheUtils.hasCachedUser(userId)) {
      callBack.done(Arrays.asList(getThirdPartUser(UserCacheUtils.getCachedUser(userId))), null);
    } else {
      UserCacheUtils.fetchUsers(Arrays.asList(userId), new UserCacheUtils.CacheUserCallback() {
        @Override
        public void done(List<IMUser> userList, Exception e) {
          callBack.done(getThirdPartUsers(userList), e);
        }
      });
    }
  }

  @Override
  public void getFriends(List<String> list, final ThirdPartUserUtils.FetchUserCallBack callBack) {
    UserCacheUtils.fetchUsers(list, new UserCacheUtils.CacheUserCallback() {
      @Override
      public void done(List<IMUser> userList, Exception e) {
        callBack.done(getThirdPartUsers(userList), e);
      }
    });
  }

  @Override
  public void getFriends(int skip, int limit, final ThirdPartUserUtils.FetchUserCallBack callBack) {
    FriendsManager.fetchFriends(false, new FindCallback<IMUser>() {
      @Override
      public void done(List<IMUser> list, AVException e) {
        callBack.done(getThirdPartUsers(list), e);
      }
    });
  }

  private static ThirdPartUserUtils.ThirdPartUser getThirdPartUser(IMUser IMUser) {
    return new ThirdPartUserUtils.ThirdPartUser(IMUser.getObjectId(), IMUser.getUsername(), IMUser.getAvatarUrl());
  }

  public static List<ThirdPartUserUtils.ThirdPartUser> getThirdPartUsers(List<IMUser> IMUsers) {
    List<ThirdPartUserUtils.ThirdPartUser> thirdPartUsers = new ArrayList<>();
    for (IMUser user : IMUsers) {
      thirdPartUsers.add(getThirdPartUser(user));
    }
    return thirdPartUsers;
  }
}
