package org.csware.ee.shipper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;

import org.csware.ee.Guard;
import org.csware.ee.api.ContactApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.QCloudService;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IFragment;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.ActionType;
import org.csware.ee.model.ContactListReturn;
import org.csware.ee.model.DeliverInfo;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.fragment.UserAddFriendFragment;
import org.csware.ee.shipper.fragment.UserFriendFragment;
import org.csware.ee.view.TopActionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class UserFriendFragmentActivity extends FragmentActivityBase implements IFragment {

    final static String TAG = "UserFriend";
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.boxFragment)
    FrameLayout boxFragment;
    @InjectView(R.id.LinAddFreind)
    LinearLayout LinAddFreind;
    @InjectView(R.id.txtPhone)
    EditText txtPhone;
    @InjectView(R.id.btnConfirm)
    Button btnConfirm;
    View main;

    private static Handler sHandler;
    public static Activity instance;

    /**
     * 给ListView添加下拉刷新
     */
    private SwipeRefreshLayout swipeLayout;

    /**
     * ListView
     */
    private ListView listView;
    private List<ContactListReturn.ContactsEntity> infoList;
    FreindListAdapter freindAdapter;
    DbCache _dbCache;
    DeliverInfo _info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        main = getLayoutInflater().inflate(R.layout.user_friend_fragment_activity, null);
//        main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        main.setOnClickListener(this);
//        setContentView(main);
        setContentView(R.layout.user_friend_fragment_activity);
        ButterKnife.inject(this);
//        Log.d(TAG, "onCreate");
        LinAddFreind.setVisibility(View.GONE);
        init();
        instance = this;
        sHandler = new Handler();

        sHandler.post(mHideRunnable); // hide the navigation bar
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                sHandler.post(mHideRunnable); // hide the navigation bar
            }
        });

//        UserFriendFrag = new UserFriendFragment();
        topBar.setTitle(getString(R.string.title_activity_del_friend));
//        topBar.setTitle(getString(R.string.title_activity_user_friend));
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.boxFragment, UserFriendFrag)
//                .commit();

//        hideMenu();
        nofity(false);
        showDelMenu();

    }

    private void init() {
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
        showMenu();
        finish(getString(R.string.title_activity_user_friend));
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeLayout.setOnRefreshListener(refreshListener);

        // 顶部刷新的样式
        swipeLayout.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_blue_bright);

        infoList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview);
        freindAdapter = new FreindListAdapter(infoList,true);
        listView.setAdapter(freindAdapter);
        final SharedPreferences preferences = baseActivity.getSharedPreferences("Contact", Context.MODE_PRIVATE); //intent.putExtra("action", "select");
        final String contact = preferences.getString("action", "");
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
                    Intent intent = new Intent(UserFriendFragmentActivity.this, UserBeaerActivity.class);
                    intent.putExtra("BeaerInfo", infoList.get(position));
                    startActivity(intent);
                }
            }
        };
