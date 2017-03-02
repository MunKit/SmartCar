package com.example.munkit.smartcar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.NotificationCompat;
import android.view.WindowManager;
import android.content.Intent;
import android.widget.SeekBar;
import android.widget.Toast;

public class monitoring extends AppCompatActivity{
    NotificationCompat.Builder notification1;
    private static final int uniqueID1 = 145;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitoring);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        notification1 = new NotificationCompat.Builder(this);
        notification1.setAutoCancel(true);
        SeekBar simpleSeekBar=(SeekBar)findViewById(R.id.seekBar9);
        // perform seek bar change listener event used for getting the progress value
        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(monitoring.this, "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
                if (progressChangedValue<2)
                {
                    //pop notification
                    notification1.setSmallIcon(R.mipmap.ic_launcher);
                    notification1.setTicker("Engine Oil");
                    notification1.setWhen(System.currentTimeMillis());
                    notification1.setContentTitle("SmartCar");
                    notification1.setContentText("Engine Oil is low");
                    Intent intet = new Intent(monitoring.this,monitoring.class);
                    //PendingIntent pending = PendingIntent.getActivities(monitoring.this,0, new Intent[]{intet},PendingIntent.FLAG_UPDATE_CURRENT);
                    PendingIntent pending = PendingIntent.getActivities(monitoring.this,0, new Intent[]{intet},PendingIntent.FLAG_UPDATE_CURRENT);
                    notification1.setContentIntent(pending);

                    // builds notification and issues it
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(uniqueID1,notification1.build());
                }
            }
        });
    }
}
