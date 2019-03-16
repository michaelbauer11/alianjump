package com.example.owner.ninjamp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Animation {
    private Bitmap[] frames;
    private int frameIndex;
    private boolean playedOnce = false;
    private boolean doneRunOnce = false;
    private boolean isPlaying = false;
    private float frameTime;
    private long lastFrame;
    private final Paint generalPaint = new Paint();


    public void setPlayedOnce(boolean playedOnce){
        this.playedOnce = playedOnce;
        doneRunOnce = false;
    }
    public boolean isPlaying() {
        return isPlaying;
    }
    public boolean isDoneRunOnce(){ return doneRunOnce; }


    public void play() {
        isPlaying = true;
        frameIndex = 0;
        lastFrame = System.currentTimeMillis();
    }

    public void stop() {
        isPlaying = false;
    }

    Animation(Bitmap[] frames, float animTime) {
        this.frames = frames;
        frameIndex = 0;
        frameTime = animTime/frames.length;
        lastFrame = System.currentTimeMillis();
    }

    private void scaleRect(Rect rect) {
        float whRatio = (float)(frames[frameIndex].getWidth())/frames[frameIndex].getHeight();
        if(rect.width() > rect.height())
            rect.left = rect.right - (int)(rect.height() * whRatio);
        else
            rect.top = rect.bottom - (int)(rect.width() * (1/whRatio));
    }

    public void draw(Canvas canvas, Rect destination) {
        //scaleRect(destination);
        canvas.drawBitmap(frames[frameIndex], null, destination, generalPaint);
    }

    public void update() {
        if(System.currentTimeMillis() - lastFrame > frameTime*1000) {
            frameIndex++;
            if(frameIndex >= frames.length){
                if(System.currentTimeMillis() - lastFrame > frameTime*1000) frameIndex = 0;
                if (playedOnce){
                    stop();
                    doneRunOnce = true;
                    frameIndex = frames.length - 1;
                }
            }
            lastFrame = System.currentTimeMillis();
        }
    }
}