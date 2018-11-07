package com.dotwin.sensordemo;

import android.hardware.Sensor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorViewHolder> {
    private List<SensorData> mData;

    public SensorAdapter(List<SensorData> mData) {
        this.mData = mData;
    }

    public void replaceData(List<SensorData> data) {
        mData = data;
    }
    @NonNull
    @Override
    public SensorAdapter.SensorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sensor, null);
        SensorViewHolder holder = new SensorViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SensorAdapter.SensorViewHolder viewHolder, int i) {

        SensorData item = mData.get(i);
        viewHolder.timeTV.setText(String.valueOf(item.time) + "ms");

        if (item.type == Sensor.TYPE_GYROSCOPE) {
            viewHolder.textView0.setText(format(item.values[0]));
            viewHolder.textView1.setText(format(item.values[1]));
            viewHolder.textView2.setText(format(item.values[2]));
        } else {
            viewHolder.textView0.setText(String.valueOf(item.values[0]));
            viewHolder.textView1.setText(String.valueOf(item.values[1]));
            viewHolder.textView2.setText(String.valueOf(item.values[2]));
        }
    }


    private static String format(float value) {
        return new DecimalFormat("0.000000000000000").format(value);
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class SensorViewHolder extends RecyclerView.ViewHolder {
        public TextView timeTV;
        public TextView textView0;
        public TextView textView1;
        public TextView textView2;

        public SensorViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTV = itemView.findViewById(R.id.time_tv);
            textView0 = itemView.findViewById(R.id.tv0);
            textView1 = itemView.findViewById(R.id.tv1);
            textView2 = itemView.findViewById(R.id.tv2);
        }


    }
}
