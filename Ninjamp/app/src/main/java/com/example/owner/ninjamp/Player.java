package com.example.owner.ninjamp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Michael bauer
 * Date: 3.7.18
 * This class has every important thing about the player.
 * Its Constructor generate the player rect size.
 * Its speed sets from the class GamePlayScene from an object of OnTouchForPlayer.
 * Every frame, the update function updates the player position according to its velocity value
 * and the PUBLIC_VAR.GRAVITY value, with relating to collision with obstacles
 *
 * Important - the sides rect detection sizes can be changed from the class attributes
 */



public class Player implements GameObject {
    private Rect rectangle;
    private double velocityX = 0;
    private double velocityY = 0;
    private boolean dead;
    private boolean onAir = false;
    private boolean inWormHall;
    private long wormHallStartingTime;
    private long wormHallActivateTime;
    final private Point wormHallPointForPlayer = new Point(100,-200);


    private GamePlayScene gamePlayScene;
    private ObstacleManager obstacleManager;
    private Rect rightRect;
    private Rect bottomRect;
    private Rect leftRect;

    // player sides colliding rect sizes:
    private int SIDE_RECT_HEIGHT = 20* PUBLIC_VAR.PLAYER_SIZE/100;
    private int SIDE_RECT_WIDTH = 2* PUBLIC_VAR.PLAYER_SIZE/100;
    private int BOTTOM_RECT_HEIGHT = PUBLIC_VAR.PLAYER_SIZE/100;
    private int BOTTOM_RECT_WIDTH = 40* PUBLIC_VAR.PLAYER_SIZE/100;

    private Animation jump;
    private Animation idle;
    private Animation run;
    private Animation landing;

    private AnimationManager animationManager;
    /**
     * Constructor
     * @param rectangle - player rect
     * @param gamePlayScene - ths gamePlayScene object
     */
    Player(Rect rectangle, GamePlayScene gamePlayScene){
        this.rectangle = rectangle;
        dead = true;
        this.gamePlayScene = gamePlayScene;
        this.obstacleManager = gamePlayScene.getObstacleManager();

        rightRect = new Rect(rectangle.right - SIDE_RECT_WIDTH/2  ,rectangle.centerY() - SIDE_RECT_HEIGHT/2,rectangle.right + SIDE_RECT_WIDTH/2,rectangle.centerY()+SIDE_RECT_HEIGHT/2);
        bottomRect = new Rect(this.rectangle.centerX() - BOTTOM_RECT_WIDTH/2, this.rectangle.bottom - BOTTOM_RECT_HEIGHT, this.rectangle.centerX() + BOTTOM_RECT_WIDTH/2, this.rectangle.bottom);
        leftRect = new Rect(this.rectangle.left - SIDE_RECT_WIDTH/2,this.rectangle.centerY()- SIDE_RECT_HEIGHT/2,this.rectangle.left + SIDE_RECT_WIDTH/2,this.rectangle.centerY()+SIDE_RECT_HEIGHT/2);


        final Resources resourcesForBitMap = PUBLIC_VAR.CURRENT_CONTEXT.getResources();

        Bitmap jump1 = ConstantsFunc.decodeSampledBitmapFromResource(resourcesForBitMap, R.drawable.jmp1, PUBLIC_VAR.PLAYER_SIZE, PUBLIC_VAR.PLAYER_SIZE);
        Bitmap jump2 = ConstantsFunc.decodeSampledBitmapFromResource(resourcesForBitMap, R.drawable.jmp2, PUBLIC_VAR.PLAYER_SIZE, PUBLIC_VAR.PLAYER_SIZE);
        Bitmap jump3 = ConstantsFunc.decodeSampledBitmapFromResource(resourcesForBitMap, R.drawable.jmp3, PUBLIC_VAR.PLAYER_SIZE, PUBLIC_VAR.PLAYER_SIZE);
        this.jump = new Animation(new Bitmap[]{jump1,jump2,jump3}, 0.4f);

        Bitmap idle1 = ConstantsFunc.decodeSampledBitmapFromResource(resourcesForBitMap, R.drawable.green__0000_idle_1, PUBLIC_VAR.PLAYER_SIZE, PUBLIC_VAR.PLAYER_SIZE);
        Bitmap idle2 = ConstantsFunc.decodeSampledBitmapFromResource(resourcesForBitMap, R.drawable.green__0001_idle_2, PUBLIC_VAR.PLAYER_SIZE, PUBLIC_VAR.PLAYER_SIZE);
        Bitmap idle3 = ConstantsFunc.decodeSampledBitmapFromResource(resourcesForBitMap, R.drawable.green__0002_idle_3, PUBLIC_VAR.PLAYER_SIZE, PUBLIC_VAR.PLAYER_SIZE);
        this.idle = new Animation(new Bitmap[]{idle1,idle2,idle3}, 0.8f);

        Bitmap landing1 = ConstantsFunc.decodeSampledBitmapFromResource(resourcesForBitMap, R.drawable.jmp4, PUBLIC_VAR.PLAYER_SIZE, PUBLIC_VAR.PLAYER_SIZE);
        this.landing = new Animation(new Bitmap[]{landing1}, 0.3f);

        animationManager = new AnimationManager(new Animation[]{this.jump, this.idle, this.landing});
    }

    public Rect getRectangle(){
        return rectangle;
    }

    public void setPosition(Point point){
        this.rectangle.set(point.x - rectangle.width()/2,point.y - rectangle.height()/2,point.x + rectangle.width()/2, point.y + rectangle.height()/2);
    }

