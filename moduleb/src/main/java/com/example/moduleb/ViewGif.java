package com.example.moduleb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.net.Uri;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class ViewGif extends View {

    private Movie mMovie;
    private long mMovieStart;
    java.io.InputStream is = SecondActivity.input;

    public ViewGif(Context context) {
        super(context);
        setFocusable(true);

        mMovie = SecondActivity.movie;
    }

    public ViewGif(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        setFocusable(true);

        mMovie = SecondActivity.movie;
    }

    public ViewGif(Context context, AttributeSet attrSet, int defStyle) {
        super(context, attrSet, defStyle);
        setFocusable(true);

        mMovie = SecondActivity.movie;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0x00000000);

        Paint p = new Paint();
        p.setAntiAlias(true);

        long now = android.os.SystemClock.uptimeMillis();
        if (mMovieStart == 0) { // first time
            mMovieStart = now;
        }
        if (mMovie != null) {
            int dur = mMovie.duration();
            if (dur == 0) {
                dur = 1000;
            }
            int relTime = (int) ((now - mMovieStart) % dur);
            mMovie.setTime(relTime);
            mMovie.draw(canvas, getWidth() / 2 - mMovie.width() / 2,
                    getHeight() / 2 - mMovie.height() / 2);
            invalidate();
        }
    }
}