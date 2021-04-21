package com.stormstreaming.stormplayer.elements;

import com.stormstreaming.stormlibrary.StormLibrary;
import com.stormstreaming.stormplayer.StormPlayerView;

public class InterfaceElement implements StormLibrary.EventListener{

    protected StormPlayerView stormPlayerView;

    public InterfaceElement(StormPlayerView stormPlayerView){
        this.stormPlayerView = stormPlayerView;
    }

}
