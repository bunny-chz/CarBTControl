package com.bunny.CarBTControl;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import cn.wandersnail.bluetooth.BTManager;
import cn.wandersnail.bluetooth.ConnectCallback;
import cn.wandersnail.bluetooth.Connection;
import cn.wandersnail.bluetooth.EventObserver;
import cn.wandersnail.bluetooth.UUIDWrapper;
import cn.wandersnail.bluetooth.WriteCallback;

/**
 * Project:  51CarBlueToothControl
 * Comments:    MainActivity 控制小车操作界面类
 * JDK version used: <JDK1.8>
 * Create Date：2023-01-08
 * Version: 1.0
 */
public class MainActivity extends AppCompatActivity implements EventObserver {
    private Connection connection;
    private TextView tvLog;
    private final String TAG = "CarBTControl TAG";
    private Button btnCtrl1,btnCtrl2,btnCtrl3,btnCtrl4,btnCtrl5,
            btnCtrl6,btnCtrl7,btnCtrl8,btnCtrl9,btnSendHex,btnBtLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLink();
        initView();
    }

    private void initView(){
        EditText etMsg = findViewById(R.id.etMsg);
        tvLog = findViewById(R.id.tvLog);
        btnCtrl1 = findViewById(R.id.btnCtrl1);
        btnCtrl2 = findViewById(R.id.btnCtrl2);
        btnCtrl3 = findViewById(R.id.btnCtrl3);
        btnCtrl4 = findViewById(R.id.btnCtrl4);
        btnCtrl5 = findViewById(R.id.btnCtrl5);
        btnCtrl6 = findViewById(R.id.btnCtrl6);
        btnCtrl7 = findViewById(R.id.btnCtrl7);
        btnCtrl8 = findViewById(R.id.btnCtrl8);
        btnCtrl9 = findViewById(R.id.btnCtrl9);
        btnSendHex = findViewById(R.id.btnSendHex);
        btnBtLink = findViewById(R.id.btnBtLink);
        btnSendHex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaleAnimation animation = new ScaleAnimation(0.75f, 1.0f, 0.75f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(100);
                animation.setFillAfter(true);
                btnSendHex.startAnimation(animation);
                if (connection.isConnected()) {
                    if (!etMsg.getText().toString().isEmpty()) {
                        String str = etMsg.getText().toString();
                        Log.d(TAG,"******************************** " + str + " *******************");
                        int strInt = Integer.parseInt(str);
                        connection.write(null, intToBytes(strInt), new WriteCallback() {
                            @Override
                            public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                            }
                        });
                    }
                }
            }
        });
        btnBtLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaleAnimation animation = new ScaleAnimation(0.75f, 1.0f, 0.75f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(100);
                animation.setFillAfter(true);
                btnBtLink.startAnimation(animation);
                connection.disconnect();
                finish();
            }
        });
        btnCtrl1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (connection.isConnected()) {
                    ScaleAnimation animation = new ScaleAnimation(0.75f, 1.0f, 0.75f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(100);
                    animation.setFillAfter(true);
                    btnCtrl1.startAnimation(animation);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            connection.write(null, intToBytes(3), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                        case MotionEvent.ACTION_UP:
                            connection.write(null, intToBytes(5), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                    }
                }
                return false;
            }
        });
        btnCtrl2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (connection.isConnected()) {
                    ScaleAnimation animation = new ScaleAnimation(0.75f, 1.0f, 0.75f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(100);
                    animation.setFillAfter(true);
                    btnCtrl2.startAnimation(animation);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            connection.write(null, intToBytes(1), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                        case MotionEvent.ACTION_UP:
                            connection.write(null, intToBytes(5), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                    }
                }
                return false;
            }
        });
        btnCtrl3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (connection.isConnected()) {
                    ScaleAnimation animation = new ScaleAnimation(0.75f, 1.0f, 0.75f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(100);
                    animation.setFillAfter(true);
                    btnCtrl3.startAnimation(animation);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            connection.write(null, intToBytes(4), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                        case MotionEvent.ACTION_UP:
                            connection.write(null, intToBytes(5), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                    }
                }
                return false;
            }
        });
        btnCtrl4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (connection.isConnected()) {
                    ScaleAnimation animation = new ScaleAnimation(0.75f, 1.0f, 0.75f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(100);
                    animation.setFillAfter(true);
                    btnCtrl4.startAnimation(animation);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            connection.write(null, intToBytes(8), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                        case MotionEvent.ACTION_UP:
                            connection.write(null, intToBytes(5), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                    }
                }
                return false;
            }
        });
        btnCtrl5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (connection.isConnected()) {
                    ScaleAnimation animation = new ScaleAnimation(0.75f, 1.0f, 0.75f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(100);
                    animation.setFillAfter(true);
                    btnCtrl5.startAnimation(animation);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            connection.write(null, intToBytes(5), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                        case MotionEvent.ACTION_UP:
                            connection.write(null, intToBytes(5), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                    }
                }
                return false;
            }
        });
        btnCtrl6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (connection.isConnected()) {
                    ScaleAnimation animation = new ScaleAnimation(0.75f, 1.0f, 0.75f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(100);
                    animation.setFillAfter(true);
                    btnCtrl6.startAnimation(animation);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            connection.write(null, intToBytes(9), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                        case MotionEvent.ACTION_UP:
                            connection.write(null, intToBytes(5), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                    }
                }
                return false;
            }
        });
        btnCtrl7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (connection.isConnected()) {
                    ScaleAnimation animation = new ScaleAnimation(0.75f, 1.0f, 0.75f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(100);
                    animation.setFillAfter(true);
                    btnCtrl7.startAnimation(animation);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            connection.write(null, intToBytes(6), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                        case MotionEvent.ACTION_UP:
                            connection.write(null, intToBytes(5), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                    }
                }
                return false;
            }
        });
        btnCtrl8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (connection.isConnected()) {
                    ScaleAnimation animation = new ScaleAnimation(0.75f, 1.0f, 0.75f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(100);
                    animation.setFillAfter(true);
                    btnCtrl8.startAnimation(animation);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            connection.write(null, intToBytes(2), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                        case MotionEvent.ACTION_UP:
                            connection.write(null, intToBytes(5), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                    }
                }
                return false;
            }
        });
        btnCtrl9.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (connection.isConnected()) {
                    ScaleAnimation animation = new ScaleAnimation(0.75f, 1.0f, 0.75f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(100);
                    animation.setFillAfter(true);
                    btnCtrl9.startAnimation(animation);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            connection.write(null, intToBytes(7), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                        case MotionEvent.ACTION_UP:
                            connection.write(null, intToBytes(5), new WriteCallback() {
                                @Override
                                public void onWrite(@NonNull BluetoothDevice device, @NonNull String tag, @NonNull byte[] value, boolean result) {

                                }
                            });
                            break;
                    }
                }
                return false;
            }
        });
    }

    private void initLink(){
        BluetoothDevice device = getIntent().getParcelableExtra("device");
        connection = BTManager.getInstance().createConnection(device, UUIDWrapper.useDefault(), this);
        if (connection == null) {
            finish();
            return;
        }
        connection.connect(new ConnectCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvLog.append("连接成功\n");
                        tvLog.setBackgroundColor(getResources().getColor(R.color.link_on));
                    }
                });

            }

            @Override
            public void onFail(@NonNull String errMsg, @Nullable Throwable e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvLog.append("连接失败,请重新连接！\n");
                        tvLog.setBackgroundColor(getResources().getColor(R.color.link_off));
                    }
                });
            }
        });
    }

    public byte[] intToBytes(int value ) {
        byte[] src = new byte[4];
        src[0] =  (byte) (value & 0xFF);
        src[1] =  (byte) ((value>>8) & 0xFF);
        src[2] =  (byte) ((value>>16) & 0xFF);
        src[3] =  (byte) ((value>>24) & 0xFF);
        return src;
    }
}