    public void setResetForPlayer(ObstacleManager obstacleManager, Point point){
        this.dead = false;
        this.obstacleManager = obstacleManager;
        this.rectangle.set(point.x - rectangle.width()/2,point.y - rectangle.height()/2,point.x + rectangle.width()/2, point.y + rectangle.height()/2);
        this.onAir = false;
        this.inWormHall = false;
        activateWormHole(500, false,true);
    }

    private void increaseX(float x){
        rectangle.right -= x;
        rectangle.left -= x;
    }


    public void setVelocity(double velX, double velY){
        velocityX = velX;
        velocityY = velY;
        this.rectangle.bottom++;
        this.rectangle.top++;
    }

    private void zeroVelocity(){
        velocityY = 0;
        velocityX = 0;
    }

    public boolean isDead(){ return dead;}

    public boolean isOnAir(){ return onAir;}

    public void prepareToJump(){
        this.animationManager.playAnim(2);
    }

    /**
     * This function checks if the player rect intersects with any of the obstacles.
     * The sizes of the rect on the side of the player rect, which detects the collision side,
     * can be change in the class attributes
     * @return - String with the side the object intersects with
     */
    private String detectCollisionSide(){
        int playerWidth = rectangle.right - rectangle.left;
        int playerHeight = rectangle.bottom - rectangle.top;
        bottomRect.set(this.rectangle.centerX() - BOTTOM_RECT_WIDTH/2, this.rectangle.bottom - BOTTOM_RECT_HEIGHT, this.rectangle.centerX() + BOTTOM_RECT_WIDTH/2, this.rectangle.bottom);
        Obstacle bottomObstacle = obstacleManager.rectCollide(bottomRect);
        if (bottomObstacle != null){
            this.getRectangle().top = bottomObstacle.getRectangle().top - playerHeight + 1;
            this.getRectangle().bottom = bottomObstacle.getRectangle().top + 1;
            return "bottom";
        }
        rightRect.set(rectangle.right - SIDE_RECT_WIDTH/2  ,rectangle.centerY() - SIDE_RECT_HEIGHT/2,rectangle.right + SIDE_RECT_WIDTH/2,rectangle.centerY()+SIDE_RECT_HEIGHT/2);
        Obstacle rightObstacle = obstacleManager.rectCollide(rightRect);
        if (rightObstacle != null){
            this.getRectangle().left = rightObstacle.getRectangle().left - playerWidth;
            this.getRectangle().right = rightObstacle.getRectangle().left;
            return "right";
        }
        leftRect.set(this.rectangle.left - SIDE_RECT_WIDTH/2,this.rectangle.centerY()- SIDE_RECT_HEIGHT/2,this.rectangle.left + SIDE_RECT_WIDTH/2,this.rectangle.centerY()+SIDE_RECT_HEIGHT/2);
        if(obstacleManager.rectCollide(leftRect) != null) return "left";
        return "";
    }

    /**
     * Run every frame and updates player position, According to obstacles collision, gravity, and
     * player current velocity. In addition treats screen border.
     */
    @Override
    public void update() {
        PUBLIC_VAR.GRAVITY = 1;

        // Treats collision with obstacles
        switch (detectCollisionSide()){
            case "bottom":
                if (!gamePlayScene.getJustLeftTouch()) {
                    PUBLIC_VAR.GRAVITY = 0;
                    if(this.onAir) {animationManager.playAnimOnce(2); this.onAir = false;}
                    if(!this.landing.isPlaying()) animationManager.playAnim(1);
                    if (this.getRectangle().left <= 0)  this.velocityX = 0;
                    else {
                        this.zeroVelocity();
                        this.increaseX(obstacleManager.getObstaclesSpeed());
                    }
                } else{
                    animationManager.playAnimOnce(0);
                    this.onAir = true;
                    gamePlayScene.setJustLeftTouch(false);
                }
                break;
            case "right":
                if(this.velocityX > obstacleManager.getObstaclesSpeed()) velocityX *= -1;
                else this.velocityX = 0;
                break;
            case "left":
                velocityX *= -1;
                break;
        }

        // Treats screen border :
        if(this.getRectangle().right >= PUBLIC_VAR.SCREEN_WIDTH) this.velocityX = 0;
        if (this.getRectangle().bottom >= PUBLIC_VAR.SCREEN_HEIGHT || this.getRectangle().right + 1 < 0) {
            dead = true;
            this.zeroVelocity();
        }
        else velocityY += PUBLIC_VAR.GRAVITY;

        // update player position according to its velocity
        if(!inWormHall) rectangle.set((int)Math.round(rectangle.left + velocityX) ,(int)Math.round(rectangle.top + velocityY), (int)Math.round(rectangle.right + velocityX), (int)Math.round(rectangle.bottom + velocityY));
        else {
            if (System.currentTimeMillis() - wormHallStartingTime > wormHallActivateTime) {
                setPosition(gamePlayScene.playerPoint);
                inWormHall = false;
                this.zeroVelocity();
            } else setPosition(wormHallPointForPlayer);
        }
        animationManager.update();
    }



    public void activateWormHole(long activateTime, boolean activateStartWormHole, boolean activateEndWormHole){
        wormHallActivateTime = activateTime;
        this.inWormHall = true;
        wormHallStartingTime = System.currentTimeMillis();
    }

    @Override
    public void draw(Canvas canvas) {
        animationManager.draw(canvas, rectangle);
    }
}
