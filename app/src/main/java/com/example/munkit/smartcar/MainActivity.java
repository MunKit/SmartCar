package com.example.munkit.smartcar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private MqttAndroidClient mqttAndroidClient;
    private final String serverUri = "tcp://iot.eclipse.org:1883";
    private final String clientId = "CarClient";
    private static final String TAG = "Mymessage";
    private final String pubchannel = "Car/message";
    private final String subchannel = "Car/respond";

    AlarmManager alarm_manager;
    //ToggleButton alarmswitch;
    Context context;
    PendingIntent pending_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.context = this;

        //setup alarm service
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize alarm button
        //alarmswitch = (ToggleButton) findViewById(R.id.Alarmswitch);

        //Create Intent for alarm
        final Intent alarm_intent = new Intent(this.context, receivealarm.class);

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    Log.i(TAG,"Reconnected to : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic(subchannel);
                } else {
                    Log.i(TAG,"Connected to: " + serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.i(TAG,"The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String mess = new String(message.getPayload());
                Log.i(TAG,"Incoming message: " + mess);
                if (mess.equals("OnLamp"))
                {
                    ToggleButton lamp = (ToggleButton) findViewById(R.id.Lampswitch);
                    lamp.setChecked(true);
                }
                else if(mess.equals("OffLamp"))
                {
                    ToggleButton lamp = (ToggleButton) findViewById(R.id.Lampswitch);
                    lamp.setChecked(false);
                }
                else if(mess.equals("OffDoor"))
                {
                    ToggleButton door = (ToggleButton) findViewById(R.id.Doorswitch);
                    door.setChecked(false);
                }
                else if(mess.equals("OnDoor"))
                {
                    ToggleButton door = (ToggleButton) findViewById(R.id.Doorswitch);
                    door.setChecked(true);
                }
                else if(mess.equals("OffAlarm"))
                {
                    ToggleButton alarm = (ToggleButton) findViewById(R.id.Alarmswitch);
                    alarm.setChecked(false);
                }
                else if(mess.equals("OnAlarm"))
                {
                    ToggleButton alarm = (ToggleButton) findViewById(R.id.Alarmswitch);
                    alarm.setChecked(true);
                }
                else if(mess.equals("OffAircon"))
                {
                    ToggleButton aircon = (ToggleButton) findViewById(R.id.Airconswitch);
                    aircon.setChecked(false);
                }
                else if(mess.equals("OnAircon"))
                {
                    ToggleButton aircon = (ToggleButton) findViewById(R.id.Airconswitch);
                    aircon.setChecked(true);
                }
                else
                {

                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic(subchannel);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG,"Failed to connect to: " + serverUri);
                }
            });


        } catch (MqttException ex){
            ex.printStackTrace();
        }
        //switch lamp
        ToggleButton lamp = (ToggleButton) findViewById(R.id.Lampswitch);
        lamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    publishMessage(pubchannel,"OnLamp");
                } else {
                    publishMessage(pubchannel,"OffLamp");
                    // The toggle is disabled
                }
            }
        });
        //switch lamp
        ToggleButton door = (ToggleButton) findViewById(R.id.Doorswitch);
        door.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    publishMessage(pubchannel,"OnDoor");
                } else {
                    publishMessage(pubchannel,"OffDoor");
                    // The toggle is disabled
                }
            }
        });
        //switch lamp
        ToggleButton alarm = (ToggleButton) findViewById(R.id.Alarmswitch);
        alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    publishMessage(pubchannel,"OnAlarm");
                    //Log.e("Alarm","Alarm");
                    alarm_intent.putExtra("extra", "alarm on");
                    sendBroadcast(alarm_intent);
                    pending_intent = PendingIntent.getBroadcast(MainActivity.this, 1, alarm_intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    alarm_manager.set(AlarmManager.RTC_WAKEUP, 0,pending_intent);

                } else {
                    publishMessage(pubchannel,"OffAlarm");
                    //Log.e("Alarm1","Alarm1");
                    alarm_manager.cancel(pending_intent);
                    alarm_intent.putExtra("extra", "alarm off");
                    sendBroadcast(alarm_intent);
                    // The toggle is disabled
                }
            }
        });
        //switch lamp
        ToggleButton aircon = (ToggleButton) findViewById(R.id.Airconswitch);
        aircon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    publishMessage(pubchannel,"OnAircon");
                } else {
                    publishMessage(pubchannel,"OffAircon");
                    // The toggle is disabled
                }
            }
        });

    }
    public void subscribeToTopic(String subscriptionTopic){
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG,"Subscribed!");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG,"Failed to subscribe");
                }
            });

        } catch (MqttException ex){
            Log.i(TAG,"Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    public void publishMessage(String Channel, String pubmessage){

        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(pubmessage.getBytes());
            mqttAndroidClient.publish(Channel, message);
            if(!mqttAndroidClient.isConnected()){
                Log.i(TAG,mqttAndroidClient.getBufferedMessageCount() + " messages in buffer.");
            }
        } catch (MqttException e) {
            Log.i(TAG,"Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
