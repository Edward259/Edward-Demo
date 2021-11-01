package com.edward.edwardapplication.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.onyx.kreader.IReaderInfoService;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Edward.
 * Date: 2021/10/29
 * Time: 16:18
 * Desc:
 */
public class ReaderInfoServiceConnection implements ServiceConnection {
    public static final String TAG = "ReaderInfoService";
    private static final String READER_INFO_SERVICE_PACKET_NAME = "com.onyx.kreader";
    private static final String READER_INFO_SERVICE_NAME = "com.onyx.kreader.courtachive.service.ReaderInfoService";
    private volatile IReaderInfoService remoteService;
    private Context appContext;
    private static final int WAIT_SLEEP_TIME = 100;
    private volatile boolean connected = false;
    private int connectTimeoutMs = 5000;
    private volatile AtomicBoolean abort = new AtomicBoolean(false);

    public ReaderInfoServiceConnection(Context context) {
        super();
        appContext = context.getApplicationContext();
    }

    public ReaderInfoServiceConnection bindService() throws TimeoutException, InterruptedException {
        final Intent service = new Intent();
        service.setComponent(new ComponentName(READER_INFO_SERVICE_PACKET_NAME, READER_INFO_SERVICE_NAME));
        appContext.bindService(service, this, Context.BIND_AUTO_CREATE);
        waitUntilConnected();
        return this;
    }

    public ReaderInfoServiceConnection unbindService() {
        appContext.unbindService(this);
        return this;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binding) {
        Log.i(TAG, "onServiceConnected: ");
        remoteService = IReaderInfoService.Stub.asInterface(binding);
        connected = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.i(TAG, "onServiceDisconnected --" + name);
        connected = false;
        remoteService = null;
    }

    public boolean isConnected() {
        return connected;
    }

    public IReaderInfoService getRemoteService() {
        return remoteService;
    }

    public ReaderInfoServiceConnection setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
        return this;
    }

    public boolean isAbort() {
        return abort.get();
    }

    public void setAbort() {
        this.abort.set(true);
    }

    public void waitUntilConnected() throws InterruptedException, TimeoutException {
        int connectTime = 0;
        while (!connected && !isAbort()) {
            Thread.sleep(WAIT_SLEEP_TIME);
            connectTime += WAIT_SLEEP_TIME;
            if (connectTime >= connectTimeoutMs) {
                throw new TimeoutException("connect neo service timeout");
            }
        }
    }
}
