package com.edward.edwardapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edward.edwardapplication.databinding.ActivityWifiDirectBinding;
import com.onyx.android.sdk.api.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("MissingPermission")
public class WifiDirectActivity extends AppCompatActivity {

    private static final int ACCESS_FINE_LOCATION_CODE = 101;
    public static final String EDWARD_WPA = "edward_wpa";
    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mChannel;
    private ActivityWifiDirectBinding binding;
    private BroadcastReceiver mReceiver;
    private List<WifiP2pDevice> wifiP2pDevices = new ArrayList<>();
    private boolean stopDiscover = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wifi_direct);
        initManager();
        register();
        if (checkPermission()) {
            return;
        }
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregister();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_CODE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initView();
                }
                break;
        }
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_CODE);
            return true;
        }
        return false;
    }

    private void initView() {
        binding.discover.setOnClickListener(v -> discover(true));
        binding.stop.setOnClickListener(v -> stop());
    }

    private void stop() {
        stopDiscover = true;
        mWifiP2pManager.stopPeerDiscovery(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.e(EDWARD_WPA, "stopPeerDiscovery onSuccess: ");
            }

            @Override
            public void onFailure(int reason) {
                Log.e(EDWARD_WPA, "stopPeerDiscovery onFailure: " + reason);
            }
        });
    }

    private void discover(boolean firstStart) {
        if (!firstStart && stopDiscover) {
            return;
        }
        mWifiP2pManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                stopDiscover = true;
                Log.e(EDWARD_WPA, "discoverPeers onSuccess: ");
            }

            @Override
            public void onFailure(int reason) {
                stopDiscover = false;
                discover(false);
                Log.e(EDWARD_WPA, "discoverPeers onFailure: " + reason);
            }
        });
    }

    private void initManager() {
        mWifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mWifiP2pManager.initialize(this, getMainLooper(), new WifiP2pManager.ChannelListener() {
            @Override
            public void onChannelDisconnected() {

            }
        });
    }

    public void register() {
        IntentFilter intentFilter = new IntentFilter();
//监听 Wi-Fi P2P是否开启
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//监听 Wi-Fi P2P扫描状态
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
//监听 可用的P2P列表发生了改变。
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//监听 Wi-Fi P2P的连接状态发生了改变
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//监听 设备的详细配置发生了变化
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                        int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                        if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                            Log.e(EDWARD_WPA, "onReceive: WIFI_P2P_STATE_CHANGED_ACTION: enable");
                            // Wifi Direct is enabled
                        } else {
                            // Wi-Fi Direct is not enabled
                        }
                        break;
                    case WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION:
                        Log.e(EDWARD_WPA, "onReceive: WIFI_P2P_DISCOVERY_CHANGED_ACTION: disenable");
                        break;
                    case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                        if (mWifiP2pManager != null) {
                            mWifiP2pManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                                @Override
                                public void onPeersAvailable(WifiP2pDeviceList peers) {
                                    WifiP2pDevice[] objects = peers.getDeviceList().toArray(new WifiP2pDevice[]{});
                                    List<WifiP2pDevice> deviceList = Arrays.asList(objects);
                                    for (WifiP2pDevice wifiP2pDevice : deviceList) {
                                        Log.e(EDWARD_WPA, "onPeersAvailable: " + wifiP2pDevice.toString());
                                    }
                                    if (!CollectionUtils.isNullOrEmpty(deviceList)) {
                                        wifiP2pDevices = deviceList;
                                    }
                                    binding.deviceList.setAdapter(new BaseAdapter() {
                                        @Override
                                        public int getCount() {
                                            return wifiP2pDevices.size();
                                        }

                                        @Override
                                        public WifiP2pDevice getItem(int position) {
                                            return wifiP2pDevices.get(position);
                                        }

                                        @Override
                                        public long getItemId(int position) {
                                            return 0;
                                        }

                                        @Override
                                        public View getView(int position, View convertView, ViewGroup parent) {
                                            TextView textView = new TextView(context);
                                            textView.setText(getItem(position).deviceName);
                                            textView.setTextSize(25);
                                            textView.setOnClickListener(v -> connect(getItem(position)));
                                            return textView;
                                        }
                                    });
                                }
                            });
                        }
                        break;
                    case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                        NetworkInfo networkInfo = (NetworkInfo) intent
                                .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                        if (networkInfo.isConnected()) {
                            // we are connected with the other device, request connection
                            // info to find group owner IP
                            mWifiP2pManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
                                @Override
                                public void onConnectionInfoAvailable(WifiP2pInfo info) {
                                    Log.e(EDWARD_WPA, "onConnectionInfoAvailable: " + info.toString());
                                    if (info.groupFormed && info.isGroupOwner) {
                                        //server 创建ServerSocket
                                    } else if (info.groupFormed) {
                                        // The other device acts as the client. In this case, we enable the
                                        // get file button.
                                        //连接Server。
                                    }
                                }
                            });
                        } else {
                            Log.e(EDWARD_WPA, "onReceive: disconnect");
                            // It's a disconnect
                        }
                        break;
                    case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                        Log.e(EDWARD_WPA, "onReceive: WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
                        break;
                }
            }
        };
        registerReceiver(mReceiver, intentFilter);
    }

    public void unregister() {
        unregisterReceiver(mReceiver);
    }

    public void connect(WifiP2pDevice wifiP2pDevice) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wifiP2pDevice.deviceAddress;
        mWifiP2pManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Log.e(EDWARD_WPA, "onSuccess: connect" + wifiP2pDevice.toString());
            }

            @Override
            public void onFailure(int reason) {
                Log.e(EDWARD_WPA, "onFailure: connect" + wifiP2pDevice.toString() + "  " + reason);
            }
        });
    }
}