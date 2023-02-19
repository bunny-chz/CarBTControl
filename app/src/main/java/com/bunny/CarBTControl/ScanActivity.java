package com.bunny.CarBTControl;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bunny.CarBTControl.utils.VersionUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.wandersnail.bluetooth.BTManager;
import cn.wandersnail.bluetooth.DiscoveryListener;
import cn.wandersnail.commons.helper.PermissionsRequester2;
import cn.wandersnail.commons.util.ToastUtils;
import cn.wandersnail.widget.listview.BaseListAdapter;
import cn.wandersnail.widget.listview.BaseViewHolder;
import cn.wandersnail.widget.listview.PullRefreshLayout;

/**
 * Project:  51CarBlueToothControl
 * Comments:    ScanActivity 蓝牙扫描界面类
 * JDK version used: <JDK1.8>
 * Create Date：2023-01-08
 * Version: 1.0
 */
public class ScanActivity extends AppCompatActivity {
    private ListAdapter listAdapter;
    private TextView tvEmpty;
    private Button btnScan;
    private ImageView imgLoading;
    private final List<Device> devList = new ArrayList<>();
    private final String TAG = "CarBTControl TAG";
    VersionUtil versionUtil = new VersionUtil(this);
    private static class Device {
        BluetoothDevice device;
        int rssi;

