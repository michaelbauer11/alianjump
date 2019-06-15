package com.example.owner.ninjamp;

import android.graphics.Canvas;

import java.util.ArrayList;



public class MisslesManager {

    private Player player;

    // all the upgrades activated
    private ArrayList<Missle> missles;
    private long lastMissleCreation;
    private long briefBetweenMissleCreation;

    MisslesManager(Player player){
        lastMissleCreation = System.currentTimeMillis();
        missles = new ArrayList<>();
        this.player = player;
    }


    public void restart(){
        lastMissleCreation = System.currentTimeMillis();
        missles.clear();
    }

    public void update(){
        if(System.currentTimeMillis() - lastMissleCreation > briefBetweenMissleCreation) {
            missles.add(new Missle());
            lastMissleCreation = System.currentTimeMillis();
            briefBetweenMissleCreation = (Integer.valueOf(PUBLIC_VAR.CURRENT_LEVEL_WHILE_RUNNING.split("level")[1]) % 10) * 1000;
        }

        for(Missle missle : missles)
            missle.update();
    }

    public void draw(Canvas canvas){
        for(Missle missle : missles)
            missle.draw(canvas);
    }

}
