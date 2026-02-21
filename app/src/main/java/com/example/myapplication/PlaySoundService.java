package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import java.io.IOException;

public class PlaySoundService extends Service {
    private MediaPlayer mMediaplayer;
    private String mp3_filepath = "musicbox-kyuuden-shirabe.mp3";

    @Override
    public void onCreate() {
        Log.i("PlaySoundService", "call onDestroy()");
        mMediaplayer = new MediaPlayer();
        setup_sound_data();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("PlaySoundService", "call onDestroy()");
        play_sound();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("PlaySoundService", "call onDestroy()");
        stop_sound();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void setup_sound_data() {
        Log.i("PlaySoundService", "call setup_sounddata()");
        try(AssetFileDescriptor afd = getAssets().openFd(mp3_filepath))
        {
            mMediaplayer.setDataSource(
                afd.getFileDescriptor(),
                afd.getStartOffset(),
                afd.getLength()
            );
            mMediaplayer.prepare();
        } catch (IOException e) {
            Log.e("PlaySoundService", "setup_sound_data IOException:" + e.getMessage());
        }
    }

    private void play_sound() {
        Log.i("PlaySoundService", "call play_sound()");
        if(mMediaplayer == null) {
            setup_sound_data();
        }
        mMediaplayer.setLooping(true);
        mMediaplayer.start();
    }

    private void stop_sound() {
        Log.i("PlaySoundService", "call stop_sound()");
        if(mMediaplayer != null) {
            mMediaplayer.stop();
            mMediaplayer.reset();
            mMediaplayer.release();
            mMediaplayer = null;
        }
    }
}