package com.example.owner.ninjamp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Upgrade implements GameObject {

    private Rect rect;
    private Paint paint = new Paint();
    private int speedX;
    private int speedY;
    private UpgradeType upgradeType;



    Upgrade(UpgradeType upgradeType){
        int upgradeRectTop = (int) (Math.random() * PUBLIC_VAR.SCREEN_HEIGHT/2);
        rect = new Rect(PUBLIC_VAR.SCREEN_WIDTH,upgradeRectTop,PUBLIC_VAR.SCREEN_WIDTH+PUBLIC_VAR.upgradeSize,upgradeRectTop+PUBLIC_VAR.upgradeSize);
        speedX = upgradeType.upgradeSpeedX;
        speedY = upgradeType.upgradeSpeedY;
        this.upgradeType = upgradeType;
        paint.setColor(Color.RED);
    }

    public UpgradeType getUpgradeType() { return upgradeType; }

    public Rect getRect(){ return this.rect;}

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rect, paint);
    }

    @Override
    public void update() {
        if(rect.top <= 0 || rect.bottom >= 5*PUBLIC_VAR.SCREEN_HEIGHT/8)
                speedY *= -1;
        rect.set(rect.left - speedX, rect.top + speedY,rect.right - speedX, rect.bottom + speedY);
    }

    public void activateUpgrade(Player player){
        switch (upgradeType){
            case WormHole:
                player.activateWormHole(upgradeType.upgradeActiveTime, true);
            default:
                System.out.print("couldn't find enum for upgrade activation");
        }
    }
}
