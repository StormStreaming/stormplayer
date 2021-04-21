package com.stormstreaming.stormplayer.events;

import java.util.concurrent.CopyOnWriteArrayList;

public class Listeners<T> {

    public interface Event<T> {
        void invoke(T listener);
    }

    private CopyOnWriteArrayList<T> listenerAndHandlers = new CopyOnWriteArrayList<T>();

    public void addEventListener(T e){
        listenerAndHandlers.add(e);
    }

    public void removeEventListener(T e){
        listenerAndHandlers.remove(e);
    }

    public void dispatchEvent(Event<T> event){
        for(int i=0;i<listenerAndHandlers.size();i++){
            event.invoke(listenerAndHandlers.get(i));
        }
    }
}
