package com.example.owner.ninjamp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Michael Bauer
 * Date : 3.7.18
 * This Class represent an obstacle
 */
public class Obstacle implements GameObject {
    private Rect rectangle;
    private Paint paint = new Paint();

    private Bitmap tile;


    Obstacle(int left, int top, int right, int bottom, int tileID){
        this.rectangle = new Rect(left, top, right, bottom);
        tile = ConstantsFunc.decodeSampledBitmapFromResourceForJPEG(PUBLIC_VAR.CURRENT_CONTEXT.getResources(),tileID, rectangle.width(), rectangle.height());
    }

    public void updateObstacleParam(int left, int top, int right, int bottom, int tileID){
        this.rectangle.set(left,top,right,bottom);
        this.tile = ConstantsFunc.decodeSampledBitmapFromResourceForJPEG(PUBLIC_VAR.CURRENT_CONTEXT.getResources(),tileID, rectangle.width(), rectangle.height());
    }


    public void increaseX(float x){
        rectangle.right -= x;
        rectangle.left -= x;
    }

    public Rect getRectangle(){
        return rectangle;
    }



    public boolean rectCollide(Rect rect){
        return Rect.intersects(rectangle, rect);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(tile, null, rectangle, paint);
    }

    @Override
    public void update() {

    }
}