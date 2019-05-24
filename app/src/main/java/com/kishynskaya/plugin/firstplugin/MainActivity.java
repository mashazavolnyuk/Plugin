package com.kishynskaya.plugin.firstplugin;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            boolean enable = bundle.getBoolean("ENABLE");
            if (!enable) {
                Intent service = new Intent(this, ServicePlugin.class);
                service.setAction(ServicePlugin.ACTION_STOP);
                startService(service);
                finish();
            } else {
                startTask();
            }
        }
        finish();
    }

    private void startTask() {
        Intent service = new Intent(this, ServicePlugin.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(service);
        } else {
            startService(service);
        }
    }
}
