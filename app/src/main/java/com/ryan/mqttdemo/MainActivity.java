package com.ryan.mqttdemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final String SERVICE_CLASSNAME = "com.ryan.mqttdemo.MQTTService";
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button1);
        updateButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateButton();
    }

    private void updateButton() {
        if (serviceIsRunning()) {
            button.setText("Stop Service");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    button.setText("Start Service");
                    stopBlackIceService();
                    updateButton();
                }
            });

        } else {
            button.setText("Start Service");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    button.setText("Stop Service");
                    startBlackIceService();
                    updateButton();
                }
            });
        }
    }

    private void startBlackIceService() {

        final Intent intent = new Intent(this, MQTTService.class);
        startService(intent);
    }

    private void stopBlackIceService() {

        final Intent intent = new Intent(this, MQTTService.class);
        stopService(intent);
    }

    private boolean serviceIsRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SERVICE_CLASSNAME.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
