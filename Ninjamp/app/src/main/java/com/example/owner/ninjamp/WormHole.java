package com.example.owner.ninjamp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class WormHole implements GameObject{

    private Animation createWormHole;
    private AnimationManager wormHoleAnimationManager;

    private Rect  wormHoleRect;// height and width are equals

    private long wormHoleStartingTime;
    private long wormHoleActivateTime;

    private boolean isDualWormHoleNeeded;

    // position to lunch back the Alien of the wormhole
    private Rect playerStartingPoint;

    WormHole(Rect playerStartingPoint){

        wormHoleRect = new Rect(0,0,PUBLIC_VAR.PLAYER_SIZE*2, PUBLIC_VAR.PLAYER_SIZE*2);
        final Resources resourcesForBitMap = PUBLIC_VAR.CURRENT_CONTEXT.getResources();

        this.playerStartingPoint = playerStartingPoint;

        Bitmap wormhole1 = ConstantsFunc.decodeSampledBitmapFromResource(resourcesForBitMap, R.drawable.wormhole1, wormHoleRect.width(), wormHoleRect.height());
        Bitmap wormhole2 = ConstantsFunc.decodeSampledBitmapFromResource(resourcesForBitMap, R.drawable.wormhole2, wormHoleRect.width(), wormHoleRect.height());
        Bitmap wormhole3 = ConstantsFunc.decodeSampledBitmapFromResource(resourcesForBitMap, R.drawable.wormhole3, wormHoleRect.width(), wormHoleRect.height());
        Bitmap wormhole4 = ConstantsFunc.decodeSampledBitmapFromResource(resourcesForBitMap, R.drawable.wormhole4, wormHoleRect.width(), wormHoleRect.height());
        Bitmap wormhole5 = ConstantsFunc.decodeSampledBitmapFromResource(resourcesForBitMap, R.drawable.wormhole5, wormHoleRect.width(), wormHoleRect.height());

        this.createWormHole = new Animation(new Bitmap[]{wormhole1, wormhole2, wormhole3, wormhole4, wormhole5, wormhole5}, 0.5f);

        this.wormHoleAnimationManager = new AnimationManager(new Animation[]{this.createWormHole});
    }

    public boolean isWormHoleExist(){
        return !(System.currentTimeMillis() - wormHoleStartingTime > wormHoleActivateTime);
    }

    public void activateDualWormHole(long activationTime){
        this.isDualWormHoleNeeded = true;
        this.wormHoleStartingTime = System.currentTimeMillis();
        this.wormHoleActivateTime = activationTime;
        this.wormHoleAnimationManager.playAnimOnce(0);
    }

    public void activateOutComeWormHole(long activationTime){
        this.wormHoleStartingTime = System.currentTimeMillis();
        this.wormHoleActivateTime = activationTime;
        this.setWormHoleRect(playerStartingPoint);
        this.wormHoleAnimationManager.playAnimOnce(0);
    }

    private boolean activateSecondWormHole(){
        return (isDualWormHoleNeeded && (System.currentTimeMillis() - wormHoleStartingTime > wormHoleActivateTime - PUBLIC_VAR.MIN_WORM_HOLE_ACTIVATION_TIME));
    }

    public void setWormHoleRect(Rect activationPlace){
        int WORMHOLE_SIZE_INCREMENT = 20;
        this.wormHoleRect.set(activationPlace.left-WORMHOLE_SIZE_INCREMENT, activationPlace.top-WORMHOLE_SIZE_INCREMENT,
                activationPlace.right+WORMHOLE_SIZE_INCREMENT, activationPlace.bottom+WORMHOLE_SIZE_INCREMENT);
    }

    @Override
    public void update(){
        if(activateSecondWormHole()){
            activateOutComeWormHole(PUBLIC_VAR.MIN_WORM_HOLE_ACTIVATION_TIME);
            this.isDualWormHoleNeeded = false;
        }
        this.wormHoleAnimationManager.update();
    }

    @Override
    public void draw(Canvas canvas) {
        if (!this.createWormHole.isDoneRunOnce()) {
            this.wormHoleAnimationManager.draw(canvas, this.wormHoleRect);
        }
    }
}