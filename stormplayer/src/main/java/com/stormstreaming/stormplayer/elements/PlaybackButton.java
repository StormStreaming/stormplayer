package com.stormstreaming.stormplayer.elements;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.stormstreaming.stormplayer.R;
import com.stormstreaming.stormplayer.StormPlayerView;

import java.util.Timer;

public class PlaybackButton extends InterfaceElement implements View.OnClickListener, StormPlayerView.EventListener{

    private boolean currentStatusPlay = false;

    private AppCompatImageView stormPlaybackButton;

    public PlaybackButton(StormPlayerView stormPlayerView) {
        super(stormPlayerView);
        this.stormPlaybackButton = stormPlayerView.findViewById(R.id.stormPlaybackButton);
        this.stormPlaybackButton.setOnClickListener(this);
        this.stormPlayerView.getStormLibrary().addEventListener(this);

    }

    public void setPlayStatus(boolean isPlay){
        this.currentStatusPlay = isPlay;

        if(currentStatusPlay)
            this.stormPlaybackButton.setImageResource(R.drawable.storm_selector_pause);
        else
            this.stormPlaybackButton.setImageResource(R.drawable.storm_selector_play);

    }

    /*
    StormLibrary events
     */

    @Override
    public void onPlay(){
        ((Activity)this.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPlayStatus(true);
            }
        });
    }

    @Override
    public void onPause(){
        ((Activity)this.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPlayStatus(false);
            }
        });
    }

    /*
    View events
     */

    @Override
    public void onClick(View v) {
        if(!currentStatusPlay)
            this.stormPlayerView.getListeners().dispatchEvent(listener -> listener.onPlayClicked());
        else
            this.stormPlayerView.getListeners().dispatchEvent(listener -> listener.onPauseClicked());

    }
}
