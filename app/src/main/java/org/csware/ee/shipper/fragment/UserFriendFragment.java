package org.csware.ee.shipper.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.csware.ee.api.ContactApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.app.QCloudService;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.ContactListReturn;
import org.csware.ee.model.DeliverInfo;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserBeaerActivity;
import org.csware.ee.shipper.UserFriendFragmentActivity;
import org.csware.ee.utils.ResourceHelper;
import org.csware.ee.view.CommonAdapter;
import org.csware.ee.view.ViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 常用司机
 * Created by Yu on 2015/6/30.
 */
public class UserFriendFragment extends FragmentBase {
    final static String TAG = "UserFriend";

    @Override
    protected boolean getIsPrepared() {
        return true;
    }

    @Override
    protected void lazyLoad() {
        Tracer.d(TAG, "lazyLoad");

    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_friend_fragment;
    }


    /**
     * 给ListView添加下拉刷新
     */
    private SwipeRefreshLayout swipeLayout;

    /**
     * ListView
     */
    private ListView listView;

    /**
     * ListView适配器
     */
//    CommonAdapter<ContactListReturn.ContactsEntity> adapter;
    private List<ContactListReturn.ContactsEntity> infoList;
    UserFriendFragmentActivity activity;
    DbCache _dbCache;
    DeliverInfo _info;
    @Override
    public void init() {
        _dbCache = new DbCache(baseActivity);
        try {
            _info = _dbCache.GetObject(DeliverInfo.class);
            if (_info == null) {
                _info = new DeliverInfo();
            }
        } catch (Exception e) {
            //序列化错，重新创建对象
            _info = new DeliverInfo();
        }
        activity = (UserFriendFragmentActivity) getActivity();
        activity.showMenu();//禁用
        activity.finish(getString(R.string.title_activity_user_friend));

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        swipeLayout.setOnRefreshListener(refreshListener);

        // 顶部刷新的样式
        swipeLayout.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_blue_bright);

