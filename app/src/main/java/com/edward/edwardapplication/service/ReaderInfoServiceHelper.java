package com.edward.edwardapplication.service;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.TimeoutException;

/**
 * Created by Edward.
 * Date: 2021/10/29
 * Time: 16:17
 * Desc:
 */
public class ReaderInfoServiceHelper {
    public static final String TAG = "ReaderInfoServiceHelper";
    private static ReaderInfoServiceHelper helper;

    private Context context;
    private ReaderInfoServiceConnection connection = null;
    private boolean unbindAfterExecute = false;

    private ReaderInfoServiceHelper(Context context) {
        this.context = context;
        connection = new ReaderInfoServiceConnection(context.getApplicationContext());
        ensureServiceBind();
    }

    public static void init(Context appContext) {
        if (helper == null) {
            helper = new ReaderInfoServiceHelper(appContext);
        }
    }

    public static ReaderInfoServiceHelper getInstance() {
        if (helper == null) {
            throw new IllegalStateException("Please init OnyxSdk first");
        }
        return helper;
    }

    public String getVendorId() {
        String vendorId = "";
        if (!ensureServiceBind()) {
            return vendorId;
        }
        try {
            vendorId = connection.getRemoteService().getVendorId();
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            unbindService(true);
        }
        return vendorId;
    }

    public boolean supportTdhDecryption() {
        boolean supportPdfAutomaticDecryption = false;
        if (!ensureServiceBind()) {
            return supportPdfAutomaticDecryption;
        }
        try {
            supportPdfAutomaticDecryption = connection.getRemoteService().supportTdhDecryption();
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            unbindService(true);
        }
        return supportPdfAutomaticDecryption;
    }

    public String test() {
        String content = "";
        if (!ensureServiceBind()) {
            return content;
        }
        try {
            content = connection.getRemoteService().test();
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            unbindService(true);
        }
        return content;
    }

    public String test11() {
        String content = "";
        if (!ensureServiceBind()) {
            return content;
        }
        try {
            content = connection.getRemoteService().test111();
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            unbindService(true);
        }
        return content;
    }

    public boolean ensureServiceBind() {
        if (connection.isConnected()) {
            return true;
        }
        Log.w(TAG, "Binding NeoService");
        bindNeoService();
        return false;
    }

    private void bindNeoService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connection.bindService();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).run();
    }

    private void unbindService() {
        unbindService(false);
    }

    private void unbindService(boolean afterExecute) {
        if (afterExecute && !unbindAfterExecute) {
            return;
        }
        if (connection != null
                && connection.isConnected()) {
            connection.unbindService();
        }
    }
}
