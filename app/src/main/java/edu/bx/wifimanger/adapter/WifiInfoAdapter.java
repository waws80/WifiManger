package edu.bx.wifimanger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.bx.wifimanger.R;

/**
 * Created by lxf52 on 2017/3/9.
 */

public class WifiInfoAdapter extends BaseAdapter {
    private Context mCOntext;

    public WifiInfoAdapter(Context mCOntext) {
        this.mCOntext = mCOntext;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(mCOntext).inflate(R.layout.lv_item,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.xinhao.setImageResource(R.drawable.connect_signal_level_0);
        holder.wifiName.setText("lxf");
        holder.conn.setImageResource(R.drawable.aps_complain_checker);
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
