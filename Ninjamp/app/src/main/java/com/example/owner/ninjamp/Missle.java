package com.example.owner.ninjamp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.provider.SyncStateContract;

/**
 * Created by Michael Bauer
 * Date : 15.6.19
 * This Class represent an obstacle
 */
public class Missle implements GameObject {
    private Rect rectangle;
    private Paint paint = new Paint();

    private Bitmap missle;

    // Missle width and Height
    private int MISSLE_WIDTH = 60;
    private int MISSLE_HEIGHT = 20;

    private int MISSLE_SPEED = 3;

    Missle(){
        int startingPointTop = (int) (Math.random() * PUBLIC_VAR.SCREEN_HEIGHT/2);
        int startingPointLeft = PUBLIC_VAR.SCREEN_WIDTH + 10;
        this.rectangle = new Rect(startingPointLeft, startingPointTop, startingPointLeft + MISSLE_WIDTH, startingPointTop + MISSLE_HEIGHT);
        missle = ConstantsFunc.decodeSampledBitmapFromResourceForJPEG(PUBLIC_VAR.CURRENT_CONTEXT.getResources(),R.drawable.missile9, rectangle.width(), rectangle.height());
    }

    public Rect getRectangle(){
        return rectangle;
    }

    public boolean rectCollide(Rect rect){
        return Rect.intersects(rectangle, rect);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(missle, null, rectangle, paint);
    }

    @Override
    public void update() {
        rectangle.right -= MISSLE_SPEED;
        rectangle.left -= MISSLE_SPEED;
    }
}