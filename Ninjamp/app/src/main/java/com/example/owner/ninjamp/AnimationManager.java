package com.example.owner.ninjamp;

import android.graphics.Canvas;
import android.graphics.Rect;

public class AnimationManager {
    private Animation[] animations;
    private int currentAnimationIndex = 0;

    AnimationManager(Animation[] animations) {
        this.animations = animations;
    }

    public Animation getActiveAnimation(){
        return this.animations[currentAnimationIndex];
    }

    public void playAnim(int animToPlayIndex) {
        if(animToPlayIndex != currentAnimationIndex) {
            animations[currentAnimationIndex].stop();
            animations[animToPlayIndex].play();
            animations[animToPlayIndex].setPlayedOnce(false);
            currentAnimationIndex = animToPlayIndex;
        }
    }

    public void playAnimOnce(int animToPlayIndex){
        animations[currentAnimationIndex].stop();
        animations[animToPlayIndex].play();
        animations[animToPlayIndex].setPlayedOnce(true);
        currentAnimationIndex = animToPlayIndex;
    }

    public void draw(Canvas canvas, Rect rect) {
        animations[currentAnimationIndex].draw(canvas, rect);
    }

    public void update() {
        if(animations[currentAnimationIndex].isPlaying())
            animations[currentAnimationIndex].update();
    }
}