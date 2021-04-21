package com.stormstreaming.stormplayer;

import android.util.Log;

import com.stormstreaming.stormlibrary.StormLibrary;

public class StormPlayerController implements StormPlayerView.EventListener{

    private StormLibrary stormLibrary;

    public StormPlayerController(StormPlayerView stormPlayerView, StormLibrary stormLibrary){
        this.stormLibrary = stormLibrary;
        stormPlayerView.addEventListener(this);
    }

    @Override
    public void onPlayClicked(){
        this.stormLibrary.play();
    }

    @Override
    public void onPauseClicked(){
        this.stormLibrary.pause();
    }

    @Override
    public void onSeekBarSetTime(long streamSeekUnixTime){
        try {
            this.stormLibrary.seekTo(streamSeekUnixTime);
        }catch(Exception e){
            Log.e("StormPlayer", "Error while seeking", e);
        }
    }
}
