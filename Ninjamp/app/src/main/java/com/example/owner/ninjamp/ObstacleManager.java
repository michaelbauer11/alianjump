package com.example.owner.ninjamp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by : Michael Bauer
 * Date : 3.7.18
 * This class responsible for creating updating and drawing obstacles, according to generated
 * parameters - obstaclesGap, obstacleWidth, obstacleHeight. In addition , the speed of the
 * obstacles can be generated here.
 */
public class ObstacleManager {
    private ArrayList<Obstacle> obstacles;
    private int obstaclesGap;
    private int obstacleWidth;
    private int obstacleHeight;
    private float obstaclesSpeed;
    private int obstaclesSpeedIncrement;
    private int obstaclesCurrSpeed;
    private int randomHeight;
    private int randomWidth;
    private int timeForLevel;
    private int tileBitmapId;
    private long startTime;

    private String currentLevelWhileRunning;

    private long timeLevelStartRunning;

    // For creating the obstacles wisely, we take the one who go out of screen, and take hum backward with different parameters
    // In this we reduce the removing and the creation of new obstacles. And in order to implement it we should save in variable
    // the last obstacle and the first obstacle at any time
    private int lastObstacleIndex;
    private int firstObstacleIndex;

    private int obstacleHeightRand;
    private int obstacleWidthRand;

    private boolean firstOfAkindExist;
    private int firstOfAkindIndex;

    private Paint bgPaint = new Paint();
    private boolean fadeBG;

    private Bitmap BG;
    private int bgBitmapId;



    /**
     * Constructor. Generate all the obstacles array param.
     * Initiates all parameters from the relevant level json file:
     * obstacleHeight, obstacleGap, obstacleWidth, changeGap, changeHeight, changeWidth.
     * param obstacleGap - gap between obstacles
     * param obstacleWidth - obstacle width
     * param obstacleHeight - obstacle height
     */
    ObstacleManager() {
        currentLevelWhileRunning = PUBLIC_VAR.ACTIVE_LEVEL_NAME;
        PUBLIC_VAR.CURRENT_LEVEL_WHILE_RUNNING = currentLevelWhileRunning;
        updateObstacleParam(currentLevelWhileRunning);
        obstacles = new ArrayList<>();
        startTime = System.currentTimeMillis();
        timeLevelStartRunning = startTime;
        firstObstacleIndex = 0;
        lastObstacleIndex = 0;
        populateObstacles();

        firstOfAkindExist = false;
        firstOfAkindIndex = -1;

        BG = ConstantsFunc.decodeSampledBitmapFromResourceForJPEG(PUBLIC_VAR.CURRENT_CONTEXT.getResources(),bgBitmapId, PUBLIC_VAR.SCREEN_WIDTH, PUBLIC_VAR.SCREEN_HEIGHT);
    }


    /**
     * Same as the constructor but doesn't require to create new object - with new memory unit.
     */
    public void resetObstaclesManager(){
        currentLevelWhileRunning = PUBLIC_VAR.ACTIVE_LEVEL_NAME;
        PUBLIC_VAR.CURRENT_LEVEL_WHILE_RUNNING = currentLevelWhileRunning;
        updateObstacleParam(currentLevelWhileRunning);
        obstacles = new ArrayList<>();
        lastObstacleIndex = 0;
        firstOfAkindExist = false;
        firstOfAkindIndex = -1;
        firstObstacleIndex = 0;
        startTime = System.currentTimeMillis();
        timeLevelStartRunning = startTime;
        populateObstacles();
        BG = ConstantsFunc.decodeSampledBitmapFromResourceForJPEG(PUBLIC_VAR.CURRENT_CONTEXT.getResources(),bgBitmapId, PUBLIC_VAR.SCREEN_WIDTH, PUBLIC_VAR.SCREEN_HEIGHT);
        fadeBG = false;
    }

    private void updateObstacleParam(String levelName){
        try {
            JSONObject activeLevelJSONOBJ = ConstantsFunc.READ_FILE_TO_JSON_OBJ(levelName);
            this.obstaclesGap = activeLevelJSONOBJ.getInt("Obstacle Gap");
            this.obstacleWidth = activeLevelJSONOBJ.getInt("Obstacles Width");
            this.obstacleHeight = PUBLIC_VAR.SCREEN_HEIGHT - PUBLIC_VAR.SCREEN_HEIGHT / activeLevelJSONOBJ.getInt("Obstacles Height");
            this.obstaclesSpeedIncrement = activeLevelJSONOBJ.getInt("Obstacles Speed Increment");
            this.obstaclesCurrSpeed = activeLevelJSONOBJ.getInt("Obstacles Initial Speed");
            this.randomHeight = activeLevelJSONOBJ.getInt("Random Height") * 25;
            this.randomWidth = activeLevelJSONOBJ.getInt("Random Width") * 25;
            this.timeForLevel = activeLevelJSONOBJ.getInt("Time For Level");
            this.tileBitmapId = activeLevelJSONOBJ.getInt("Tile Bitmap Id");
            this.bgBitmapId = activeLevelJSONOBJ.getInt("BG Bitmap Id");
        } catch (JSONException e) {e.printStackTrace(); System.out.println("JSON Exception In ObstaclesManager constructor");}
    }

    private void updateFirstObstacleOfNewLevel(String levelName){
        try {
            JSONObject activeLevelJSONOBJ = ConstantsFunc.READ_FILE_TO_JSON_OBJ(levelName);
            this.tileBitmapId = activeLevelJSONOBJ.getInt("Tile Bitmap Id");
            firstOfAkindExist = true;
        } catch (JSONException e) {e.printStackTrace(); System.out.println("JSON Exception In ObstaclesManager constructor");}
    }

