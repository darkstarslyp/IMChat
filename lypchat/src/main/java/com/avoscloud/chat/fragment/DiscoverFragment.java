package com.avoscloud.chat.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avoscloud.chat.R;
import com.avoscloud.chat.App;
import com.avoscloud.leanchatlib.adapter.HeaderListAdapter;
import com.avoscloud.chat.util.UserCacheUtils;
import com.avoscloud.chat.service.PreferenceMap;
import com.avoscloud.chat.viewholder.DiscoverItemHolder;
import com.avoscloud.chat.model.IMUser;
import com.avoscloud.leanchatlib.utils.Constants;
import com.avoscloud.leanchatlib.utils.LogUtils;
import com.avoscloud.leanchatlib.view.RefreshableRecyclerView;

import java.util.List;

/**
 * Created by lzw on 14-9-17.
 */
public class DiscoverFragment extends BaseFragment {

  private final SortDialogListener distanceListener = new SortDialogListener(Constants.ORDER_DISTANCE);
  private final SortDialogListener updatedAtListener = new SortDialogListener(Constants.ORDER_UPDATED_AT);

  @Bind(R.id.fragment_near_srl_pullrefresh)
  protected SwipeRefreshLayout refreshLayout;

  @Bind(R.id.fragment_near_srl_view)
  protected RefreshableRecyclerView recyclerView;

  protected LinearLayoutManager layoutManager;

  HeaderListAdapter<IMUser> discoverAdapter;
  int orderType;
  PreferenceMap preferenceMap;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.discover_fragment, container, false);
    ButterKnife.bind(this, view);

    layoutManager = new LinearLayoutManager(getActivity());
    discoverAdapter = new HeaderListAdapter<>(DiscoverItemHolder.class);
    recyclerView.setOnLoadDataListener(new RefreshableRecyclerView.OnLoadDataListener() {
      @Override
      public void onLoad(int skip, int limit, boolean isRefresh) {
        loadMoreDiscoverData(skip, limit, isRefresh);
      }
    });
    recyclerView.setRelationSwipeLayout(refreshLayout);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(discoverAdapter);
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    preferenceMap = PreferenceMap.getCurUserPrefDao(getActivity());
    orderType = preferenceMap.getNearbyOrder();




//    toolbar.showRightImageButton(R.drawable.nearby_order, new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(R.string.discover_fragment_sort).setPositiveButton(R.string.discover_fragment_loginTime,
//          updatedAtListener).setNegativeButton(R.string.discover_fragment_distance, distanceListener).show();
//      }
//    });
    recyclerView.refreshData();
  }

  /**
   * 加载数据
   * @param skip
   * @param limit
   * @param isRefresh
   */
  private void loadMoreDiscoverData(final int skip, final int limit, final boolean isRefresh) {
    PreferenceMap preferenceMap = PreferenceMap.getCurUserPrefDao(App.ctx);
    AVGeoPoint geoPoint = preferenceMap.getLocation();
    if (geoPoint == null) {
      LogUtils.i("geo point is null");
      return;
    }
    AVQuery<IMUser> q = IMUser.getQuery(IMUser.class);
    IMUser user = IMUser.getCurrentUser();
    q.whereNotEqualTo(Constants.OBJECT_ID, user.getObjectId());
    if (orderType == Constants.ORDER_DISTANCE) {
      q.whereNear(IMUser.LOCATION, geoPoint);
    } else {
      q.orderByDescending(Constants.UPDATED_AT);
    }
    q.skip(skip);
    q.limit(limit);
    q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
    q.findInBackground(new FindCallback<IMUser>() {
      @Override
      public void done(List<IMUser> list, AVException e) {
        if(e==null){
          UserCacheUtils.cacheUsers(list);
          recyclerView.setLoadComplete(list.toArray(), isRefresh);
        }else{
          e.printStackTrace();
        }

      }
    });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    preferenceMap.setNearbyOrder(orderType);
  }

  public class SortDialogListener implements DialogInterface.OnClickListener {
    int orderType;

    public SortDialogListener(int orderType) {
      this.orderType = orderType;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
      DiscoverFragment.this.orderType = orderType;
      recyclerView.refreshData();
    }
  }
}
