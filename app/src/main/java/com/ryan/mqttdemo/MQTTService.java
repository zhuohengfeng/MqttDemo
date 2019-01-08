package com.ryan.mqttdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

/**
 * @author Dominik Obermaier
 */
public class MQTTService extends Service {

    //public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";
    public static final String BROKER_URL = "tcp://118.126.107.250:1883";

    /* In a real application, you should get an Unique Client ID of the device and use this, see
    http://android-developers.blogspot.de/2011/03/identifying-app-installations.html */
    public static final String clientId = "android-client";

    public static final String TOPIC = "de/eclipsemagazin/blackice/warnings";
    private MqttClient mqttClient;


    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mqttClient = new MqttClient(BROKER_URL, clientId, new MemoryPersistence());

                    mqttClient.setCallback(new PushCallback(MQTTService.this));
                    mqttClient.connect();

                    //Subscribe to all subtopics of homeautomation
                    mqttClient.subscribe(TOPIC);
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }).start();

        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        try {
            mqttClient.disconnect(0);
        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
