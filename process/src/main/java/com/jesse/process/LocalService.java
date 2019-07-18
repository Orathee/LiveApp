package com.jesse.process;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

public class LocalService extends Service {

    private static final String TAG = "ProcessService";
    private ServiceConnection serviceConnection;
    private MyBinder myBinder;

    class MyBinder extends IMyAidlInterface.Stub {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myBinder = new MyBinder();
        serviceConnection = new MyServiceConnection();

        // 让服务变成前台服务
        startForeground(16, new Notification());
        // 如果18以上的设备 使用相同id再次启动一个前台服务 然后结束这个服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startService(new Intent(this, InnerService.class));
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, RemoteService.class),
                serviceConnection, BIND_AUTO_CREATE);
        return super.onStartCommand(intent, flags, startId);
    }

    class MyServiceConnection implements ServiceConnection {

        // 服务连接后回调
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        // 连接中断后回调
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "RemoteService 可能被杀死了，拉活");

            startService(new Intent(LocalService.this, RemoteService.class));
            bindService(new Intent(LocalService.this, RemoteService.class),
                    serviceConnection, BIND_AUTO_CREATE);
        }
    }

    public static class InnerService extends Service {

        @Override
        public void onCreate() {
            super.onCreate();
            startForeground(16, new Notification());
            stopSelf();
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
