package com.stormstreaming.stormplayer.elements;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;

import com.stormstreaming.stormlibrary.model.StormMediaItem;
import com.stormstreaming.stormplayer.R;
import com.stormstreaming.stormplayer.StormPlayerView;

import java.util.List;

public class QualityButton extends InterfaceElement implements View.OnClickListener{

    private TextView stormQualityText;

    public QualityButton(StormPlayerView stormPlayerView){
        super(stormPlayerView);
        this.stormQualityText = stormPlayerView.findViewById(R.id.stormQualityText);
        this.stormQualityText.setOnClickListener(this);
        this.stormPlayerView.getStormLibrary().addEventListener(this);

    }

    public void setStormMediaItem(String label){
        List<StormMediaItem> stormMediaItems = this.stormPlayerView.getStormLibrary().getStormMediaItems();
        for(int i=0;i<stormMediaItems.size();i++){
            if(stormMediaItems.get(i).getLabel().equals(label)){
                this.stormPlayerView.getStormLibrary().playMediaItem(stormMediaItems.get(i), false);
                break;
            }
        }
    }

    @Override
    public void onStormMediaItemSelect(StormMediaItem stormMediaItem){
        this.stormQualityText.setText(stormMediaItem.getLabel());
    }

    @Override
    public void onStormMediaItemAdded(StormMediaItem stormMediaItem){
        List<StormMediaItem> stormMediaItems = this.stormPlayerView.getStormLibrary().getStormMediaItems();
        if(stormMediaItems.size() > 1)
            this.stormQualityText.setVisibility(View.VISIBLE);
        else
            this.stormQualityText.setVisibility(View.GONE);
    }

    @Override
    public void onStormMediaItemRemoved(StormMediaItem stormMediaItem){
        List<StormMediaItem> stormMediaItems = this.stormPlayerView.getStormLibrary().getStormMediaItems();
        if(stormMediaItems.size() == 1)
            this.stormQualityText.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        QualityButton that = this;
        Context context = this.stormPlayerView.getContext();

        List<StormMediaItem> stormMediaItems = this.stormPlayerView.getStormLibrary().getStormMediaItems();
        String[] list = new String[stormMediaItems.size()];
        int checkedItemID = 0;
        for(int i=0;i<stormMediaItems.size();i++){
            list[i] = stormMediaItems.get(i).getLabel();
            if(stormMediaItems.get(i).isSelected())
                checkedItemID = i;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.StormQualityDialog));


        builder.setSingleChoiceItems(list, checkedItemID, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                that.setStormMediaItem(list[which]);
                dialog.dismiss();
            }
        });


        AlertDialog alertdialog = builder.create();
        alertdialog.show();

    }
}
