package com.stormstreaming.stormplayerapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.stormstreaming.stormlibrary.StormLibrary;
import com.stormstreaming.stormlibrary.exception.EmptyMediaItemsListException;
import com.stormstreaming.stormlibrary.model.StormMediaItem;
import com.stormstreaming.stormplayer.StormPlayerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StormLibrary stormLibrary = new StormLibrary();

        StormPlayerView stormPlayerView = findViewById(R.id.stormPlayerView);
        stormPlayerView.setStormLibrary(stormLibrary);

        StormMediaItem mediaItem = new StormMediaItem("stormdev.web-anatomy.com", 80, false, "test_hd", "320p");
        stormLibrary.addMediaItem(mediaItem);

        mediaItem = new StormMediaItem("stormdev.web-anatomy.com", 80, false, "test_hd", "720p");
        stormLibrary.addMediaItem(mediaItem);

        try {
            stormLibrary.prepare(true);
        } catch (EmptyMediaItemsListException e) {
            e.printStackTrace();
        }
    }
}