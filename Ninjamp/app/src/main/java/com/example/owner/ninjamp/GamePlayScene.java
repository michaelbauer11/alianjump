package com.example.owner.ninjamp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class GamePlayScene implements Scene{

    private Player player;
    Point playerPoint;
    private ObstacleManager obstacleManager;
    private OnTouchForPlayer onTouchForPlayer;
    private int score = 0;

    private boolean justLeftTouch = false;

    // Helps us indicates whether player cancel jump - swipe up finger means cancel jump release
    private float touchStartsYValue;

    private boolean gameNotPaused = true;

    // GameOver rectangles
    final private Rect resetRect = new Rect(4* PUBLIC_VAR.SCREEN_WIDTH/12-100, 8* PUBLIC_VAR.SCREEN_HEIGHT/12-100, 4* PUBLIC_VAR.SCREEN_WIDTH/12+100,8* PUBLIC_VAR.SCREEN_HEIGHT/12+100);
    final private Rect homeRect = new Rect(8* PUBLIC_VAR.SCREEN_WIDTH/12-100, 8* PUBLIC_VAR.SCREEN_HEIGHT/12-100, 8* PUBLIC_VAR.SCREEN_WIDTH/12+100,8* PUBLIC_VAR.SCREEN_HEIGHT/12+100);
    final private Rect generalRect = new Rect(PUBLIC_VAR.SCREEN_WIDTH/6, PUBLIC_VAR.SCREEN_HEIGHT/6, 5* PUBLIC_VAR.SCREEN_WIDTH/6,5* PUBLIC_VAR.SCREEN_HEIGHT/6);
    private Bitmap generalRectBackground;
    private Bitmap homeBitmap;
    private Bitmap resetBitmap;
    private Paint gameOverPaint = new Paint();

    private Rect resumeRect = new Rect(PUBLIC_VAR.SCREEN_WIDTH/2-100, PUBLIC_VAR.SCREEN_HEIGHT/2-100, PUBLIC_VAR.SCREEN_WIDTH/2+100, PUBLIC_VAR.SCREEN_HEIGHT/2+100);
    private Rect pauseRect = new Rect(PUBLIC_VAR.SCREEN_WIDTH-125, 0, PUBLIC_VAR.SCREEN_WIDTH, 125);
    private Bitmap pause;
    private Bitmap resume;

    private boolean gameIsReady;

    private Paint pausePaint = new Paint();

    private MiniMissionChecker miniMissionChecker;
    private UpgradesManager upgradesManager;
    private MisslesManager misslesManager;
    private MeteoritManager meteoritManager;

    GamePlayScene(){
        obstacleManager = new ObstacleManager();
        playerPoint = new Point(PUBLIC_VAR.SCREEN_WIDTH /8, PUBLIC_VAR.SCREEN_HEIGHT/4);
        player = new Player(new Rect(0, 0, PUBLIC_VAR.PLAYER_SIZE, PUBLIC_VAR.PLAYER_SIZE), this);
        player.setPosition(playerPoint);
        onTouchForPlayer = new OnTouchForPlayer(player.getRectangle());
        gameOverPaint.setAlpha(5);

        // miniMissionChecker is getting started at reset
        miniMissionChecker = new MiniMissionChecker(player, obstacleManager);

        // Upgrades Manager
        upgradesManager = new UpgradesManager(player);
        misslesManager = new MisslesManager(player);
        meteoritManager = new MeteoritManager(player);

        generalRectBackground = ConstantsFunc.decodeSampledBitmapFromResource(PUBLIC_VAR.CURRENT_CONTEXT.getResources(), R.drawable.game_over_background, generalRect.width(), generalRect.height());
        homeBitmap = ConstantsFunc.decodeSampledBitmapFromResource(PUBLIC_VAR.CURRENT_CONTEXT.getResources(), R.drawable.game_over_home, homeRect.width(), homeRect.height());
        resetBitmap =ConstantsFunc.decodeSampledBitmapFromResource(PUBLIC_VAR.CURRENT_CONTEXT.getResources(), R.drawable.game_over_reset, resetRect.width(), resetRect.height());
        resume = ConstantsFunc.decodeSampledBitmapFromResource(PUBLIC_VAR.CURRENT_CONTEXT.getResources(), R.drawable.resume, resumeRect.width(), resumeRect.height());
        pause = ConstantsFunc.decodeSampledBitmapFromResource(PUBLIC_VAR.CURRENT_CONTEXT.getResources(), R.drawable.pause, pauseRect.width(), pauseRect.height());

        reset();
    }


    public void reset(){
        gameIsReady = false;
        if(!miniMissionChecker.getIsGameSceneRunningInMiniMission()) miniMissionChecker.start();
        obstacleManager.resetObstaclesManager();
        upgradesManager.restart();
        misslesManager.restart();
        meteoritManager.restart();
        score = 0;
        gameOverPaint.setAlpha(5);
        player.setResetForPlayer(obstacleManager,playerPoint);
        miniMissionChecker.setGameRun(true);
        gameNotPaused = true;
        gameIsReady = true;
    }


    @Override
    public void update() {
        if(!player.isDead()){
            if(gameNotPaused){
                onTouchForPlayer.update();
                obstacleManager.update(this);
                player.update();
                upgradesManager.update();
                misslesManager.update();
                meteoritManager.update();
            }
        }
        else{
            if(score > ConstantsFunc.readSharesPreference("Score-Record-"+ PUBLIC_VAR.ACTIVE_LEVEL_NAME, 0))
                ConstantsFunc.updateSharedPreference("Score-Record-"+ PUBLIC_VAR.ACTIVE_LEVEL_NAME, score);
        }

    }

    public void setJustLeftTouch(boolean justLeftTouch){
        this.justLeftTouch = justLeftTouch;
    }

    public boolean getJustLeftTouch() {
        return justLeftTouch;
    }

    public void incScore(){
        this.score++;
    }

    public ObstacleManager getObstacleManager() {
        return obstacleManager;
    }

    @Override
    public void draw(Canvas canvas) {
        if(!gameIsReady){
            ConstantsFunc.DRAW_ON_CENTER("LOADING...", canvas);
            return;
        }
        obstacleManager.draw(canvas);
        onTouchForPlayer.draw(canvas);
        player.draw(canvas);
        upgradesManager.draw(canvas);
        misslesManager.draw(canvas);
        meteoritManager.draw(canvas);
        canvas.drawText("score : " + String.valueOf(score), PUBLIC_VAR.SCREEN_WIDTH/15, PUBLIC_VAR.SCREEN_HEIGHT/15, PUBLIC_VAR.textPaint);
        if(player.isDead()){
            if(gameOverPaint.getAlpha() < 255) gameOverPaint.setAlpha(gameOverPaint.getAlpha()+10);
            canvas.drawBitmap(generalRectBackground,null, generalRect, gameOverPaint);
            canvas.drawBitmap(homeBitmap,null, homeRect, gameOverPaint);
            canvas.drawBitmap(resetBitmap,null, resetRect, gameOverPaint);
            ConstantsFunc.DRAW_ON_CENTER("Record: " + ConstantsFunc.readSharesPreference("Score-Record-"+ PUBLIC_VAR.ACTIVE_LEVEL_NAME, score), canvas);
        } else{
            if(gameNotPaused) canvas.drawBitmap(pause,null, pauseRect,pausePaint);
            else{
                canvas.drawBitmap(resume,null, resumeRect, pausePaint);
                canvas.drawBitmap(homeBitmap,null, homeRect, pausePaint);
                canvas.drawBitmap(resetBitmap,null, resetRect, pausePaint);
                for(int index=0;index<3;index++)
                    canvas.drawText(miniMissionChecker.getMissionsDescriptions(index), 30, resumeRect.top+index*300, PUBLIC_VAR.textPaint);
                }
            }
        }


    @Override
    public void receiveTouch(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!player.isDead() && !player.isInWormHall()) {
                    if(gameNotPaused){
                        onTouchForPlayer.setTouchStart();
                        touchStartsYValue = event.getY();
                        if(pauseRect.contains((int)event.getX(), (int)event.getY())) clickerHandler("pause");
                    }
                    else{
                        if(resumeRect.contains((int)event.getX(), (int)event.getY())) clickerHandler("resume");
                        if(homeRect.contains((int)event.getX(), (int)event.getY())) clickerHandler("home");
                        if(resetRect.contains((int)event.getX(), (int)event.getY())) clickerHandler("reset");
                    }
                }
                if(player.isDead()){
                    miniMissionChecker.setGameRun(false);
                    onTouchForPlayer.zeroEstimatedVelocities();
                    if(homeRect.contains((int)event.getX(), (int)event.getY())) clickerHandler("home");
                    if(resetRect.contains((int)event.getX(), (int)event.getY())) clickerHandler("reset");
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if(!player.isDead() && gameNotPaused ){
                    onTouchForPlayer.setPressed(false);
                    if(Math.abs(event.getY()-touchStartsYValue) < 200){
                        justLeftTouch = true;
                        player.setVelocity( onTouchForPlayer.getEstimatedVelX(), onTouchForPlayer.getEstimatedVelY());
                    }
                }
                break;
        }
    }

    private void clickerHandler(String buttonClicked){
        switch (buttonClicked){
            case "reset":
                reset();
                break;
            case "home":
                miniMissionChecker.setIsGameSceneRunning(false);
                //PUBLIC_VAR.sceneManager.updateActiveScene("Levels Scene");
                PUBLIC_VAR.mainActivityIntent.ChangeActivity();
                break;
            case "pause":
                gameNotPaused = false;
                break;
            case "resume":
                gameNotPaused = true;
                break;
        }
    }

    @Override
    public void terminate() {

    }
}
