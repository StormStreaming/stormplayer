package com.stormstreaming.stormplayer.elements;

import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatImageView;

import com.stormstreaming.stormplayer.R;
import com.stormstreaming.stormplayer.StormPlayerView;

public class FullscreenButton extends InterfaceElement implements View.OnClickListener, StormPlayerView.EventListener {

    private AppCompatImageView stormFullscreenButton;

    public FullscreenButton(StormPlayerView stormPlayerView) {
        super(stormPlayerView);

        this.stormFullscreenButton = stormPlayerView.findViewById(R.id.stormFullscreenButton);
        this.stormFullscreenButton.setOnClickListener(this);

        this.stormPlayerView.addEventListener(this);
    }

    @Override
    public void onFullScreenEnter(){
        this.stormFullscreenButton.setImageResource(R.drawable.storm_selector_exit_fullscreen);
    }

    @Override
    public void onFullScreenExit(){
        this.stormFullscreenButton.setImageResource(R.drawable.storm_selector_enter_fullscreen);
    }

    @Override
    public void onClick(View v) {
        if(!this.stormPlayerView.isFullscreen()) {
            this.stormPlayerView.getListeners().dispatchEvent(listener -> listener.onEnterFullscreenClicked());
            this.stormPlayerView.enterFullScreen();
        }else{
            this.stormPlayerView.getListeners().dispatchEvent(listener -> listener.onExitFullscreenClicked());
            this.stormPlayerView.exitFullScreen();
        }
    }
}
