package com.kishynskaya.plugin.firstplugin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class ServicePlugin extends android.app.Service {

    int idNotification = 101;
    public static final String ACTION_STOP = "ACTION_STOP";

    Handler handler = new Handler();
    Runnable runnable;
    int i = 0;
    final String ENABLED_APP = "ENABLED_APP";
    ApplicationIsEnableReceiver applicationIsEnableReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationIsEnableReceiver = new ApplicationIsEnableReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ENABLED_APP);
        registerReceiver(applicationIsEnableReceiver, intentFilter);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = "first service plugin";
        NotificationChannel channel = new NotificationChannel(channelId, "Plugin service", NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(channel);
        Notification notification = new Notification.Builder(getApplicationContext(), channelId)
                .setContentTitle(BuildConfig.VERSION_NAME)
                .setContentText(getApplicationContext().getString(R.string.app_name))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();
        startForeground(idNotification, notification);
        start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            if (intent.getAction() != null) {
                String action = intent.getAction();
                if (ACTION_STOP.equals(action)) {
                    stop();
                }
            }
        }
        return START_NOT_STICKY;
    }

    private void start() {
        runnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ServicePlugin.this, "First plugin" + "#" + ++i, Toast.LENGTH_LONG).show();
                handler.postDelayed(this, 7000);
            }
        };
        runnable.run();
    }

    private void stop() {
        runnable = null;
        handler.removeCallbacksAndMessages(null);
        unregisterReceiver(applicationIsEnableReceiver);
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class ApplicationIsEnableReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == null) return;
            if (ENABLED_APP.equals(intent.getAction())) {
                Intent enabledStates = new Intent();
                enabledStates.putExtra("packageName", getPackageName());
                enabledStates.putExtra("ENABLE", true);
                enabledStates.setAction("DETECT_ENABLING_APPS");
                sendBroadcast(enabledStates);
            }
        }
    }
}
