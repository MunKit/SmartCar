package com.example.munkit.smartcar;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.security.Provider;


public class RingtonePlayingService extends Service{

    MediaPlayer media_song;
    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //Log.i("LocalService", "Received start id" + startId + ": " + intent) ;

        String state = intent.getExtras().getString("extra");

        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                //Log.e("Alarm","ON!");
                break;
            case "alarm off":
                startId = 0;
                //Log.e("Alarm","OFF!");
                break;
            default:
                startId = 0;
                //Log.e("Alarm","OFF!");
                break;
        }

        if (!this.isRunning && startId ==1 ){
            media_song = MediaPlayer.create(this, R.raw.best_wake_up_sound);
            media_song.start();
            this.isRunning = true;
            this.startId = 0;
        }
        else if(this.isRunning && startId == 0){
            media_song.stop();
            media_song.reset();
            this.isRunning = false;
            this.startId = 0;
        }
        else if(!this.isRunning && startId == 0){
            this.isRunning = false;
            this.startId = 0;
        }
        else if (this.isRunning && startId == 1){
            this.isRunning = true;
            this.startId = 1;
        }
        else {

        }






        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        Toast.makeText(this,"Destroy", Toast.LENGTH_SHORT).show();
    }
}