//        listView.setOnItemClickListener(itemClickListener);

        syncLoadFriends();
    }

    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        public void onRefresh() {
            syncLoadFriends();
        }
    };

    void syncLoadFriends() {

        ContactApi.list(baseActivity, baseActivity, new IJsonResult<ContactListReturn>() {
            @Override
            public void ok(ContactListReturn json) {
                swipeLayout.setRefreshing(false);
                if (json.Contacts != null) {
                    infoList.clear();
                    infoList.addAll(json.Contacts);
                }
                freindAdapter.notifyDataSetChanged();
                nofity(false);
            }

            @Override
            public void error(Return json) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = baseActivity.getSharedPreferences("Contact", Context.MODE_PRIVATE); //intent.putExtra("action", "select");
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();
    }
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            int flags;
            int curApiVersion = android.os.Build.VERSION.SDK_INT;
            // This work only for android 4.4+
            if(curApiVersion >= Build.VERSION_CODES.KITKAT){
                // This work only for android 4.4+
                // hide navigation bar permanently in android activity
                // touch the screen, the navigation bar will not show
                flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN;

            }else{
                // touch the screen, the navigation bar will show
                flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }

            // must be executed in main thread :)
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    };

    public UserFriendFragment UserFriendFrag;

    /**
     * 显示ActionBar右则的菜单
     */
    public void showMenu() {
        topBar.setMenu(R.drawable.w_cysj_icon_bj, new TopActionBar.MenuClickListener() {
            @Override
            public void menuClick() {
//                Log.d(TAG, "setMenuClickListener");
//                toggleHideyBar();
                baseActivity.setTheme(R.style.ActionSheetStyle);
                ActionSheet.createBuilder(baseActivity, getSupportFragmentManager())
                        .setCancelButtonTitle("取消")
                        .setOtherButtonTitles("添加司机", "删除司机")
                        .setCancelableOnTouchOutside(true)
                        .setListener(actionSheetListener).show();

            }
        });
        topBar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    @Override
    public void onClick(View v) {
//        int i = main.getSystemUiVisibility();
//        if (i == View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) {//2
//            main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//        } else if (i == View.SYSTEM_UI_FLAG_VISIBLE) {//0
//            main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
//        } else if (i == View.SYSTEM_UI_FLAG_LOW_PROFILE) {//1
//            main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        }
    }

    /**
     * 禁止显示
     */
    public void hideMenu() {
        topBar.setMenuClickListener(null);
    }
    /**
     * 隐藏topBar
     * */
    public void hideBar(){
        topBar.setVisibility(View.GONE);
    }
    /**
     * 显示删除按钮
     */
    public void showDelMenu() {
        topBar.setMenu(android.R.drawable.ic_menu_delete, new TopActionBar.MenuClickListener() {
            @Override
            public void menuClick() {
//                Log.d(TAG, "showDelMenu");
                new AlertDialog.Builder(baseActivity).setTitle("删除确认")//设置对话框标题
                        .setMessage("您确定要删除选中的司机吗？")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                delHackman();
                            }

                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮

                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        //Log.i("alertdialog", " 请保存数据！");
                    }
                }).show();//在按键响应事件中显示此对话框

            }
        });
    }


    ActionSheet.ActionSheetListener actionSheetListener = new ActionSheet.ActionSheetListener() {
        @Override
        public void onOtherButtonClick(ActionSheet actionSheet, int index) {
//            Log.d(TAG, "click item index = " + index);
            if (index == 0) {
                //添加司机
                swipeLayout.setVisibility(View.GONE);
                LinAddFreind.setVisibility(View.VISIBLE);
                hideMenu();
            } else if (index == 1) {
                nofity(true);
                showDelMenu();
            }

        }

        @Override
        public void onDismiss(ActionSheet actionSheet, boolean isCancle) {
//            Log.d(TAG, "dismissed isCancle = " + isCancle);
        }
    };

    List<ContactListReturn.ContactsEntity> tmpList = new ArrayList<>();
    void nofity(boolean isShowCBox){
        tmpList.clear();
        tmpList.addAll(infoList);
        Tracer.e(TAG,tmpList.size()+"");
        freindAdapter = new FreindListAdapter(tmpList,true);
        listView.setAdapter(freindAdapter);
        freindAdapter.notifyDataSetChanged();
    }

    int count = 0;
    public void delHackman(){
        int max = delList.size();
        if (max>0){
            count = max;
            for (int i=0;i<delList.size();i++) {
                String id = delList.get(i).OwnerContactId+"";
                final int finalI = i;
                ContactApi.delete(baseActivity, baseActivity, id, new IJsonResult() {
                    @Override
                    public void ok(Return json) {
                        count--;
                        if (count == 0) {
                            delList.clear();
                            toastFast("删除完成");
                            syncLoadFriends();
                            nofity(false);
                            showMenu();
                        }
                    }

                    @Override
                    public void error(Return json) {

                    }
                });
            }
        }else {
            toastFast("请选择司机");
        }
    }


    public void replace(Fragment fragment, String title) {

        topBar.setTitle(title);
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(title)
                .replace(R.id.boxFragment, fragment)
                .commit();
    }
//    public void back(String title){
//        topBar.setTitle(title);
//        topBar.setBackClickListener(new TopActionBar.BackClickListener() {
//            @Override
//            public void backClick() {
//                replace(new UserFriendFragment(), "常用司机");
//            }
//        });
//    }
    public void finish(String title){
        topBar.setTitle(title);
        topBar.setBackClickListener(new TopActionBar.BackClickListener() {
            @Override
            public void backClick() {
                finish();
            }
        });
    }
    public int pos(ContactListReturn.ContactsEntity entity,List<ContactListReturn.ContactsEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).OwnerContactId ==entity.OwnerContactId ) {
                return i;
            }
        }
        return -1;
    }
    List<ContactListReturn.ContactsEntity> delList = new ArrayList<>();
    class FreindListAdapter extends BaseAdapter {

        final static String TAG = "BankListAdapter";
        Map<Integer, Boolean> isCheckMap =  new HashMap<Integer, Boolean>();
        List<ContactListReturn.ContactsEntity> infoList = new ArrayList<>();
        boolean isShowCbox = false;
        public FreindListAdapter(List<ContactListReturn.ContactsEntity> infoList, boolean isShowCbox) {
            this.infoList = infoList;
            this.isShowCbox = isShowCbox;
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

            viewHolder.chkBox.setTag(infoList.get(position).OwnerContactId);
            if (isShowCbox){
                viewHolder.chkBox.setVisibility(View.VISIBLE);
            }else {
                viewHolder.chkBox.setVisibility(View.GONE);
            }

            viewHolder.chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int radiaoId = position;
                    if(isChecked)
                    {
                        //将选中的放入hashmap中
                        isCheckMap.put(radiaoId, isChecked);
                        delList.add(infoList.get(position));
                    }
                    else
                    {
                        //取消选中的则剔除
                        isCheckMap.remove(radiaoId);
                        delList.remove(pos(infoList.get(position),delList));
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

    @OnClick({R.id.btnConfirm})
    void addFriend(View v) {
//        Log.d(TAG, "addFriend");
        String phone = txtPhone.getText().toString();
        if (Guard.isNullOrEmpty(phone)) {
            toastFast("请输入常用司机的手机号");
            return;
        }

        ContactApi.edit(baseActivity, baseActivity, ActionType.ADD, phone, new IJsonResult() {
            @Override
            public void ok(Return json) {

                //成功
                toastFast("增加成功");
                topBar.setTitle("常用司机");
                swipeLayout.setVisibility(View.VISIBLE);
                LinAddFreind.setVisibility(View.GONE);
                txtPhone.setText("");
                showMenu();
                syncLoadFriends();

            }

            @Override
            public void error(Return json) {

            }
        });


    }

}
