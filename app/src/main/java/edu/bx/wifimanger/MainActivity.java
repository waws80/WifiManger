package edu.bx.wifimanger;

import android.app.Service;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import edu.bx.wifimanger.adapter.WifiInfoAdapter;

public class MainActivity extends AppCompatActivity {
    /**
     * 界面wifi开关按钮
     */
    private Button activity_btn_wifi_switch;
    /**
     * 界面wifi信息显示
     */
    private ListView activity_lv;
    /**
     * wifi管理器
     */
    private WifiManager wifiManger;

    private Handler mOpenHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (checkWifiStatue()==WifiManager.WIFI_STATE_ENABLED){
                activity_btn_wifi_switch.setVisibility(View.GONE);
                activity_lv.setVisibility(View.VISIBLE);
                mOpenHandler.removeMessages(0x100);

            }else {
                mOpenHandler.sendEmptyMessage(0x100);
            }
        }
    };

    private Handler mCloseHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (checkWifiStatue()==WifiManager.WIFI_STATE_DISABLED){
                activity_btn_wifi_switch.setVisibility(View.VISIBLE);
                activity_lv.setVisibility(View.GONE);
                mCloseHandler.removeMessages(0x110);

            }else {
                mCloseHandler.sendEmptyMessage(0x110);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        wifiManger = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        //判断当前设备wifi状态
        initWifiStateLayout();

    }

    /**
     * 判断当前设备wifi状态
     *
     */
    private int checkWifiStatue(){
        return wifiManger.getWifiState();
    }

    /**
     * 初始化布局
     */
    private void init() {
        activity_btn_wifi_switch= (Button) findViewById(R.id.activity_wifi_switch);
        activity_lv= (ListView) findViewById(R.id.activity_rv);
        initListLayout();
        initListener();
    }

    /**
     * 初始化wifi状态布局
     */

    private void initWifiStateLayout(){
        /**
         * WIFI_STATE_DISABLED  1    WIFI网卡不可用
         WIFI_STATE_DISABLING   0	 WIFI正在关闭
         WIFI_STATE_ENABLED	    3	 WIFI网卡可用
         WIFI_STATE_ENABLING    2	 WIFI网卡正在打开
         WIFI_STATE_UNKNOWN	    4	 未知网卡状态
         */
        if (checkWifiStatue()==WifiManager.WIFI_STATE_ENABLED){
            activity_btn_wifi_switch.setVisibility(View.GONE);
            activity_lv.setVisibility(View.VISIBLE);


        }else if (checkWifiStatue()==WifiManager.WIFI_STATE_DISABLED){
            activity_lv.setVisibility(View.GONE);
            activity_btn_wifi_switch.setVisibility(View.VISIBLE);

        }
    }

    /**
     * 初始化wifi信息列表
     */
    private void initListLayout(){
        activity_lv.setAdapter(new WifiInfoAdapter(this));
        View header=View.inflate(this,R.layout.lv_header,null);
        activity_lv.addHeaderView(header);
    }

    /**
     * 打开wifi按钮设置监听事件
     */
    private void initListener() {
        activity_btn_wifi_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当wifi关闭时才可以执行打开wifi操作
                if (checkWifiStatue()==WifiManager.WIFI_STATE_DISABLED){
                    wifiManger.setWifiEnabled(true);
                    mOpenHandler.sendEmptyMessage(0x100);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(R.id.menu_group,R.id.wifi_switch,2,"wifi开关");
        menu.add(R.id.menu_group,R.id.wifi_hot,3,"wifi热点");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.wifi_switch:
                if (checkWifiStatue()==WifiManager.WIFI_STATE_ENABLED){
                    wifiManger.setWifiEnabled(false);
                    mCloseHandler.sendEmptyMessage(0x110);
                }
                break;
            case R.id.wifi_hot:
                Toast.makeText(this, "wifi热点", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
