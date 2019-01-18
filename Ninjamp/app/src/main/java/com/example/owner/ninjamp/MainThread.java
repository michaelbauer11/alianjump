package com.example.owner.ninjamp;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Owner on 11/06/2018.
 */
public class MainThread extends Thread {
    private static final int MAX_FPS = 60;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    public void run(){
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long targetTime = 1000/MAX_FPS;

        while(running) {

            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch(Exception e){e.printStackTrace();}
                }
            }
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;
            try{
                if(waitTime > 0){
                    this.sleep(waitTime);
                }
            } catch (Exception e){e.printStackTrace();}

            frameCount++;
            if(frameCount == MAX_FPS){
                frameCount = 0;
            }
        }
    }

    public void setRunning(boolean running){
        this.running = running;

        /*if(!running){
            try{sleep(500);} catch (Exception e ){e.printStackTrace();
                Log.e("sleep error", "in Main Thread - setRunning func");}
            gamePanel = null;
            surfaceHolder = null;

            //android.os.Process.killProcess(android.os.Process.myPid());
        }*/
    }


    @Override
    protected void finalize() throws Throwable{
        System.out.println("Main Thread class succesfully garbage collected");
    }

}