        infoList = new ArrayList<>();
        ContactListReturn.ContactsEntity info = new ContactListReturn.ContactsEntity();
        listView = (ListView) rootView.findViewById(R.id.listview);
        freindAdapter = new FreindListAdapter(infoList);
//        adapter = new CommonAdapter<ContactListReturn.ContactsEntity>(baseActivity, infoList, R.layout.list_item_friend) {
//
//            /**重写，返回了序列元素的标识Id
//             * 返回0时表示没有主键Id
//             * */
//            @Override
//            public long getItemId(int position) {
//                ContactListReturn.ContactsEntity item = infoList.get(position);
//                //Tracer.e(TAG,"itemId="+item.getId());
//                if (item != null)
//                    return item.OwnerContactId;
//                return 0;
//            }
//
//            // 出价列表数据填充
//            @Override
//            public void convert(ViewHolder helper, ContactListReturn.ContactsEntity item) {
//                if (item == null) return;
//                helper.setText(R.id.labName, item.Name + "");
//                helper.setText(R.id.labCarNo, item.Plate + "");
//                helper.setTag(R.id.chkBox, item.OwnerContactId + "");
//                final ImageView headPic = helper.getView(R.id.ivHeadPic);
//                QCloudService.asyncDisplayImage(baseActivity, item.Avatar, headPic);
//            }
//
//        };
        listView.setAdapter(freindAdapter);
        final SharedPreferences preferences = baseActivity.getSharedPreferences("Contact", Context.MODE_PRIVATE); //intent.putExtra("action", "select");
        final String contact = preferences.getString("action","");
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (contact.equals("select")){
                    _info.hackmanPhone =infoList.get(position).Mobile;
                    _dbCache.save(_info);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear().commit();
                    UserFriendFragmentActivity.instance.finish();
                }else {
                    Intent intent = new Intent(getActivity(), UserBeaerActivity.class);
                    intent.putExtra("BeaerInfo", infoList.get(position));
                    startActivity(intent);
                }
            }
        };
        listView.setOnItemClickListener(itemClickListener);

        syncLoadFriends();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = baseActivity.getSharedPreferences("Contact", Context.MODE_PRIVATE); //intent.putExtra("action", "select");
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();
    }

    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        public void onRefresh() {
            syncLoadFriends();
        }
    };


    void syncLoadFriends() {

        ContactApi.list(baseFragment.getActivity(), baseActivity, new IJsonResult<ContactListReturn>() {
            @Override
            public void ok(ContactListReturn json) {
                swipeLayout.setRefreshing(false);
                if (json.Contacts != null ){
                    infoList.clear();
                    infoList.addAll(json.Contacts);
                }
                freindAdapter.notifyDataSetChanged();
            }

            @Override
            public void error(Return json) {

            }
        });

    }
    Map<Integer, Boolean> isCheckMap =  new HashMap<Integer, Boolean>();
    FreindListAdapter freindAdapter;
    public void showCheckBox() {
        //listView.getFocusedChild();
        if (listView == null){
            return;
        }
        ////因为listview.getcount只是得到当前页面可以显示的listview的item，所以i的值会大于它
//        CheckBox checkBox = (CheckBox) listView.getAdapter().getView(i, null, null);
        int num_of_visible_view=listView.getLastVisiblePosition() -
                listView.getFirstVisiblePosition();
//        int max = listView.getCount();
//        activity.showDelMenu();
        for (int i = 0; i < num_of_visible_view; i++) {
            //找到需要选中的条目

            View v = listView.getChildAt(i);
            if (v==null){
//                Tracer.d(TAG, listView.getChildAt(i)+"showCheckBox:" + listView.getCount());
                return;
            }
            CheckBox chkBox = (CheckBox) v.findViewById(R.id.chkBox);
            if(isCheckMap!=null && isCheckMap.containsKey(i))
            {
                chkBox.setChecked(isCheckMap.get(i));
            }
            else
            {
                chkBox.setChecked(false);
            }
            chkBox.setVisibility(View.VISIBLE);
            final int finalI = i;
            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int radiaoId = finalI;
                    if (isChecked) {
                        //将选中的放入hashmap中
                        isCheckMap.put(radiaoId, isChecked);
                    } else {
                        //取消选中的则剔除
                        isCheckMap.remove(radiaoId);
                    }
                }
            });
        }
        activity.showDelMenu();

    }

    int count = 0;

    public void delCheckedHackman() {
        //Tracer.d(TAG, "showCheckBox:" + listView.getCount());
//        int max = listView.getCount();
        int max=listView.getLastVisiblePosition() -
                listView.getFirstVisiblePosition();
        if (max>0) {
            count = max;
            for (int i = 0; i < max; i++) {
                View v = listView.getChildAt(i);
                CheckBox chkBox = (CheckBox) v.findViewById(R.id.chkBox);
                String id = chkBox.getTag().toString();
                Tracer.i(TAG, "delete phone=" + id);
                if (!chkBox.isChecked()) {
                    count--;
                    continue;
                }

                ContactApi.delete(baseFragment.getActivity(), baseActivity, id, new IJsonResult() {
                    @Override
                    public void ok(Return json) {
                        count--;
                        if (count == 0) {
                            toastFast("删除完成");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    activity.UserFriendFrag = new UserFriendFragment();
                                    activity.replace(activity.UserFriendFrag, "常用司机");
                                    activity.showMenu();
                                }
                            }, 500);
                        }
                    }

                    @Override
                    public void error(Return json) {

                    }
                });
            }

        }else {
            toastFast("没有可删司机");
        }
    }

    class FreindListAdapter extends BaseAdapter {

        final static String TAG = "BankListAdapter";
        Map<Integer, Boolean> isCheckMap =  new HashMap<Integer, Boolean>();
        List<ContactListReturn.ContactsEntity> infoList = new ArrayList<>();
        public FreindListAdapter(List<ContactListReturn.ContactsEntity> infoList) {
            this.infoList = infoList;
        }

        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public Object getItem(int position) {
            return infoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            View view = convertView;
            //Log.w(TAG, ":" + arrData[position] + "-" + value);
            if (view == null) {
                view = LayoutInflater.from(baseActivity).inflate(
                        R.layout.list_item_friend, null);
//                view = layoutInflater.inflate(R.layout.list_item_friend, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.labCarNo = (TextView) view.findViewById(R.id.labCarNo);
                viewHolder.labName = (TextView) view.findViewById(R.id.labName);
                viewHolder.ivHeadPic = (ImageView) view.findViewById(R.id.ivHeadPic);
                viewHolder.chkBox = (CheckBox) view.findViewById(R.id.chkBox);
                view.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) view.getTag();
                //找到需要选中的条目
                if(isCheckMap!=null && isCheckMap.containsKey(position))
                {
                    viewHolder.chkBox.setChecked(isCheckMap.get(position));
                }
                else
                {
                    viewHolder.chkBox.setChecked(false);
                }
            }
            viewHolder.chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int radiaoId = position;
                    if(isChecked)
                    {
                        //将选中的放入hashmap中
                        isCheckMap.put(radiaoId, isChecked);
                    }
                    else
                    {
                        //取消选中的则剔除
                        isCheckMap.remove(radiaoId);
                    }
                }
            });
            viewHolder.labName.setText(infoList.get(position).Name);
            viewHolder.labCarNo.setText(infoList.get(position).Plate);
            QCloudService.asyncDisplayImage(baseActivity, infoList.get(position).Avatar, viewHolder.ivHeadPic);

            return view;
        }

         class ViewHolder {
             TextView labName,labCarNo;
             ImageView ivHeadPic;
             CheckBox chkBox;
        }
    }
}
