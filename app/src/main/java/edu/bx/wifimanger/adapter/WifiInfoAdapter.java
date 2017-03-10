package edu.bx.wifimanger.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.bx.wifimanger.R;

/**
 * Created by lxf52 on 2017/3/9.
 */

public class WifiInfoAdapter extends BaseAdapter {
    private Context mCOntext;

    private List<ScanResult> scanResults;

    private WifiManager wifiManager;

    public WifiInfoAdapter(Context mCOntext, List<ScanResult> scanResults,WifiManager wifiManager) {
        this.mCOntext = mCOntext;
        this.scanResults=scanResults;
        this.wifiManager=wifiManager;
    }

    @Override
    public int getCount() {
        return scanResults.size();
    }

    @Override
    public Object getItem(int position) {
        return scanResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScanResult scanResult = scanResults.get(position);
        ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(mCOntext).inflate(R.layout.lv_item,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        //判断信号强度
        if (Math.abs(scanResult.level)<60){
            holder.xinhao.setImageResource(R.drawable.connect_signal_level_3);
        }else if (60<=Math.abs(scanResult.level)&&Math.abs(scanResult.level)<70){
            holder.xinhao.setImageResource(R.drawable.connect_signal_level_2);
        }else if (70<=Math.abs(scanResult.level)&&Math.abs(scanResult.level)<80){
            holder.xinhao.setImageResource(R.drawable.connect_signal_level_1);
        }else {
            holder.xinhao.setImageResource(R.drawable.connect_signal_level_0);
        }
        holder.wifiName.setText(scanResult.SSID);
        if (wifiManager.getConnectionInfo().getSSID().replace(" ","").contains(scanResult.SSID)&& !scanResult.SSID
                .isEmpty()){
            holder.conn.setVisibility(View.VISIBLE);
            holder.conn.setImageResource(R.drawable.aps_complain_checker);
        }else {
            holder.conn.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder{
        ImageView xinhao;
        TextView wifiName;
        ImageView conn;

        public ViewHolder(View itemView) {
            xinhao= (ImageView) itemView.findViewById(R.id.item_iv_xinhao);
            wifiName= (TextView) itemView.findViewById(R.id.item_tv);
            conn= (ImageView) itemView.findViewById(R.id.item_iv_conn);
        }
    }
}
