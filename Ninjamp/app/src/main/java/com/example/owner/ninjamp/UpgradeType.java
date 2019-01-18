package com.example.owner.ninjamp;

public enum UpgradeType {
    WormHole    (3,3,3,3000) // speed of the upgrade on the canvas(x,y), collect amount needs to activate upgrade
    ;

    final int upgradeSpeedX;
    final int upgradeSpeedY;
    final int collectTimes;
    final long upgradeActiveTime;

    UpgradeType(int upgradeSpeedX, int upgradeSpeedY,  int collectTimes, long upgradeActiveTime){
        this.upgradeSpeedX = upgradeSpeedX;
        this.upgradeSpeedY = upgradeSpeedY;
        this.collectTimes = collectTimes;
        this.upgradeActiveTime = upgradeActiveTime;
    }

    public void activateUpgrade(UpgradeType upgradeType, Player player){
        switch (upgradeType){
            case WormHole:
                player.activateWormHole(upgradeType.upgradeActiveTime, true, true);
            default:
                System.out.print("couldnt find enum");

        }
    }
}
