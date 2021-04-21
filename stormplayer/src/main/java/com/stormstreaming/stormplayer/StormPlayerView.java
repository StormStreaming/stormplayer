package com.stormstreaming.stormplayer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.exoplayer2.ui.PlayerView;
import com.stormstreaming.stormlibrary.StormLibrary;
import com.stormstreaming.stormplayer.elements.ErrorScreen;
import com.stormstreaming.stormplayer.elements.FullscreenButton;
import com.stormstreaming.stormplayer.elements.LoaderElement;
import com.stormstreaming.stormplayer.elements.PlaybackButton;
import com.stormstreaming.stormplayer.elements.QualityButton;
import com.stormstreaming.stormplayer.elements.SeekBarElement;
import com.stormstreaming.stormplayer.events.Listeners;

public class StormPlayerView extends FrameLayout implements View.OnClickListener{

    public interface EventListener{

        default public void onPlayClicked(){};
        default public void onPauseClicked(){};
        default public void onEnterFullscreenClicked(){};
        default public void onExitFullscreenClicked(){};

        default public void onVideoClicked(){}

        default public void onSeekBarChangedByUser(int progress){}
        default public void onSeekBarSetTime(long streamSeekUnixTime){}

        default public void onFullScreenEnter(){}
        default public void onFullScreenExit(){}

        default public void onErrorScreenShow(String errorMessage){}
        default public void onErrorScreenHide(){}

        default public void onLoaderShow(){}
        default public void onLoaderHide(){}
    }

    private View view;
    private StormLibrary stormLibrary;
    private PlayerView exoPlayerView;
    private HideGUIController hideGUIController;

    private Listeners<StormPlayerView.EventListener> listeners = new Listeners<StormPlayerView.EventListener>();

    /*
    For fullscreen
     */
    private Dialog fullScreenDialog;
    private View stormPlayerContainer;

    /*
    GUI elements
     */
    private PlaybackButton playbackButton;
    private FullscreenButton fullscreenButton;
    private ErrorScreen errorScreen;
    private SeekBarElement seekBarElement;
    private QualityButton qualityButton;
    private LoaderElement loaderElement;

    private boolean isFullscreen = false;

    public StormPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public StormPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = layoutInflater.inflate(R.layout.storm_player_view, this, false);
        this.exoPlayerView = this.view.findViewById(R.id.exoPlayerView);
        this.exoPlayerView.getVideoSurfaceView().setOnClickListener(this);
        this.addView(this.view);
    }

    public void setStormLibrary(StormLibrary stormLibrary) {
        this.stormLibrary = stormLibrary;
        this.stormLibrary.initExoPlayer(getContext(), this.exoPlayerView);
        new StormPlayerController(this, this.stormLibrary);

        this.prepareElements();
    }

    public void enterFullScreen(){

        if(this.fullScreenDialog == null && this.errorScreen.getErrorScreen() == null) {

            Activity activity = (Activity) this.getContext();
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            this.stormPlayerContainer = this.findViewById(R.id.stormPlayerContainer);
            this.fullScreenDialog = new Dialog(this.getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            this.fullScreenDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.fullScreenDialog.setCanceledOnTouchOutside(true);
            this.fullScreenDialog.show();

            ((ViewGroup) this.stormPlayerContainer.getParent()).removeView(this.stormPlayerContainer);
            this.fullScreenDialog.setContentView(this.stormPlayerContainer);
            Window window = this.fullScreenDialog.getWindow();
            window.setGravity(Gravity.CENTER);

            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            this.listeners.dispatchEvent(listener -> listener.onFullScreenEnter());
            this.isFullscreen = true;
        }
    }

    public void exitFullScreen(){
        if(this.fullScreenDialog != null) {
            Activity activity = (Activity) this.getContext();
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            ((ViewGroup)this.stormPlayerContainer.getParent()).removeView(this.stormPlayerContainer);
            ((ConstraintLayout) this.findViewById(R.id.stormPlayerRoot)).addView(stormPlayerContainer);
            this.fullScreenDialog.dismiss();
            this.fullScreenDialog = null;

            this.listeners.dispatchEvent(listener -> listener.onFullScreenExit());
            this.isFullscreen = false;
        }
    }

    public StormLibrary getStormLibrary() {
        return stormLibrary;
    }

    public View getView(){
        return this.view;
    }

    public PlayerView getExoPlayerView() {
        return exoPlayerView;
    }

    public Listeners<StormPlayerView.EventListener> getListeners(){
        return listeners;
    }

    public void addEventListener(StormPlayerView.EventListener e){
        listeners.addEventListener(e);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == this.exoPlayerView.getVideoSurfaceView().getId())
            listeners.dispatchEvent(listener -> listener.onVideoClicked());
    }

    private void prepareElements(){
        this.hideGUIController = new HideGUIController(this);
        this.playbackButton = new PlaybackButton(this);
        this.fullscreenButton = new FullscreenButton(this);
        this.errorScreen = new ErrorScreen(this);
        this.seekBarElement = new SeekBarElement(this);
        this.qualityButton = new QualityButton(this);
        this.loaderElement = new LoaderElement(this);
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }
}
