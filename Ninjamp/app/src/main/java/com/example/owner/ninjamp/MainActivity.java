package com.example.owner.ninjamp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private GamePanel gamePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        PUBLIC_VAR.SCREEN_HEIGHT = dm.heightPixels;
        PUBLIC_VAR.SCREEN_WIDTH = dm.widthPixels;

        PUBLIC_VAR.mainActivityIntent = MainActivity.this;
        gamePanel = new GamePanel(this);
        setContentView(gamePanel);
    }




    public void ChangeActivity(){
        gamePanel.setThreadToNull();

        Intent intent = new Intent( MainActivity.this, Main2Activity.class);
        startActivity(intent, null); // startActivity allow you to move
        finish();
        System.out.println("end cleaning.....");
        }
}
