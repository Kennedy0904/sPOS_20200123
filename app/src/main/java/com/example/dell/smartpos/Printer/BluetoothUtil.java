package com.example.dell.smartpos.Printer;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.dell.smartpos.R;
import com.example.dell.smartpos.Constants;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class BluetoothUtil {

    private Context context = null;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ArrayList<BluetoothDevice> bondDevices = null;// 用于存放已配对蓝牙设备
    private Button searchDevices = null;
    private TextView selection = null;
    private ListView bondDevicesListView = null;
    private String printeraddress = "";
    private String printername = "";


    public BluetoothUtil(Context context, ListView bondDevicesListView, Button searchDevices, TextView selection) {
        this.context = context;
        this.bondDevicesListView = bondDevicesListView;
        this.bondDevices = new ArrayList<BluetoothDevice>();
        this.selection = selection;
        this.searchDevices = searchDevices;
        this.initIntentFilter();
        this.initbondlist();
    }

    private void initbondlist() {

        SharedPreferences pref = context.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        printeraddress = pref.getString(Constants.pref_printer_address, "");
        printername = pref.getString(Constants.pref_printer_name, "");
        if ("".equals(printeraddress) || printeraddress == null) {
            printeraddress = "";
        }
        if ("".equals(printername) || printername == null) {
            printername = "";
        }
        Log.d("OTTO","SETTEXT>>"+printername+":"+printeraddress);
        selection.setText(printername);
    }
    /**
     * 添加已绑定蓝牙设备到ListView
     */
    private void addBondDevicesToListView() {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        int count = this.bondDevices.size();
        Log.d("OTTO","已绑定设备数量：" + count);
        for (int i = 0; i < count; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("deviceName", this.bondDevices.get(i).getName());
            data.add(map);// 把item项的数据加到data中
        }
        String[] from = { "deviceName" };
        int[] to = { R.id.device_name };
        SimpleAdapter simpleAdapter = new SimpleAdapter(this.context, data,R.layout.bonddevice_item, from, to);
        // 把适配器装载到listView中
        this.bondDevicesListView.setAdapter(simpleAdapter);

        this.bondDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                BluetoothDevice device = bondDevices.get(arg2);
                SharedPreferences pref = context.getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString(Constants.pref_printer_address,device.getAddress());
                edit.putString(Constants.pref_printer_name,device.getName());
                edit.commit();
                printername = device.getName();
                printeraddress = device.getAddress();
                Log.d("OTTO","SETTEXT>>"+printername+":"+printeraddress);
                selection.setText(printername);
            }
        });

    }

    public void initIntentFilter() {
        // 设置广播信息过滤
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果
        context.registerReceiver(receiver, intentFilter);
    }

    /**
     * 判断蓝牙是否打开
     *
     * @return boolean
     */
    public boolean isOpen() {
        return this.bluetoothAdapter.isEnabled();
    }

    /**
     * 搜索蓝牙设备
     */
    public void searchDevices() {
        this.bondDevices.clear();
        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
        this.bluetoothAdapter.startDiscovery();
    }

    /**
     * 添加已绑定蓝牙设备到list集合
     *
     */
    public void addBandDevices(BluetoothDevice device) {
        Log.d("OTTO","已绑定设备名称：" + device.getName());
        if (!this.bondDevices.contains(device)) {
            this.bondDevices.add(device);
        }
    }

    /**
     * 蓝牙广播接收器
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        ProgressDialog progressDialog = null;

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    addBandDevices(device);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                progressDialog = ProgressDialog.show(context,
                        context.getString(R.string.please_wait_2),
                        context.getString(R.string.searching_devices),
                        true,
                        false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(Constants.BLUETOOTH_SEARCH_TIME);
                            bluetoothAdapter.cancelDiscovery();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d("OTTO","设备搜索完毕");
                progressDialog.dismiss();

                addBondDevicesToListView();
                Log.d("OTTO","BroadcastReceiver5");
            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    Log.d("OTTO","--------打开蓝牙-----------");
                    bondDevicesListView.setEnabled(true);
                } else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                    Log.d("OTTO","--------关闭蓝牙-----------");
                    bondDevicesListView.setEnabled(false);
                }
            }
        }
    };

}
