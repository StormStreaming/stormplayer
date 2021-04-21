package com.stormstreaming.stormplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSeekBar;

import com.stormstreaming.stormplayer.R;

public class SeekBar extends AppCompatSeekBar {

    private Paint bigCirclePaint;

    public SeekBar(Context context) {
        super(context);
        init();
    }

    public SeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Initialization
     */
    private void init() {


        bigCirclePaint = new Paint();
        bigCirclePaint.setColor(getResources().getColor(R.color.storm_seekbar_background));
        bigCirclePaint.setAntiAlias(true);

        //Api21 and above call, remove the background behind the slider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSplitTrack(false);
        }
    }

    /**
     * Rewrite the onDraw method to draw tick marks
     *
     * @param canvas
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {

        Rect thumbRect = null;
        if (getThumb() != null) {
            thumbRect = getThumb().getBounds();
        }

        int cX = getWidth()-45;
        int cY = getHeight()/2;

        //big circle
        canvas.drawCircle(cX, cY, 18, bigCirclePaint);

        super.onDraw(canvas);
    }


}
