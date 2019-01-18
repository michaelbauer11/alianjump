package com.example.owner.ninjamp;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;

public class PUBLIC_VAR {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static Rect backgroundRect = new Rect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    public static double GRAVITY = 1;

    // x velocity increment
    final public static double X_VELOCITY_INC = 0.5;
    // y velocity increment
    final public static double Y_VELOCITY_INC = 1;

    // bigger counter means less points represented, hops between parabola points
    final public static int TIME_BETWEEN_PARABOLA_POINTS = 2;

    // All game's texts Paint - initiates in GamePanel Class
    public static Paint textPaint = new Paint();

    // Animation stuff - context for animation
    public static Context CURRENT_CONTEXT;

    // Player Rect WIDTH and HEIGHT
    // Player intersection rects initiates their values from here too
    final public static int PLAYER_SIZE = 100;

    // Scene manager reachable from anywhere
    public static SceneManager sceneManager;

    // Active level name. According to it, the GamePlayScene and its other classes
    // will know what level details to run
    public static String ACTIVE_LEVEL_NAME = "level11";

    // Numbers of Levels
    public static final int NUMBERS_OF_LEVELS = 4;

    // Current level while levels is running:
    public static String CURRENT_LEVEL_WHILE_RUNNING = "level11";

    // Main Activity Intent for passing from one activity to another:
    public static MainActivity mainActivityIntent;

    // Upgrade rect size
    final public static int upgradeSize = 50;

}
