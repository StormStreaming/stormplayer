package com.stormstreaming.stormplayer.elements;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.stormstreaming.stormlibrary.model.VideoProgress;
import com.stormstreaming.stormplayer.R;
import com.stormstreaming.stormplayer.StormPlayerView;
import com.stormstreaming.stormplayer.view.SeekBar;

public class SeekBarElement extends InterfaceElement implements android.widget.SeekBar.OnSeekBarChangeListener {

    private long progressBarStartTime;
    private long progressBarEndTime;
    private long progressBarCurrTime;

    private VideoProgress videoProgress;
    private volatile boolean stopRefreshBar = false;

    private int lastSeekProgress = 0;
    private SeekBar seekBar;
    private TextView stormSeekBarTooltipText;

    public SeekBarElement(StormPlayerView stormPlayerView) {
        super(stormPlayerView);

        this.seekBar = stormPlayerView.findViewById(R.id.stormSeekBar);
        this.seekBar.setOnSeekBarChangeListener(this);

        this.stormSeekBarTooltipText = stormPlayerView.findViewById(R.id.stormSeekBarTooltipText);

        this.stormPlayerView.getStormLibrary().addEventListener(this);
    }

    @Override
    public void onVideoProgress(VideoProgress videoProgress){

        this.videoProgress = videoProgress;

        this.progressBarStartTime = videoProgress.getSourceStartTime() + videoProgress.getSourceDuration() - videoProgress.getDvrCacheSize();
        this.progressBarEndTime = videoProgress.getSourceStartTime() + videoProgress.getSourceDuration();
        this.progressBarCurrTime = videoProgress.getStreamStartTime() + videoProgress.getStreamDuration();

        if(this.progressBarCurrTime > this.progressBarEndTime)
            this.progressBarCurrTime = this.progressBarEndTime;

        ((Activity)this.stormPlayerView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshBar();
            }
        });
    }

    private void refreshBar(){

        if(stopRefreshBar)
            return;

        if(this.videoProgress.getDvrCacheSize() < 1000 * 10)
            ((View)this.seekBar.getParent()).setVisibility(View.GONE);
        else{
            ((View)this.seekBar.getParent()).setVisibility(View.VISIBLE);

            double end = this.progressBarEndTime - this.progressBarStartTime;
            double value = this.progressBarCurrTime - this.progressBarStartTime;

            double percent = (value * 100)/end;
            this.setSeekBarValue(percent);
        }

    }

    /*
    progress is a value between 0 and 10000
     */
    public void seekTo(int progress){

        if(this.videoProgress == null)
            return;

        /*
        progress to percent
         */
        double percent = ((double)progress*100)/10000;

        /*
        percent to time
         */
        long seekTime = this.percentToTime(percent);

        this.stormPlayerView.getListeners().dispatchEvent(listener -> listener.onSeekBarSetTime(seekTime));
    }

    private long percentToDvrTime(double percent){

        if(this.videoProgress == null)
            return 0;

        percent = 100-percent;

        long endTime = this.videoProgress.getDvrCacheSize();
        long dvrTime =  Math.round((endTime*percent)/100);
        return dvrTime;
    }

    private long percentToTime(double percent){
        return this.progressBarEndTime-this.percentToDvrTime(percent);
    }

    private void updateToolTip(int progress){

        double percent = ((double)progress*100)/10000;
        int dvrTimeSeconds = (int)this.percentToDvrTime(percent)/1000;

        String txt = dvrTimeSeconds > 0 ? "-"+this.secondsToNiceTime(dvrTimeSeconds) : stormPlayerView.getResources().getString(R.string.liveText);

        this.stormSeekBarTooltipText.setText(txt);
    }

    private void setSeekBarValue(double percent){
        long value = Math.round((percent*10000)/100);
        this.seekBar.setProgress((int)value);
    }

    private String secondsToNiceTime(int seconds){
        int p1 = seconds % 60;
        int p2 = seconds / 60;
        int p3 = p2 % 60;

        return String.format("%02d", p3)+ ":" + String.format("%02d", p1);

    }

    @Override
    public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {

        if(fromUser) {
            if(progress >= 9650) {
                this.seekBar.setProgress(10000);
                progress = 10000;
            }

            this.lastSeekProgress = progress;
            final int finalProgress = progress;
            this.stormPlayerView.getListeners().dispatchEvent(listener -> listener.onSeekBarChangedByUser(finalProgress));
        }

        this.stormSeekBarTooltipText.measure(0,0);

        int x = seekBar.getThumb().getBounds().left-(this.stormSeekBarTooltipText.getMeasuredWidth()/2)+37;
        if(x < 0)
            x = 0;
        else if(x > seekBar.getWidth()-this.stormSeekBarTooltipText.getWidth())
            x = seekBar.getWidth()-this.stormSeekBarTooltipText.getWidth();

        this.stormSeekBarTooltipText.setX(x);
        this.updateToolTip(progress);

    }

    @Override
    public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
        this.stopRefreshBar = true;
        this.stormSeekBarTooltipText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
        this.stopRefreshBar = false;
        this.stormSeekBarTooltipText.setVisibility(View.GONE);
        this.seekTo(this.lastSeekProgress);
    }
}
