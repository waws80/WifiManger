package edu.bx.wifimanger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

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
    private View header;
    private List<ScanResult> wifiMangerScanResults;


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
       // initListLayout();
        initListener();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(new WifiReceiver(),filter);
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
            activity_lv.setAdapter(null);
            initListLayout();


        }else if (checkWifiStatue()==WifiManager.WIFI_STATE_DISABLED){
            activity_lv.setVisibility(View.GONE);
            activity_btn_wifi_switch.setVisibility(View.VISIBLE);

        }
    }

    /**
     * 初始化wifi信息列表
     */
    private void initListLayout(){
        wifiMangerScanResults = wifiManger.getScanResults();
        activity_lv.setAdapter(new WifiInfoAdapter(this, wifiMangerScanResults,wifiManger));
        header = View.inflate(this, R.layout.lv_header,null);
        if (activity_lv.getHeaderViewsCount()==0){
            activity_lv.addHeaderView(header);
        }
        initHeader();

        initLVItemClick();

    }

    /**
     * 初始化wifi列表点击事件
     */
    private void initLVItemClick() {
        activity_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv=new TextView(MainActivity.this);
                        if (wifiMangerScanResults!=null){
                            tv.setText(wifiMangerScanResults.get(position).SSID+"-------------"+position);
                            BottomSheetDialog dialog=new BottomSheetDialog(MainActivity.this);
                            dialog.setContentView(tv);
                            dialog.show();
                        }
            }
        });
    }

    /**
     * 初始化头部数据（当前连接的wifi信息）
     */
    private void initHeader() {
        WifiInfo connectionInfo = wifiManger.getConnectionInfo();
        TextView title = (TextView) header.findViewById(R.id.header_tv_title);
        TextView loc= (TextView) header.findViewById(R.id.header_tv_loc);
        TextView xinhao= (TextView) header.findViewById(R.id.header_tv_xinhao);
        String ssid = connectionInfo.getSSID();
        title.setText(ssid.isEmpty()?"成功连接未知":"成功连接"+ssid);
        //判断信号强度
        if (Math.abs(connectionInfo.getRssi())<65){
           xinhao.setText("信号：强");
        }else if (65<=Math.abs(connectionInfo.getRssi())&&Math.abs(connectionInfo.getRssi())<75){
            xinhao.setText("信号：中");
        }else {
            xinhao.setText("信号：弱");
        }
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

                }
                break;
            case R.id.wifi_hot:
                Toast.makeText(this, "wifi热点", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private class WifiReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
//                mScanResults = mWifiManager.getScanResults();
//                Log.d(TAG, "mScanResults.size()===" + mScanResults.size());
                activity_btn_wifi_switch.setVisibility(View.GONE);
                activity_lv.setAdapter(null);
                activity_lv.setVisibility(View.VISIBLE);
                initListLayout();

            }
//系统wifi的状态
            else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int wifiState = intent.getIntExtra(
                        WifiManager.EXTRA_WIFI_STATE, 0);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_ENABLED:
//                        Log.d(TAG, "WiFi已启用" + DateUtils.getCurrentTime());
                        wifiManger.startScan();
                        break;
                    case WifiManager.WIFI_STATE_DISABLED:
                        activity_btn_wifi_switch.setVisibility(View.VISIBLE);
                        activity_lv.setAdapter(null);
                        activity_lv.removeHeaderView(header);
                        activity_lv.setVisibility(View.GONE);
                        break;
                }
            }
        }


    }
}
