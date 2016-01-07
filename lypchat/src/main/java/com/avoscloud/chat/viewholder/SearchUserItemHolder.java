package com.avoscloud.chat.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avoscloud.chat.R;
import com.avoscloud.chat.friends.ContactPersonInfoActivity;
import com.avoscloud.chat.model.IMUser;
import com.avoscloud.leanchatlib.utils.Constants;
import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.avoscloud.leanchatlib.viewholder.CommonViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by wli on 15/12/3.
 */
public class SearchUserItemHolder extends CommonViewHolder<IMUser> {

  private TextView nameView;
  private ImageView avatarView;
  private IMUser IMUser;

  public SearchUserItemHolder(Context context, ViewGroup root) {
    super(context, root, R.layout.search_user_item_layout);

    nameView = (TextView)itemView.findViewById(R.id.search_user_item_tv_name);
    avatarView = (ImageView)itemView.findViewById(R.id.search_user_item_im_avatar);

    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getContext(), ContactPersonInfoActivity.class);
        intent.putExtra(Constants.LEANCHAT_USER_ID, IMUser.getObjectId());
        getContext().startActivity(intent);
      }
    });
  }

  @Override
  public void bindData(final IMUser IMUser) {
    this.IMUser = IMUser;
    ImageLoader.getInstance().displayImage(IMUser.getAvatarUrl(), avatarView, PhotoUtils.avatarImageOptions);
    nameView.setText(IMUser.getUsername());
  }

  public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<SearchUserItemHolder>() {
    @Override
    public SearchUserItemHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
      return new SearchUserItemHolder(parent.getContext(), parent);
    }
  };
}