        Device(BluetoothDevice device, int rssi) {
            this.device = device;
            this.rssi = rssi;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Device)) return false;
            Device device1 = (Device) o;
            return device.equals(device1.device);
        }

        @Override
        public int hashCode() {
            return Objects.hash(device);
        }
    }

    ActivityResultLauncher<Intent> enableBluetooth =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            //回调过来的结果代码
                            int resultCode = result.getResultCode();
                            if (resultCode == RESULT_OK){
                                if (isOpenBluetooth()){
                                    Toast.makeText(ScanActivity.this, "蓝牙已打开", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ScanActivity.this, "蓝牙没打开", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

    ActivityResultLauncher<String> requestBluetoothConnect = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result.equals(true)) {
                    enableBluetooth.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
                } else {
                    Toast.makeText(this, "本机版本是Android12，未获取此权限，则无法打开蓝牙。", Toast.LENGTH_SHORT).show();
                }
            });

    ActivityResultLauncher<String> requestLocation = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result.equals(false)) {
                    Toast.makeText(this, "请打开位置定位服务，以便于扫描蓝牙设备", Toast.LENGTH_SHORT).show();
                }
                if (result.equals(true)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("获取定位后需要重启软件");
                    builder.setPositiveButton("关闭应用去重启", (dialogInterface, i) -> {
                        finish();
                    });
                    builder.create().show();
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        openBluetooth();
        initViews();
        initPermission();
        BTManager.isDebugMode = true;
        BTManager.getInstance().addDiscoveryListener(discoveryListener);
        initialize();
    }

    private void initViews() {
        btnScan = findViewById(R.id.btnScan);
        imgLoading = findViewById(R.id.imgLoading);
        ListView lv = findViewById(R.id.lv);
        tvEmpty = findViewById(R.id.tvEmpty);

        listAdapter = new ListAdapter(this, devList);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(ScanActivity.this, MainActivity.class);
            intent.putExtra("device", devList.get(position).device);
            startActivity(intent);
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScaleAnimation animation = new ScaleAnimation(0.75f, 1.0f, 0.75f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(100);
                animation.setFillAfter(true);
                btnScan.startAnimation(animation);
                if (btnScan.getText().toString().equals("扫描")){
                    if(!versionUtil.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
                        requestLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                    else{
                        if (isOpenGps()){
                            Log.d(TAG,"************* isOpen GPS **************");
                            doStartDiscovery();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                            builder.setCancelable(false);
                            builder.setTitle("请打开位置定位服务，以便于扫描蓝牙设备");
                            builder.setPositiveButton("打开定位", (dialogInterface, i) -> {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                                Log.d(TAG,"Not Open GPS,to open it");
                            });
                            builder.setNegativeButton("取消", (dialogInterface, i) -> {
                                Toast.makeText(ScanActivity.this, "请打开位置定位服务,否则无法扫描到蓝牙设备", Toast.LENGTH_SHORT).show();
                            });
                            builder.create().show();
                        }
                    }
                } else if (btnScan.getText().toString().equals("停止扫描")){
                    if (BTManager.getInstance().isInitialized()) {
                        BTManager.getInstance().stopDiscovery();
                        Animation operatingAnim = AnimationUtils.loadAnimation(ScanActivity.this, R.anim.rotate);
                        operatingAnim.setInterpolator(new LinearInterpolator());
                        imgLoading.clearAnimation();
                        imgLoading.setVisibility(View.INVISIBLE);
                        btnScan.setText("扫描");
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BTManager.getInstance().release();
        Process.killProcess(Process.myPid());
    }

    private final DiscoveryListener discoveryListener = new DiscoveryListener() {
        @Override
        public void onDiscoveryStart() {

        }

        @Override
        public void onDiscoveryStop() {
            Animation operatingAnim = AnimationUtils.loadAnimation(ScanActivity.this, R.anim.rotate);
            operatingAnim.setInterpolator(new LinearInterpolator());
            imgLoading.clearAnimation();
            imgLoading.setVisibility(View.INVISIBLE);
            btnScan.setText("扫描");
        }

        @Override
        public void onDiscoveryError(int errorCode, @NonNull String errorMsg) {
            switch(errorCode) {
                case DiscoveryListener.ERROR_LACK_LOCATION_PERMISSION://缺少定位权限
                    break;
                case DiscoveryListener.ERROR_LOCATION_SERVICE_CLOSED://位置服务未开启
                    break;
                case DiscoveryListener.ERROR_LACK_SCAN_PERMISSION://缺少搜索权限
                    break;
                case DiscoveryListener.ERROR_SCAN_FAILED://搜索失败
                    ToastUtils.showShort("搜索出错：" + errorCode);
                    break;
            }
        }

        @Override
        public void onDeviceFound(@NonNull BluetoothDevice device, int rssi) {
            tvEmpty.setVisibility(View.INVISIBLE);
            Device dev = new Device(device, rssi);
            if (!devList.contains(dev)) {
                devList.add(dev);
                listAdapter.notifyDataSetChanged();
            }
        }
    };

    //需要进行检测的权限
    private List<String> getNeedPermissions() {
        List<String> list = new ArrayList<>();
        if (getApplicationInfo().targetSdkVersion >= 29) {//target sdk版本在29以上的需要精确定位权限才能搜索到蓝牙设备
            list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        //Android 12需要
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            list.add(Manifest.permission.BLUETOOTH_SCAN);
            list.add(Manifest.permission.BLUETOOTH_CONNECT);
        }
        return list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Main", "onResume");
        if (BTManager.getInstance().isInitialized()) {
            if (BTManager.getInstance().isBluetoothOn()) {
                doStartDiscovery();
            } else {
                openBluetooth();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (BTManager.getInstance().isInitialized()) {
            BTManager.getInstance().stopDiscovery();
            Animation operatingAnim = AnimationUtils.loadAnimation(ScanActivity.this, R.anim.rotate);
            operatingAnim.setInterpolator(new LinearInterpolator());
            imgLoading.clearAnimation();
            imgLoading.setVisibility(View.INVISIBLE);
        }
    }

    private void openBluetooth(){
        //蓝牙是否已打开
        if (isOpenBluetooth()) {
            Log.d(TAG,"************* isOpen Bluetooth **************");
            return ;
        }
        //是Android12
        if (versionUtil.isAndroid12()) {
            //检查是否有BLUETOOTH_CONNECT权限
            if (versionUtil.hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                //打开蓝牙
                enableBluetooth.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
                Log.d(TAG,"isAndroid12() ---> BluetoothAdapter.ACTION_REQUEST_ENABLE");
            } else {
                //请求权限
                requestBluetoothConnect.launch(Manifest.permission.BLUETOOTH_CONNECT);
                Log.d(TAG,"isAndroid12() ---> Manifest.permission.BLUETOOTH_CONNECT");
            }
            return ;
        }
        Log.d(TAG,"is NOT Android12() ---> BluetoothAdapter.ACTION_REQUEST_ENABLE");
        //不是Android12 直接打开蓝牙
        enableBluetooth.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
    }

    private boolean isOpenBluetooth(){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null){
            return false;
        }
        return adapter.isEnabled();
    }

    private boolean isOpenGps(){
        //从系统服务中获取定位管理器
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void initialize() {
        //动态申请权限
        PermissionsRequester2 permissionsRequester = new PermissionsRequester2(this);
        permissionsRequester.setCallback(list -> {

        });
        permissionsRequester.checkAndRequest(getNeedPermissions());
    }

    private void doStartDiscovery() {
         devList.clear();
         listAdapter.notifyDataSetChanged();
         tvEmpty.setVisibility(View.VISIBLE);
         BTManager.getInstance().startDiscovery();
         Animation operatingAnim = AnimationUtils.loadAnimation(ScanActivity.this, R.anim.rotate);
         operatingAnim.setInterpolator(new LinearInterpolator());
         imgLoading.startAnimation(operatingAnim);
         imgLoading.setVisibility(View.VISIBLE);
         btnScan.setText("停止扫描");
    }

    private static class ListAdapter extends BaseListAdapter<Device> {

        ListAdapter(@NotNull Context context, @NotNull List<Device> list) {
            super(context, list);
        }

        @NotNull
        @Override
        protected BaseViewHolder<Device> createViewHolder(int i) {
            return new BaseViewHolder<Device>() {
                TextView tvName;
                TextView tvAddr;
                ImageView imgRssi;

                @Override
                public void onBind(@NonNull Device device, int i) {
                    tvName.setText(TextUtils.isEmpty(device.device.getName()) ? "N/A" : device.device.getName());
                    tvAddr.setText(device.device.getAddress());
                    if (device.rssi <= 0 && device.rssi >= -50){
                        imgRssi.setImageResource(R.drawable.rssi_strong);
                    }else if(device.rssi < -50 && device.rssi >= -70){
                        imgRssi.setImageResource(R.drawable.rssi_middle);
                    }else if(device.rssi < -70){
                        imgRssi.setImageResource(R.drawable.rssi_weak);
                    }
                }

                @NotNull
                @Override
                public View createView() {
                    View view = View.inflate(context, R.layout.item_scan, null);
                    tvName = view.findViewById(R.id.tvName);
                    tvAddr = view.findViewById(R.id.tvAddr);
                    imgRssi = view.findViewById(R.id.imgRssi);
                    return view;
                }
            };
        }
    }
    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.BLUETOOTH,Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_ADVERTISE,Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.BLUETOOTH_PRIVILEGED};
        ArrayList<String> toApplyList = new ArrayList<>();
        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);// 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 0x01);
        }
    }
}
