package org.csware.ee.shipper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gghl.view.wheelcity.AddressData;
import com.gghl.view.wheelcity.OnWheelChangedListener;
import com.gghl.view.wheelcity.WheelView;
import com.gghl.view.wheelcity.adapters.AbstractWheelTextAdapter;
import com.gghl.view.wheelcity.adapters.ArrayWheelAdapter;
import com.gghl.view.wheelview.JudgeDate;
import com.gghl.view.wheelview.ScreenInfo;
import com.gghl.view.wheelview.WheelMain;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.MyAlertDialog;

import org.csware.ee.app.ActivityBase;
import org.csware.ee.utils.Tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DemoActivity extends ActivityBase {

    private Button btn0;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    WheelMain wheelMain;
    EditText txttime;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String cityTxt;
    String url = "http://192.168.0.208:8025/GGangLog/AppInstallationLog?&versionNo1.0&mobileModel=Xiaomi-HM%20NOTE%201LTE&macAndroid_ebe1221a-4d6f-4c9b-a019-28d963dae169&appType4&mobilenull&ip10.0.2.15";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_demo);


        initView();
        txttime = (EditText) findViewById(R.id.txttime);
        Calendar calendar = Calendar.getInstance();
        txttime.setText(calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + "");

    }



    private void initView() {
        btn0 = (Button) findViewById(R.id.btn0);
        btn0.setOnClickListener(this);
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
        btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(this);
        btn5 = (Button) findViewById(R.id.btn5);
        btn5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn0:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String json =  Tools.GetMethod(url);
//                        Log.e("DemoActivity",json);
                    }
                }).start();;

                //                //AreaItem[] items = new AreaItem[]{new AreaItem(0,1,"test1"),new AreaItem(0,2,"test2")};
//                String js = "{\"province\":[{\"name\":\"北京市\",\"areaId\":\"110000\",\"parentId\":\"0\"},{\"name\":\"天津市\",\"areaId\":\"120000\",\"parentId\":\"0\"}]}";
//                try {
//
//                    JSONObject jo = new JSONObject(js);
//                    JSONArray items = jo.getJSONArray("province");
//                    int length = items.length();
//                    for(int i = 0; i < length; i++){//遍历JSONArray
//                        Log.d("debugTest", ":"+i);
//                        JSONObject oj = items.getJSONObject(i);
//                        Log.d("debugTestObj:", ":"+oj.get("name"));
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

