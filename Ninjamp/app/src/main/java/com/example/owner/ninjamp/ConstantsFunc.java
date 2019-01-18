package com.example.owner.ninjamp;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import static android.content.Context.MODE_PRIVATE;


/**
 *
 */
public class ConstantsFunc {
    /**
     * Receive a filename , extract the file as a string, and insert it into a JSONObject
     * @param filename - file name given, to take the json from it
     * @return - Json object according to file, or null if an error accrued
     */
    public static JSONObject READ_FILE_TO_JSON_OBJ(String filename){
        try {
            InputStream is = PUBLIC_VAR.CURRENT_CONTEXT.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            return new JSONObject(json);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static void DRAW_ON_CENTER(String text, Canvas canvas){
        Rect r = new Rect();
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        PUBLIC_VAR.textPaint.setTextAlign(Paint.Align.LEFT);
        PUBLIC_VAR.textPaint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, PUBLIC_VAR.textPaint);
    }

    public static void DRAW_ON_RECT_CENTER(String text, Canvas canvas, Rect r){
        int cHeight = r.height();
        canvas.getClipBounds(r);
        int cWidth = r.width();
        PUBLIC_VAR.textPaint.setTextAlign(Paint.Align.LEFT);
        PUBLIC_VAR.textPaint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, PUBLIC_VAR.textPaint);
    }

    public static String readSharesPreference(String keyToReadFrom, String defaultValue){
        SharedPreferences prefs = PUBLIC_VAR.CURRENT_CONTEXT.getSharedPreferences("Player-Data", MODE_PRIVATE);
        return prefs.getString(keyToReadFrom, defaultValue);
    }

    public static int readSharesPreference(String keyToReadFrom, int defaultValue){
        SharedPreferences prefs = PUBLIC_VAR.CURRENT_CONTEXT.getSharedPreferences("Player-Data", MODE_PRIVATE);
        return prefs.getInt(keyToReadFrom, defaultValue);
    }

    public static void updateSharedPreference(String keyToReadFrom, String valueToUpdate){
        SharedPreferences.Editor editor = PUBLIC_VAR.CURRENT_CONTEXT.getSharedPreferences("Player-Data", MODE_PRIVATE).edit();
        editor.putString(keyToReadFrom, valueToUpdate);
        editor.apply();
    }

    public static void updateSharedPreference(String keyToReadFrom, int valueToUpdate){
        SharedPreferences.Editor editor = PUBLIC_VAR.CURRENT_CONTEXT.getSharedPreferences("Player-Data", MODE_PRIVATE).edit();
        editor.putInt(keyToReadFrom, valueToUpdate);
        editor.apply();
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 4;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResourceForJPEG(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSizeForJPEG(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSizeForJPEG(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 4096;
            }
        }
        return inSampleSize;
    }

    public static int indexOf(int value, int[] array){
        int index = 0;
        while(array[index] != value)
            index++;
        return index;
    }


    public static void WRITE_TO_JSON_FILE(String keyInJSON, String valueToInsert, String filename){
        // JSONObject jsonFromFile = READ_FILE_TO_JSON_OBJ(filename);
        //jsonFromFile.put(keyInJSON, valueToInsert);
    }

    public static void saveFile(String s){
    FileOutputStream fos = null;
        try {
            fos = PUBLIC_VAR.CURRENT_CONTEXT.openFileOutput("missions.json", MODE_PRIVATE);
            fos.write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static JSONArray readFile() {
        File sdcard = PUBLIC_VAR.CURRENT_CONTEXT.getFilesDir();
        File file = new File(sdcard, "missions.json");
        StringBuilder text = new StringBuilder();
        int linesToTake = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null && linesToTake != 3) {
                text.append(line + "\n");
                linesToTake++;
            }
            text.append("]");
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray ja = null;
        try {
            ja = new JSONArray(text.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ja;
    }
}