package com.example.root.tappydefender;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

/**
 * Created by root on 10/2/17.
 */

public class PlayerShip {
    private Bitmap bitmap;
    private  int x, y;
    private int speed = 0;
    private boolean boosting;
    private final int GRAVITY = -12;
    private int minY, maxY;
    private int MIN_SPEED = 1;
    private int MAX_SPEED = 20;
    private Rect hitBox;
    private int shieldStrength;

    public PlayerShip(Context context, int x, int y){
        this.x = 50;
        this.y = 50;
        speed = 0;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
        scaleBitmap(x);
        boosting = false;
        minY = 0;
        maxY = y - bitmap.getHeight();
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        shieldStrength = 2;


    }
    private void scaleBitmap(int screenx) {
        if (screenx<1000){
            bitmap =Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/3,bitmap.getHeight()/3,false);

        }
        else if(screenx<1200){
            bitmap =Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2,false);

        }
    }
    public void update(){


        if (boosting){
            speed += 2;
        }
        else {
            speed -= 5;
        }

        if(speed > MAX_SPEED)
        {
            speed = MAX_SPEED;
        }
        if (speed < MIN_SPEED)
        {
            speed = MIN_SPEED;
        }

        y -= speed + GRAVITY;

        if (y > maxY)
        {
            y = maxY;
        }
        if (y < minY)
        {
            y = minY;
        }
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
    public int getX(){
        return  x;
    }

    public int getY(){
        return  y;
    }

    public void setBoosting(){
        boosting = true;
    }

    public void stopBoosting(){
        boosting = false;
    }

    public int getSpeed() {
        return speed;
    }

    public Rect getHitBox(){
        return hitBox;
    }

    public int getShieldStrength(){
        return shieldStrength;
    }

    public void reduceShieldStrength(){
        shieldStrength--;
    }
}
