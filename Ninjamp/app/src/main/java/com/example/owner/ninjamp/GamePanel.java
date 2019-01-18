package com.example.owner.ninjamp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Owner on 11/06/2018.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    //private SceneManager sceneManager;
    private GamePlayScene gamePlayScene;

    public void setThreadToNull() { thread.setRunning(false);
    }
    public GamePanel(Context context) {
        super(context);
        PUBLIC_VAR.CURRENT_CONTEXT = context;
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        //PUBLIC_VAR.sceneManager = new SceneManager();
        gamePlayScene = new GamePlayScene();
        setFocusable(true);
        initiatesConstants();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry){
            try{
                System.out.println("checks if this func called");
                thread.setRunning(false);
                thread.join();
            }catch (Exception e){ e.printStackTrace();}
            retry = false;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //super.onTouchEvent(event);
        //PUBLIC_VAR.sceneManager.receiveTouch(event);
        gamePlayScene.receiveTouch(event);
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //PUBLIC_VAR.sceneManager.draw(canvas);
        gamePlayScene.draw(canvas);
    }


    public void update() {
        //PUBLIC_VAR.sceneManager.update();
        gamePlayScene.update();
    }

    public void initiatesConstants(){
        PUBLIC_VAR.textPaint.setColor(Color.WHITE);
        PUBLIC_VAR.textPaint.setTextSize(75);
        PUBLIC_VAR.textPaint.setStyle(Paint.Style.FILL);
        PUBLIC_VAR.textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));

        PUBLIC_VAR.backgroundRect = new Rect(0,0, PUBLIC_VAR.SCREEN_WIDTH, PUBLIC_VAR.SCREEN_HEIGHT);
    }

    @Override
    protected void finalize() throws Throwable{
        System.out.println("game panel succefully garbage collected");
        System.out.println(thread + ", ");
    }
}

