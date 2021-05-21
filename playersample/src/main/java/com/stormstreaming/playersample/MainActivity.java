package com.stormstreaming.playersample;

import android.os.Bundle;

import com.stormstreaming.stormlibrary.StormGateway;
import com.stormstreaming.stormlibrary.StormLibrary;
import com.stormstreaming.stormlibrary.exception.EmptyMediaItemsListException;
import com.stormstreaming.stormlibrary.model.StormGatewayServer;
import com.stormstreaming.stormlibrary.model.StormMediaItem;
import com.stormstreaming.stormplayer.StormPlayerView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        EMBED EXAMPLE:
         */


        StormLibrary stormLibrary = new StormLibrary();

        StormPlayerView stormPlayerView = findViewById(R.id.stormPlayerView);
        stormPlayerView.setStormLibrary(stormLibrary);

        /*temp */
        StormMediaItem mediaItem23 = new StormMediaItem("stormdev.web-anatomy.com", 443, true, "test_hd", "320p");
        stormLibrary.addMediaItem(mediaItem23, true);
        /*temp koniec*/

        StormMediaItem mediaItem = new StormMediaItem("sub1.example.com", 80, false, "test_hd", "320p");
        stormLibrary.addMediaItem(mediaItem);

        mediaItem = new StormMediaItem("sub2.example.com", 80, false, "test_hd", "720p");
        stormLibrary.addMediaItem(mediaItem);

        try {
            stormLibrary.prepare(true);
        } catch (EmptyMediaItemsListException e) {
            e.printStackTrace();
        }


        /*
        GATEWAY EXAMPLE:
         */

        /*
        StormLibrary stormLibrary = new StormLibrary();

        StormPlayerView stormPlayerView = findViewById(R.id.stormPlayerView);
        stormPlayerView.setStormLibrary(stormLibrary);

        StormGateway stormGateway = stormLibrary.initStormGateway("test");

        StormGatewayServer server = new StormGatewayServer("sub1.domain.com","live", 443, true);
        stormGateway.addStormGatewayServer(server);

        StormGatewayServer server2 = new StormGatewayServer("sub2.domain.com","live", 443, true);
        stormGateway.addStormGatewayServer(server2);

        try {
            stormLibrary.prepare(true);
        } catch (EmptyMediaItemsListException e) {
            e.printStackTrace();
        }
         */
    }
}