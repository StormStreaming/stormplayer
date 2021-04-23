package com.stormstreaming.stormplayer;

import android.app.Activity;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.stormstreaming.stormlibrary.StormLibrary;

import java.util.Timer;
import java.util.TimerTask;

public class HideGUIController implements StormPlayerView.EventListener, StormLibrary.EventListener{

    class HideTimerTask extends TimerTask {

        private HideGUIController hideGUIController;
        private volatile int hideAfterMS = HIDE_DELAY_MS;

        public HideTimerTask(HideGUIController hideGUIController){
            this.hideGUIController = hideGUIController;
        }

        public void reset(){
            this.hideAfterMS = HIDE_DELAY_MS;
        }

        public void stop(){
            this.hideAfterMS = -1;
        }

        public void run() {
            HideGUIController that = this.hideGUIController;
            ((Activity)this.hideGUIController.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(hideAfterMS == 0) {
                        that.hideGUI();
                        hideAfterMS = -1;
                    }
                }
            });
            hideAfterMS -= 500;
        }
    }

    /*
    Must be a multiply of 500
     */
    public static final int HIDE_DELAY_MS = 2500;

    private StormPlayerView stormPlayerView;
    private Timer hideTimer;
    private HideTimerTask hideTimerTask;

    private volatile boolean isPlaying = false;

    private ConstraintLayout stormControls;

    public HideGUIController(StormPlayerView stormPlayerView){
        this.stormPlayerView = stormPlayerView;
        this.stormControls = stormPlayerView.findViewById(R.id.stormControls);
        this.stormPlayerView.addEventListener(this);
        this.stormPlayerView.getStormLibrary().addEventListener(this);

    }

    public void hideGUI(){
        if(!this.isPlaying)
            return;
        cancelHideTimer();
        this.stormControls.setVisibility(View.GONE);
    }

    public void showGUI(){
        this.stormControls.setVisibility(View.VISIBLE);
    }

    private void cancelHideTimer(){
        if(this.hideTimer == null)
            return;
        this.hideTimer.cancel();
        this.hideTimerTask.stop();
        this.hideTimer = null;
        this.hideTimerTask = null;
    }

    private void restartHideTimer(){
        this.cancelHideTimer();

        this.hideTimer = new Timer();
        this.hideTimerTask = new HideTimerTask(this);
        this.hideTimer.schedule(this.hideTimerTask, 0, 500);
    }

    @Override
    public void onVideoPlay(){
        this.isPlaying = true;
        this.restartHideTimer();
    }

    @Override
    public void onVideoPause(){
        this.isPlaying = false;
        this.restartHideTimer();
    }

    @Override
    public void onSeekBarChangedByUser(int progress){
        if(this.hideTimerTask != null)
            this.hideTimerTask.reset();
    }

    @Override
    public void onVideoClicked(){
        this.showGUI();
        this.restartHideTimer();
    }

}
