package com.example.owner.ninjamp;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashMap;

public class UpgradesManager {

    private Player player;

    // all the upgrades activated
    private ArrayList<Upgrade> upgrades;
    private long lastUpgradeCreation;
    private long briefBetweenUpgradeCreation;

    // stores the amounts of collected upgrades in a game
    HashMap<UpgradeType, Integer> upgradeCollected = new HashMap<>();

    private int index;
    private Upgrade tempUpgrade;

    UpgradesManager(Player player){
        lastUpgradeCreation = System.currentTimeMillis();
        upgrades = new ArrayList<>();
        this.player = player;
        for(UpgradeType upgradeType : UpgradeType.values())
            upgradeCollected.put(upgradeType,0);
    }

    public void restart(){
        lastUpgradeCreation = System.currentTimeMillis();
        upgrades.clear();
        for(UpgradeType upgradeType : UpgradeType.values())
            upgradeCollected.put(upgradeType,0);
    }

    public void update(){
        // checks is one of the upgrade collected enough - it will activate him on player
        for (UpgradeType key : upgradeCollected.keySet()){
            if(upgradeCollected.get(key) > key.collectTimes) {
                UpgradeType.WormHole.activateUpgrade(key, player);
                upgradeCollected.put(key,0);
            }
        }

        for(index = 0 ; index < upgrades.size() ; index++) { // can't iterate over a list and remove object from it, got to do it with index
            tempUpgrade = upgrades.get(index);

            if (player.getRectangle().contains(tempUpgrade.getRect().centerX(),tempUpgrade.getRect().centerY())) {
                upgradeCollected.put(tempUpgrade.getUpgradeType(),upgradeCollected.get(tempUpgrade.getUpgradeType())+1);
                upgrades.remove(tempUpgrade);
            }else if(tempUpgrade.getRect().right <= 0) upgrades.remove(tempUpgrade);
        }

        if(System.currentTimeMillis() - lastUpgradeCreation > briefBetweenUpgradeCreation) {
            upgrades.add(new Upgrade(UpgradeType.WormHole));
            lastUpgradeCreation = System.currentTimeMillis();
            briefBetweenUpgradeCreation = (Integer.valueOf(PUBLIC_VAR.CURRENT_LEVEL_WHILE_RUNNING.split("level")[1]) % 10) * 2000;
        }

        for(Upgrade upgrade : upgrades)
            upgrade.update();

    }

    public void draw(Canvas canvas){
        for(Upgrade upgrade : upgrades)
            upgrade.draw(canvas);
    }
}
