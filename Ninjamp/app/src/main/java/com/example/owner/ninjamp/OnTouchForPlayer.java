package com.example.owner.ninjamp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;


/*
* Created by Michael Bauer
* Date : 1.7.18
* This class relays to every thing that should happen when the user
* touches the screen and wants to move the player,
* During the touch on the screen :
* It calculates the parabola points according to the estimated velocities
* and creates a parabola array with Point object - (x,y).
* Than it draw the parabola on the screen by drawing a circle in every Point in the array.
*/
public class OnTouchForPlayer implements GameObject{
    private ArrayList<Point> parabolaPoint;
    private boolean pressed;
    private Rect playerRect;
    private double estimatedVelX;
    private double estimatedVelY;
    private boolean reachEnd;
    private Paint paint;

    /**
     * Constructor
     * @param playerRect - receive the player rect
     */
    OnTouchForPlayer(Rect playerRect){
        parabolaPoint = new ArrayList<>();
        this.playerRect = playerRect;
        pressed = false;
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    /**
     * Initiates the estimated velocities - X and Y - every time a new touch is occur
     */
    public void setTouchStart(){
        this.pressed = true;
        this.estimatedVelY = PUBLIC_VAR.X_VELOCITY_INC*4;
        this.estimatedVelX = PUBLIC_VAR.Y_VELOCITY_INC*4;
        paint.setAlpha(255);
        this.reachEnd = false;
    }

    /**
     update estimated velocities in X and Y, each frame, while user is pressing
     and than call to updateParabolaPoints to update the drawn parabola
     */
    private void updateParabolaParam(){
        if (!reachEnd) {
             estimatedVelX += PUBLIC_VAR.X_VELOCITY_INC;
             estimatedVelY -= PUBLIC_VAR.Y_VELOCITY_INC;
        } else {
             estimatedVelX -= PUBLIC_VAR.X_VELOCITY_INC;
             estimatedVelY += PUBLIC_VAR.Y_VELOCITY_INC;
        }

        updateParabolaPoints();
    }

    /**
     update the parabola points that represents the estimated path of the player after
     increasing/decreasing its velocity
     */
    private void updateParabolaPoints(){
        if (parabolaPoint.size() != 0) parabolaPoint.clear();
        double newPointX;
        double newPointY;
        int t = 0;
        do{
            newPointX = estimatedVelX * t;
            // gravity equals 1 always
            newPointY = estimatedVelY * t + 0.5 * Math.pow(t, 2);
            parabolaPoint.add(new Point((int) newPointX + playerRect.centerX(), (int) newPointY + playerRect.centerY()));
            // bigger counter means less points represented
            t+= PUBLIC_VAR.TIME_BETWEEN_PARABOLA_POINTS;
        }
        while ((int)newPointY + playerRect.centerY() <= playerRect.centerY());
        // treats the parabola direction
        if (parabolaPoint.get(parabolaPoint.size() - 1).x >= PUBLIC_VAR.SCREEN_WIDTH) reachEnd = true;
        if(parabolaPoint.get(parabolaPoint.size() - 1).x <= playerRect.centerX()) reachEnd = false;
    }


    public double getEstimatedVelX(){
        return estimatedVelX;
    }


    public double getEstimatedVelY(){
        return estimatedVelY;
    }


    public void setPressed(boolean pressed){
        this.pressed = pressed;
    }


    public boolean isPressed(){
        return pressed;
    }


    public void zeroEstimatedVelocities(){
        this.estimatedVelX = PUBLIC_VAR.X_VELOCITY_INC*2;
        this.estimatedVelY = PUBLIC_VAR.Y_VELOCITY_INC * 2;
    }


    /**
     *  draw parabola points
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        for (Point point : parabolaPoint) {
            canvas.drawCircle(point.x, point.y, 10, paint);
        }
    }

    /**
     * update parabola points if player touches the screen
     */
    @Override
    public void update(){
        if(pressed){
            updateParabolaParam();
        }
        else{
            if(paint.getAlpha() >= 5) paint.setAlpha(paint.getAlpha() - 5);
            else parabolaPoint.clear();
        }
    }
}
