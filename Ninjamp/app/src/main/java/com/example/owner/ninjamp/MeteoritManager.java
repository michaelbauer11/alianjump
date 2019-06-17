package com.example.owner.ninjamp;

import android.graphics.Canvas;

import java.util.ArrayList;



public class MeteoritManager {

    private Player player;

    // all the upgrades activated
    private ArrayList<Meteorit> meteorits;
    private long lastMeteoritCreation;
    private long briefBetweenMeteoritCreation;

    MeteoritManager(Player player){
        lastMeteoritCreation = System.currentTimeMillis();
        meteorits = new ArrayList<>();
        this.player = player;
    }


    public void restart(){
        lastMeteoritCreation = System.currentTimeMillis();
        meteorits.clear();
    }

    public void update(){
        if(System.currentTimeMillis() - lastMeteoritCreation > briefBetweenMeteoritCreation) {
            meteorits.add(new Meteorit());
            lastMeteoritCreation = System.currentTimeMillis();
            briefBetweenMeteoritCreation = (Integer.valueOf(PUBLIC_VAR.CURRENT_LEVEL_WHILE_RUNNING.split("level")[1]) % 10) * 1000;
        }

        for(Meteorit meteorit : meteorits)
            meteorit.update();
    }

    public void draw(Canvas canvas){
        for(Meteorit meteorit : meteorits)
            meteorit.draw(canvas);
    }

}
