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
public class Meteorit implements GameObject {
    private Rect rectangle;
    private Paint paint = new Paint();

    private Bitmap meteorit;

    // Meteorit width and Height
    private int METEORIT_WIDTH = 50;
    private int METEORIT_HEIGHT = 50;

    private int METEORIT_SPEED = 3;

    Meteorit(){
        int startingPointTop = (int) (Math.random() * PUBLIC_VAR.SCREEN_HEIGHT/2);
        int startingPointLeft = PUBLIC_VAR.SCREEN_WIDTH + 10;
        this.rectangle = new Rect(startingPointLeft, startingPointTop, startingPointLeft + METEORIT_WIDTH, startingPointTop + METEORIT_HEIGHT);
        meteorit = ConstantsFunc.decodeSampledBitmapFromResourceForJPEG(PUBLIC_VAR.CURRENT_CONTEXT.getResources(),R.drawable.missile9, rectangle.width(), rectangle.height());
    }

    public Rect getRectangle(){
        return rectangle;
    }

    public boolean rectCollide(Rect rect){
        return Rect.intersects(rectangle, rect);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(meteorit, null, rectangle, paint);
    }

    @Override
    public void update() {
        rectangle.right -= METEORIT_SPEED;
        rectangle.left -= METEORIT_SPEED;
    }
}