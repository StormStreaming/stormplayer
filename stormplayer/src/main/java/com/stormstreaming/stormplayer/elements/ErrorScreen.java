package com.stormstreaming.stormplayer.elements;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.stormstreaming.stormplayer.R;
import com.stormstreaming.stormplayer.StormPlayerView;

public class ErrorScreen extends InterfaceElement {

    private View errorScreen;

    public ErrorScreen(StormPlayerView stormPlayerView) {
        super(stormPlayerView);

        this.stormPlayerView.getStormLibrary().addEventListener(this);
    }

    public void showError(String text) {

        if (this.errorScreen != null)
            return;

        LayoutInflater layoutInflater = (LayoutInflater) this.stormPlayerView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.errorScreen = layoutInflater.inflate(R.layout.storm_player_error, this.stormPlayerView, false);
        this.stormPlayerView.addView(this.errorScreen);

        TextView errorText = this.errorScreen.findViewById(R.id.stormErrorText);
        errorText.setText(text);

        this.stormPlayerView.getListeners().dispatchEvent(listener -> listener.onErrorScreenShow(text));

    }

    public void hideError() {
        if (this.errorScreen == null)
            return;

        this.stormPlayerView.removeView(errorScreen);
        this.errorScreen = null;
        this.stormPlayerView.getListeners().dispatchEvent(listener -> listener.onErrorScreenHide());
    }

    @Override
    public void onVideoStop() {
        ((Activity) this.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showError(stormPlayerView.getResources().getString(R.string.videoStopText));
            }
        });
    }

    @Override
    public void onVideoNotFound() {
        ((Activity) this.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showError(stormPlayerView.getResources().getString(R.string.videoNotFoundText));
            }
        });
    }

    @Override
    public void onIncompatiblePlayerProtocol(int playerProtocolVersion, int serverProtocolVersion) {
        ((Activity) this.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showError(stormPlayerView.getResources().getString(R.string.incompatiblePlayerProtocolText));
            }
        });
    }

    @Override
    public void onConnectionError(Exception e) {
        ((Activity) this.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showError(stormPlayerView.getResources().getString(R.string.connectionErrorText));
            }
        });
    }

    public View getErrorScreen() {
        return errorScreen;
    }
}
