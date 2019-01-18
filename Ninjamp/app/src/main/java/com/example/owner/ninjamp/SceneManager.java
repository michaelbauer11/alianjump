package com.example.owner.ninjamp;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Manage all scene
 */
public class SceneManager {
    private ArrayList<Scene> scenes = new ArrayList<>();
    private int activeSceneIndex;
    private HashMap<String, Integer> scenesNameToIndex ;
    /**
     * Constructor, initiates all scene in it
     */
    SceneManager(){
        scenes.add(new GamePlayScene());
        scenes.add(new LevelsScene(PUBLIC_VAR.NUMBERS_OF_LEVELS, (GamePlayScene) scenes.get(0)));

        scenesNameToIndex = new HashMap<>();
        scenesNameToIndex.put("Game Play Scene", 0);
        scenesNameToIndex.put("Levels Scene", 1);

        updateActiveScene("Levels Scene");
    }

    public void updateActiveScene(String sceneName){
        this.activeSceneIndex = scenesNameToIndex.get(sceneName);
    }

    public void receiveTouch(MotionEvent event){
        scenes.get(activeSceneIndex).receiveTouch(event);
    }

    public void update(){
        scenes.get(activeSceneIndex).update();
    }

    public void draw(Canvas canvas){
        scenes.get(activeSceneIndex).draw(canvas);
    }

    @Override
    protected void finalize() throws Throwable{
        System.out.println("Scene Manager class succesfully garbage collected");
    }
}