//                Log.d("OOOOOO","HERE+++++++++++++++++++++++++++++++");
//                Log.d("Privince","size:"+ ChinaArea.getProvinces().size());
//                Log.d("Cities","size:"+ ChinaArea.getCities().size());
//                Log.d("Areas","size:"+ ChinaArea.getAreas().size());
//                Log.d("OOOOOO","HERE+++++++++++++++++++++++++++++++");
//
//
//                AreaItem[] items = ChinaArea.getAreas().toArray(new AreaItem[0]);
//                Log.d("Area","count:"+items.length);





                break;

            case R.id.btn1:

                LayoutInflater inflater = LayoutInflater.from(baseActivity);
                final View timepickerview = inflater.inflate(R.layout.timepicker,
                        null);
                ScreenInfo screenInfo = new ScreenInfo(baseActivity);
                wheelMain = new WheelMain(timepickerview);
                wheelMain.screenheight = screenInfo.getHeight();
                String time = txttime.getText().toString();
                Calendar calendar = Calendar.getInstance();
                if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
                    try {
                        calendar.setTime(dateFormat.parse(time));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                wheelMain.initDateTimePicker(year, month, day);
                new AlertDialog.Builder(baseActivity)
                        .setTitle("选择时间")
                        .setView(timepickerview)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        txttime.setText(wheelMain.getTime());
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                }).show();
                break;
            case R.id.btn2:
                new ActionSheetDialog(baseActivity)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("用相机更换头像", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {

                                    }
                                })
                        .addSheetItem("去相册选择头像", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {

                                    }
                                }).show();
                break;
            case R.id.btn3:
                new ActionSheetDialog(baseActivity)
                        .builder()
                        .setTitle("请选择操作")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("条目一", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Toast.makeText(baseActivity,
                                                "item" + which, Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                })
                        .addSheetItem("条目二", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Toast.makeText(baseActivity,
                                                "item" + which, Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                })
                        .addSheetItem("条目三", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Toast.makeText(baseActivity,
                                                "item" + which, Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                })
                        .addSheetItem("条目四", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Toast.makeText(baseActivity,
                                                "item" + which, Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                })
                        .addSheetItem("条目五", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Toast.makeText(baseActivity,
                                                "item" + which, Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                })
                        .addSheetItem("条目六", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Toast.makeText(baseActivity,
                                                "item" + which, Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                })
                        .addSheetItem("条目七", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Toast.makeText(baseActivity,
                                                "item" + which, Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                })
                        .addSheetItem("条目八", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Toast.makeText(baseActivity,
                                                "item" + which, Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                })
                        .addSheetItem("条目九", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Toast.makeText(baseActivity,
                                                "item" + which, Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                })
                        .addSheetItem("条目十", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Toast.makeText(baseActivity,
                                                "item" + which, Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }).show();
                break;
            case R.id.btn4:
                LayoutInflater inflater1 = LayoutInflater.from(baseActivity);
                final View timepickerview1 = inflater1.inflate(R.layout.timepicker,
                        null);
                ScreenInfo screenInfo1 = new ScreenInfo(baseActivity);
                wheelMain = new WheelMain(timepickerview1);
                wheelMain.screenheight = screenInfo1.getHeight();
                String time1 = txttime.getText().toString();
                Calendar calendar1 = Calendar.getInstance();
                if (JudgeDate.isDate(time1, "yyyy-MM-dd")) {
                    try {
                        calendar1.setTime(dateFormat.parse(time1));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                int year1 = calendar1.get(Calendar.YEAR);
                int month1 = calendar1.get(Calendar.MONTH);
                int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
                wheelMain.initDateTimePicker(year1, month1, day1);
                final MyAlertDialog dialog = new MyAlertDialog(baseActivity)
                        .builder()
                        .setTitle(btn4.getText().toString())
                                // .setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
                                // .setEditText("1111111111111")
                        .setView(timepickerview1)
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                dialog.setPositiveButton("保存", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),
                                wheelMain.getTime(), 1).show();
                    }
                });
                dialog.show();
                break;
            case R.id.btn5:
                View view = dialogm();
                final MyAlertDialog dialog1 = new MyAlertDialog(baseActivity)
                        .builder()
                        .setTitle(btn5.getText().toString())
                                // .setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
                                // .setEditText("1111111111111")
                        .setView(view)
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                dialog1.setPositiveButton("保存", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), cityTxt, 1).show();
                    }
                });
                dialog1.show();
                break;
            default:
                break;
        }
    }

    private View dialogm() {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.wheelcity_cities_layout, null);
        final WheelView country = (WheelView) contentView
                .findViewById(R.id.wheelcity_country);
        country.setVisibleItems(3);
        country.setViewAdapter(new CountryAdapter(this));

        final String cities[][] = AddressData.CITIES;
        final String ccities[][][] = AddressData.COUNTIES;
        final WheelView city = (WheelView) contentView
                .findViewById(R.id.wheelcity_city);
        city.setVisibleItems(0);

        // 地区选择
        final WheelView ccity = (WheelView) contentView
                .findViewById(R.id.wheelcity_ccity);
        ccity.setVisibleItems(0);// 不限城市

        country.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateCities(city, cities, newValue);

//                Log.d("WheelDebug","oldValue:"+oldValue+"   newValue:"+newValue + "     getCurrentItem:"+country.getCurrentItem());

                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + " | "
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + " | "
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];


            }
        });

        city.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updatecCities(ccity, ccities, country.getCurrentItem(),
                        newValue);
                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + " | "
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + " | "
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
            }
        });

        ccity.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + " | "
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + " | "
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
            }
        });

        country.setCurrentItem(1);// 设置北京
        city.setCurrentItem(1);
        ccity.setCurrentItem(1);
        return contentView;
    }

    /**
     * Updates the city wheel
     */
    private void updateCities(WheelView city, String cities[][], int index) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
                cities[index]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }

    /**
     * Updates the ccity wheel
     */
    private void updatecCities(WheelView city, String ccities[][][], int index,
                               int index2) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
                ccities[index][index2]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }

    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private String countries[] = AddressData.PROVINCES;

        /**
         * Constructor
         */
        protected CountryAdapter(Context context) {
            super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
            setItemTextResource(R.id.wheelcity_country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return countries.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }


}
