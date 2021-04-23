package com.stormstreaming.stormplayer.elements;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;

import com.stormstreaming.stormplayer.R;
import com.stormstreaming.stormplayer.StormPlayerView;

public class LoaderElement extends InterfaceElement implements StormPlayerView.EventListener{

    ProgressBar loader;

    public LoaderElement(StormPlayerView stormPlayerView) {
        super(stormPlayerView);
        this.loader = stormPlayerView.findViewById(R.id.stormLoader);
        this.stormPlayerView.getStormLibrary().addEventListener(this);
        this.stormPlayerView.addEventListener(this);

    }

    public void showLoader(){
        this.loader.setVisibility(View.VISIBLE);
        this.stormPlayerView.getListeners().dispatchEvent(listener -> listener.onLoaderShow());
    }

    public void hideLoader(){
        this.loader.setVisibility(View.GONE);
        this.stormPlayerView.getListeners().dispatchEvent(listener -> listener.onLoaderHide());
    }

    @Override
    public void onVideoConnecting() {
        ((Activity)this.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLoader();
            }
        });
    }

    @Override
    public void onVideoPlay() {
        hideLoader();
    }

    @Override
    public void onErrorScreenShow(String errorMessage) {
        hideLoader();
    }
}