    private void fadeBackgroundInAndOut(){
        /*if(fadeBG && bgPaint.getAlpha() > 10)
            bgPaint.setAlpha(bgPaint.getAlpha()-250);
        if(!fadeBG && bgPaint.getAlpha() < 240)
            bgPaint.setAlpha(bgPaint.getAlpha()+250);*/
    }

    public float getObstaclesSpeed(){
        return obstaclesSpeed;
    }

    /**
     * This function initiates the first obstacles array with given attributes, in the first time
     * this class is constructed
     */
    private void populateObstacles(){
        obstacleHeightRand = this.obstacleHeight;
        obstacleWidthRand = this.obstacleWidth;
        int currX = PUBLIC_VAR.SCREEN_WIDTH*2;
        while(currX > PUBLIC_VAR.SCREEN_WIDTH/3){
            obstacles.add(new Obstacle(currX-obstacleWidthRand,obstacleHeightRand,currX, PUBLIC_VAR.SCREEN_HEIGHT, tileBitmapId));
            lastObstacleIndex++;
            if(lastObstacleIndex >obstacles.size()-1) lastObstacleIndex =0;
            currX = (currX - obstacleWidthRand - obstaclesGap);
            obstacleHeightRand = obstacleHeight + ((int) (Math.random() * randomHeight*2) +1) - randomHeight;
            obstacleWidthRand = obstacleWidth + ((int) ((Math.random() * randomWidth*2) +1) - randomWidth);
        }
        // Set the first obstacle to start from the left side of the screen screen
        obstacles.get(obstacles.size()-1).getRectangle().left -= 5*PUBLIC_VAR.SCREEN_WIDTH/8;
        //obstacles.get(obstacles.size()-1).getRectangle().right = ;
    }

    /**
     * return if any of the obstacles intersects with the given rect, and returned the intersected
     * obstacle
     * @param rect - given rect to check if it intersects with any of the obstacles
     * @return the intersected obstacle or null
     */
    public Obstacle rectCollide(Rect rect){
        for(Obstacle ob : obstacles)
            if(ob.rectCollide(rect))
                return ob;
        return null;
    }


    /**
     * This function updates the obstacles every turn : its places, and if the last one
     * get out of the screen border, it  removes it from the list, and add another one.
     * This function use the object param: obstaclesGap, obstacleHeight.
     * @param gamePlayScene - given for score increment
     */
    public void update(GamePlayScene gamePlayScene){

        if(System.currentTimeMillis() - timeLevelStartRunning >= timeForLevel && Integer.valueOf(currentLevelWhileRunning.split("level")[1])%10 < 4){
            currentLevelWhileRunning = "level"+String.valueOf(Integer.valueOf(currentLevelWhileRunning.split("level")[1])+1);
            PUBLIC_VAR.CURRENT_LEVEL_WHILE_RUNNING = currentLevelWhileRunning;
            updateFirstObstacleOfNewLevel(currentLevelWhileRunning);
            // updateObstacleParam(currentLevelWhileRunning);
            timeLevelStartRunning = System.currentTimeMillis();
            fadeBG = true;
            System.out.println("level endssss");
        }

        //fade back ground in and out;
        fadeBackgroundInAndOut();

        // When level need to changed but is first object still isn't arrive to screen
        if(firstOfAkindIndex != -1 && obstacles.get(firstOfAkindIndex).getRectangle().left <= PUBLIC_VAR.SCREEN_WIDTH){
                updateObstacleParam(currentLevelWhileRunning);
                // Changing background MIDGAME
                timeLevelStartRunning = System.currentTimeMillis();
                BG = ConstantsFunc.decodeSampledBitmapFromResourceForJPEG(PUBLIC_VAR.CURRENT_CONTEXT.getResources(), bgBitmapId, PUBLIC_VAR.SCREEN_WIDTH, PUBLIC_VAR.SCREEN_HEIGHT);
                firstOfAkindIndex = -1;
                fadeBG = false;
        }

        if((int) (System.currentTimeMillis() - startTime)/3000 > obstaclesCurrSpeed) obstaclesCurrSpeed += obstaclesSpeedIncrement;
        for(Obstacle ob : obstacles){
            obstaclesSpeed = obstaclesCurrSpeed;
            ob.increaseX(obstaclesSpeed);
        }
        Obstacle lastObstacle = obstacles.get(lastObstacleIndex);
        if(lastObstacle.getRectangle().right <= 0) {

            obstacleHeightRand = obstacleHeight + ((int) (Math.random() * randomHeight*2) +1) - randomHeight;
            obstacleWidthRand = obstacleWidth + ((int) ((Math.random() * randomWidth*2) +1) - randomWidth);
            obstacles.get(lastObstacleIndex).updateObstacleParam(obstacles.get(firstObstacleIndex).getRectangle().right + obstaclesGap,obstacleHeightRand,obstacles.get(firstObstacleIndex).getRectangle().right + obstacleWidthRand + obstaclesGap, PUBLIC_VAR.SCREEN_HEIGHT, tileBitmapId);
            lastObstacleIndex = (lastObstacleIndex<1) ? obstacles.size()-1 : lastObstacleIndex-1;
            firstObstacleIndex = (firstObstacleIndex<1) ? obstacles.size()-1 : firstObstacleIndex-1;
            if(firstOfAkindExist) {
                firstOfAkindIndex = firstObstacleIndex;
                firstOfAkindExist = false;
            }
            gamePlayScene.incScore();
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(BG, null, PUBLIC_VAR.backgroundRect, bgPaint);
        for(Obstacle ob : obstacles){
            ob.draw(canvas);
        }
    }
}


