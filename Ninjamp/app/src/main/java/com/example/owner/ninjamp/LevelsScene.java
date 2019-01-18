package com.example.owner.ninjamp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

public class LevelsScene implements Scene {
    private GamePlayScene gamePlayScene;
    private ArrayList<Rect> levelsBoxes;



    LevelsScene(int levelsNumbers, GamePlayScene gamePlayScene){
        this.gamePlayScene = gamePlayScene;
        this.levelsBoxes = new ArrayList<>(levelsNumbers);
        for(int i = 0; i < levelsNumbers; i++)
            levelsBoxes.add(new Rect( 300*(i+1), PUBLIC_VAR.SCREEN_HEIGHT/2 - 100 , 300*(i+1) + 200, PUBLIC_VAR.SCREEN_HEIGHT/2 + 100));
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(-1);
        Paint p = new Paint();
        p.setColor(Color.CYAN);
        for(Rect rect : levelsBoxes) {
            canvas.drawRect(rect, p);
            // ConstantsFunc.DRAW_ON_RECT_CENTER(String.valueOf(levelsBoxes.indexOf(rect)+1), canvas, rect);
            canvas.drawText(String.valueOf(levelsBoxes.indexOf(rect)+1),rect.centerX(),rect.centerY(), PUBLIC_VAR.textPaint);
        }
    }


    @Override
    public void receiveTouch(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                for(Rect rect : levelsBoxes)
                    if(rect.contains((int)event.getX(), (int)event.getY())) {
                        PUBLIC_VAR.sceneManager.updateActiveScene("Game Play Scene");
                        PUBLIC_VAR.ACTIVE_LEVEL_NAME = "level" + String.valueOf((levelsBoxes.indexOf(rect)+1)*10+1);
                        gamePlayScene.reset();
                    }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
    }


    @Override
    public void terminate() {

    }

    @Override
    public void update() {

    }
}
