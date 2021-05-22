package com.stormstreaming.stormplayer.elements;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;

import com.stormstreaming.stormlibrary.model.StormMediaItem;
import com.stormstreaming.stormlibrary.model.VideoMetaData;
import com.stormstreaming.stormplayer.R;
import com.stormstreaming.stormplayer.StormPlayerView;

import java.util.List;

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
    public void onGatewayConnecting() {
        ((Activity)this.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLoader();
            }
        });
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

        ((Activity)this.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoader();
            }
        });
    }

    @Override
    public void onErrorScreenShow(String errorMessage) {

        ((Activity)this.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoader();
            }
        });
    }

    @Override
    public void onVideoMetaData(VideoMetaData videoMetaData){
        ((Activity)this.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoader();
            }
        });
    }

    @Override
    public void onGatewayConnectionError(Exception e){
        ((Activity)this.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoader();
            }
        });
    }

    @Override
    public void onGatewayGroupNameNotFound(){
        ((Activity)this.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoader();
            }
        });
    }

    @Override
    public void onGatewayStormMediaItems(List<StormMediaItem> stormMediaItems){
        ((Activity)this.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoader();
            }
        });
    }
}
