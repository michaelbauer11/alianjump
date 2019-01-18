package com.example.owner.ninjamp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MiniMissionChecker extends Thread {
    private Player player;
    private ObstacleManager obstacleManager;
    private Boolean gameRun = true;
    private JSONArray missionArray;

    // Mission index player reach, there is always three missions
    private int[] missionsPlayerReached = new int[3];
    // Missions current score in this time level is running:
    private JSONObject missionsCurrentRunningScore = new JSONObject();

    // Missions descriptions for display
    private String[] missionsDescriptions = new String[3];

    // Boolean var for sky jump indication
    private boolean touchTheGroundBefore = true;

    // Check if missions accrue every given milliseconds:
    private final int CHECK_MISSION_COMPLETE_PERIOD = 300;

    private boolean isGameSceneRunning;

    MiniMissionChecker(Player player, ObstacleManager obstacleManager){
        this.player = player;
        this.obstacleManager = obstacleManager;

        missionsPlayerReached[0] = ConstantsFunc.readSharesPreference("firstMissionLevelPlayerReach", 0);
        missionsPlayerReached[1] = ConstantsFunc.readSharesPreference("secondMissionLevelPlayerReach", 1);
        missionsPlayerReached[2] = ConstantsFunc.readSharesPreference("thirdMissionLevelPlayerReach", 2);

        isGameSceneRunning = false;
        updateMissionArray();
    }

    public void setIsGameSceneRunning(boolean isGameSceneRunning){this.isGameSceneRunning = isGameSceneRunning;}

    public boolean getIsGameSceneRunningInMiniMission(){return isGameSceneRunning;}

    public String getMissionsDescriptions(int index){
        try {
            return missionsDescriptions[index] + String.valueOf(missionsCurrentRunningScore.get(String.valueOf(missionsPlayerReached[index])));
        } catch (JSONException exception ){exception.printStackTrace();}
        return "";
    }

    private void updateMissionArray(){
        try {
            missionArray = ConstantsFunc.READ_FILE_TO_JSON_OBJ("missions.json").getJSONArray("missions");
            int simpleIndex = 0;
            for(int missionIndex : missionsPlayerReached){
                if (missionArray.getJSONArray(missionIndex).get(3).equals("continue")) {
                    missionsCurrentRunningScore.put(String.valueOf(missionIndex), ConstantsFunc.readSharesPreference(String.valueOf(missionIndex), 0));
                }
                else missionsCurrentRunningScore.put(String.valueOf(missionIndex), 0);
                missionsDescriptions[simpleIndex] = missionArray.getJSONArray(missionIndex).getString(4);
                simpleIndex++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getNextLevel(){
        System.out.println("nextLevel");
        int nextLevelIndex = 0;
        for(int missionIndex : missionsPlayerReached)
            nextLevelIndex = Math.max(missionIndex, nextLevelIndex);
        return nextLevelIndex+1;
    }


    private void updateMissionScoreSharedPreference(){
        try {
            for (int missionIndex : missionsPlayerReached) {
                if (missionArray.getJSONArray(missionIndex).getInt(2) <= missionsCurrentRunningScore.getInt(String.valueOf(missionIndex))){
                    System.out.println("Congrats, mission " + missionIndex + " solved");
                    int nextLevel = getNextLevel();
                    int missionIndexInArray = ConstantsFunc.indexOf(missionIndex, missionsPlayerReached);
                    switch (missionIndexInArray){
                        case 0:
                            ConstantsFunc.updateSharedPreference("firstMissionLevelPlayerReach", nextLevel);
                            break;
                        case 1:
                            ConstantsFunc.updateSharedPreference("secondMissionLevelPlayerReach", nextLevel);
                            break;
                        case 2:
                            ConstantsFunc.updateSharedPreference("thirdMissionLevelPlayerReach", nextLevel);
                            break;
                    }
                    missionsPlayerReached[missionIndexInArray] = nextLevel;
                    if (missionArray.getJSONArray(nextLevel).get(3).equals("continue"))
                        missionsCurrentRunningScore.put(String.valueOf(nextLevel),ConstantsFunc.readSharesPreference(String.valueOf(nextLevel), 0));
                    else missionsCurrentRunningScore.put(String.valueOf(nextLevel), 0);
                }
                else if (missionArray.getJSONArray(missionIndex).get(3).equals("continue")) {
                    ConstantsFunc.updateSharedPreference(String.valueOf(missionIndex), missionsCurrentRunningScore.getInt(String.valueOf(missionIndex)));
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setGameRun(boolean gameIsRun){
        gameRun = gameIsRun;
        if (gameRun) updateMissionArray();
        else updateMissionScoreSharedPreference();
    }

    private void skyJumpMission(String missionIndex){
        try {
            if(!touchTheGroundBefore && !player.isOnAir()) touchTheGroundBefore = true;
            if(PUBLIC_VAR.CURRENT_LEVEL_WHILE_RUNNING.split("level")[1].equals(missionArray.getJSONArray(Integer.valueOf(missionIndex)).getString(1))){
                if(player.getRectangle().centerY() <= 0 && touchTheGroundBefore) {
                    missionsCurrentRunningScore.put(missionIndex, (int) missionsCurrentRunningScore.get(missionIndex) + 1);
                    touchTheGroundBefore = false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        super.run();
        isGameSceneRunning = true;
        while (isGameSceneRunning)
        while (gameRun) {
            try {
                for(int missionIndex : missionsPlayerReached)
                    switch ((String) missionArray.getJSONArray(missionIndex).get(0)){
                        case "sky_jump":
                            skyJumpMission(String.valueOf(missionIndex));
                            break;
                    }
                sleep(CHECK_MISSION_COMPLETE_PERIOD);
            } catch (JSONException e) {
                Log.e("error", "could not parse json");
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
