package com.spyro_soft.aidl;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.os.Handler;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;
import android.util.Log;

public class ServiceBinder implements ServiceConnection {

    private Context mContext;
    private long mController;
    private String mIntentName;
    private AtomicBoolean mIsConnected;

    public native void onServiceConnected(IBinder binder, long controllerPtr);
    public native void onServiceDisconnected(long controllerPtr);

    public ServiceBinder(Context mainContext, long controllerPtr, String intentName) {
        mContext = mainContext;
        mController = controllerPtr;
        mIntentName = intentName;
        mIsConnected = new AtomicBoolean(false);

        bindToService();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder)
    {
        Log.i(mIntentName, "Service connected");
        mIsConnected.set(true);
		onServiceConnected(iBinder, mController);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName)
    {
        Log.i(mIntentName, "Service disconnected");
        mIsConnected.set(false);
		onServiceDisconnected(mController);
    }

    @Override
    public void onBindingDied(ComponentName name)
    {
        showToast("Binding died");
        Log.i(mIntentName, "Binding died");
        mIsConnected.set(false);
        mContext.unbindService(this);
    }

    @Override
    public void onNullBinding(ComponentName name)
    {
        showToast("Null service binding");
        Log.i(mIntentName, "Null service binding");
        mIsConnected.set(false);
    }

    public boolean isConnected() {
        return mIsConnected.get();
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void bindToService() {
        if (!mIsConnected.get()) {
            Intent explicitIntent = convertImplicitIntentToExplicitIntent(new Intent(mIntentName));
            if (explicitIntent != null) {
                Log.i(mIntentName, "Connecting to service");
                mContext.bindService(explicitIntent, this, Context.BIND_AUTO_CREATE);
            } else {
                Log.i(mIntentName, "Service connection failed");
                showToast("Service connection failed");
            }
        }
    }

    private Intent convertImplicitIntentToExplicitIntent(Intent implicitIntent) {
        PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentServices(implicitIntent, 0);

        if (resolveInfoList == null || resolveInfoList.size() != 1) {
            Log.i(mIntentName, "Service connection failed" + implicitIntent.toString() + " " + (resolveInfoList == null) + " " + resolveInfoList.size() );
            return null;
        }

        ResolveInfo serviceInfo = resolveInfoList.get(0);
        ComponentName component = new ComponentName(serviceInfo.serviceInfo.packageName,
                                                    serviceInfo.serviceInfo.name);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }
}
