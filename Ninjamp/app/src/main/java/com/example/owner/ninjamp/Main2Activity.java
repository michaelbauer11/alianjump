package com.example.owner.ninjamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // make app full screen
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        findViewById(R.id.button).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                playGameActivity(1);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                playGameActivity(2);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                playGameActivity(3);
            }
        });
        findViewById(R.id.button4).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                playGameActivity(4);
            }
        });

    }

    private void playGameActivity(int level){
        PUBLIC_VAR.ACTIVE_LEVEL_NAME = "level" + String.valueOf(level*10+1);
        Intent intent = new Intent( Main2Activity.this, MainActivity.class);
        startActivity(intent); // startActivity allow you to move
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
