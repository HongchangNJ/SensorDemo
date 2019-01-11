package com.dotwin.sensordemo;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static MainActivity instance;
    private RecyclerView mRecyclerView;
    private TextView mAccerTV;
    private TextView mGyroscopeTV;
    private TextView mMageticTV;
    private TextView mGravityTV;
    private TextView mOrientationTV;

    private TextView mStartTV;
    private TextView mPauseTV;

    private int mType;
    private int mState; // 0:运行状态 1 暂停状态

    private SensorAdapter mAdapter;
    private List<SensorData> mAccerList = new ArrayList<>();
    private List<SensorData> mGyroList = new ArrayList<>();
    private List<SensorData> mMageList = new ArrayList<>();
    private List<SensorData> mGravityList = new ArrayList<>();
    private List<SensorData> mOrientationList = new ArrayList<>();

    private long mAccerTime = System.currentTimeMillis();
    private long mGyroTime = System.currentTimeMillis();
    private long mMageTime = System.currentTimeMillis();
    private long mGravityTime = System.currentTimeMillis();
    private long mOrientationTime = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        mRecyclerView = findViewById(R.id.recyclerview);
        mAccerTV = findViewById(R.id.accer);
        mGyroscopeTV = findViewById(R.id.gyroscope);
        mMageticTV = findViewById(R.id.magnetic);
        mGravityTV = findViewById(R.id.gravity);
        mOrientationTV = findViewById(R.id.orientation);


        mStartTV = findViewById(R.id.start_tv);
        mPauseTV = findViewById(R.id.pause_tv);

        mStartTV.setOnClickListener(view -> {
            if (mState == 1) {
                mState = 0;
            }
            registerSensorListener();
            mAccerTime = System.currentTimeMillis();
            mGyroTime = System.currentTimeMillis();
            mMageTime = System.currentTimeMillis();
            mGravityTime = System.currentTimeMillis();
            mOrientationTime = System.currentTimeMillis();
        });

        mPauseTV.setOnClickListener(view -> {

            if (mState == 0) {
                unReigisterSensorListener();
                mState = 1;
            }
        });

        mAccerTV.setOnClickListener(view -> {
            mType = Sensor.TYPE_ACCELEROMETER;
            mAdapter.replaceData(mAccerList);

            if (mState == 1) {
                mAdapter.notifyDataSetChanged();
            }

            mAccerTV.setBackgroundColor(Color.RED);

            mGyroscopeTV.setBackgroundColor(Color.WHITE);
            mMageticTV.setBackgroundColor(Color.WHITE);
            mGravityTV.setBackgroundColor(Color.WHITE);
            mOrientationTV.setBackgroundColor(Color.WHITE);

        });

        mGyroscopeTV.setOnClickListener(view -> {
            mType = Sensor.TYPE_GYROSCOPE;
            mAdapter.replaceData(mGyroList);

            if (mState == 1) {
                mAdapter.notifyDataSetChanged();
            }

            mGyroscopeTV.setBackgroundColor(Color.RED);
            mAccerTV.setBackgroundColor(Color.WHITE);
            mMageticTV.setBackgroundColor(Color.WHITE);
            mGravityTV.setBackgroundColor(Color.WHITE);
            mOrientationTV.setBackgroundColor(Color.WHITE);

        });

        mMageticTV.setOnClickListener(view -> {
            mType = Sensor.TYPE_MAGNETIC_FIELD;
            mAdapter.replaceData(mMageList);

            if (mState == 1) {
                mAdapter.notifyDataSetChanged();
            }

            mMageticTV.setBackgroundColor(Color.RED);
            mGyroscopeTV.setBackgroundColor(Color.WHITE);
            mAccerTV.setBackgroundColor(Color.WHITE);
            mGravityTV.setBackgroundColor(Color.WHITE);
            mOrientationTV.setBackgroundColor(Color.WHITE);

        });

        mGravityTV.setOnClickListener(view -> {
            mType = Sensor.TYPE_GRAVITY;
            mAdapter.replaceData(mGravityList);

            if (mState == 1) {
                mAdapter.notifyDataSetChanged();
            }

            mMageticTV.setBackgroundColor(Color.WHITE);
            mGyroscopeTV.setBackgroundColor(Color.WHITE);
            mAccerTV.setBackgroundColor(Color.WHITE);
            mGravityTV.setBackgroundColor(Color.RED);
            mOrientationTV.setBackgroundColor(Color.WHITE);

        });

        mOrientationTV.setOnClickListener(view -> {
            mType = Sensor.TYPE_ORIENTATION;
            mAdapter.replaceData(mOrientationList);

            if (mState == 1) {
                mAdapter.notifyDataSetChanged();
            }

            mMageticTV.setBackgroundColor(Color.WHITE);
            mGyroscopeTV.setBackgroundColor(Color.WHITE);
            mAccerTV.setBackgroundColor(Color.WHITE);
            mGravityTV.setBackgroundColor(Color.WHITE);
            mOrientationTV.setBackgroundColor(Color.RED);

        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mType = Sensor.TYPE_ACCELEROMETER;
        mAdapter = new SensorAdapter(mAccerList);
        mRecyclerView.setAdapter(mAdapter);

        registerSensorListener();
    }

    private void unReigisterSensorListener() {
        SensorManager sm = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        sm.unregisterListener(this);
        mAdapter.notifyDataSetChanged();
    }

    private void registerSensorListener() {
        SensorManager sm = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);

        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();
        switch (type) {
            case Sensor.TYPE_ACCELEROMETER:
                //Log.d("Test", "X:" + event.values[0] + ", Y:" + event.values[1] + ", Z:" + event.values[2]);

                SensorData accerData = new SensorData();
                long currAccerTime = System.currentTimeMillis();
                accerData.time = currAccerTime - mAccerTime;
                mAccerTime = currAccerTime;

                accerData.values[0] = event.values[0];
                accerData.values[1] = event.values[1];
                accerData.values[2] = event.values[2];
                accerData.type = Sensor.TYPE_ACCELEROMETER;

                mAccerList.add(0, accerData);

                if (mAccerList.size() > 30) {
                    mAccerList.remove(30);
                }
                break;
            case Sensor.TYPE_GYROSCOPE:
                //Log.d("Test", "X:" + event.values[0] + ", Y:" + event.values[1] + ", Z:" + event.values[2]);
                SensorData gyroData = new SensorData();
                long currGyroTime = System.currentTimeMillis();
                gyroData.time = currGyroTime - mGyroTime;
                mGyroTime = currGyroTime;

                gyroData.values[0] = event.values[0];
                gyroData.values[1] = event.values[1];
                gyroData.values[2] = event.values[2];

                gyroData.type = Sensor.TYPE_GYROSCOPE;

                mGyroList.add(0, gyroData);
                if (mGyroList.size() > 30) {
                    mGyroList.remove(30);
                }
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                //Log.d("Test", "X:" + event.values[0] + ", Y:" + event.values[1] + ", Z:" + event.values[2]);

                SensorData magData = new SensorData();
                long currMagTime = System.currentTimeMillis();
                magData.time = currMagTime - mMageTime;
                mMageTime = currMagTime;

                magData.values[0] = event.values[0];
                magData.values[1] = event.values[1];
                magData.values[2] = event.values[2];
                magData.type = Sensor.TYPE_MAGNETIC_FIELD;
                mMageList.add(0, magData);
                if (mMageList.size() > 30) {
                    mMageList.remove(30);
                }
                break;

            case Sensor.TYPE_GRAVITY:
                Log.d("Test", "X:" + event.values[0] + ", Y:" + event.values[1] + ", Z:" + event.values[2]);
                SensorData gravity = new SensorData();
                long gravityTime = System.currentTimeMillis();
                gravity.time = gravityTime - mGravityTime;
                mGravityTime = gravityTime;

                gravity.values[0] = event.values[0];
                gravity.values[1] = event.values[1];
                gravity.values[2] = event.values[2];
                gravity.type = Sensor.TYPE_GRAVITY;
                mGravityList.add(0, gravity);
                if (mGravityList.size() > 30) {
                    mGravityList.remove(30);
                }
                break;

            case Sensor.TYPE_ORIENTATION:
                Log.d("Test", "X:" + event.values[0] + ", Y:" + event.values[1] + ", Z:" + event.values[2]);
                SensorData orientation = new SensorData();
                long orientationTime = System.currentTimeMillis();
                orientation.time = orientationTime - mOrientationTime;
                mOrientationTime = orientationTime;

                orientation.values[0] = event.values[0];
                orientation.values[1] = event.values[1];
                orientation.values[2] = event.values[2];
                orientation.type = Sensor.TYPE_ORIENTATION;
                mOrientationList.add(0, orientation);
                if (mOrientationList.size() > 30) {
                    mOrientationList.remove(30);
                }
                break;
            default:
                break;
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
