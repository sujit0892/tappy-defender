package com.example.root.tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by root on 10/2/17.
 */

public class EenmyShip {

    private int x;
    private int y;
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    private Bitmap enemy;
    private int speed = 1;
    private Rect hitBox;

    public EenmyShip(Context context, int screenx, int screenY){
        Random generator = new Random();
        int whichBitmap = generator.nextInt(3);
        switch (whichBitmap)
        {
            case 0:
                enemy = BitmapFactory.decodeResource(context.getResources(),R.drawable.enemy);
                break;
            case 1:
                enemy = BitmapFactory.decodeResource(context.getResources(),R.drawable.enemy2);
                break;
            case 2:
                enemy = BitmapFactory.decodeResource(context.getResources(),R.drawable.enemy3);
                break;

        }
        scaleBitmap(screenx);

        maxY = screenY;
        maxX = screenx;
        minY = 0;
        minX = 0;
        speed = generator.nextInt(6) + 10;
        x = screenx;
        y = generator.nextInt(maxY) - enemy.getHeight();

        hitBox = new Rect(x, y, enemy.getWidth(), enemy.getHeight());
    }

    private void scaleBitmap(int screenx) {
        if (screenx<1000){
            enemy =Bitmap.createScaledBitmap(enemy,enemy.getWidth()/3,enemy.getHeight()/3,false);

        }
        else if(screenx<1200){
            enemy =Bitmap.createScaledBitmap(enemy,enemy.getWidth()/2,enemy.getHeight()/2,false);

        }
    }

    public void update(int playership){
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + enemy.getWidth();
        hitBox.bottom = y + enemy.getHeight();
        x -= playership;
        x -= speed;
        if (x < minX - enemy.getWidth()){
            x = maxX;
            Random generator = new Random();
            speed = generator.nextInt(6) + 10;
            y =generator.nextInt(maxY) - enemy.getHeight();
        }

    }
    public Bitmap getBitmap(){
        return enemy;
    }
    public int getX(){
        return  x;
    }

    public int getY(){
        return  y;
    }

    public Rect getHitBox(){
        return hitBox;
    }
    public void setX(int x) {
        this.x = x;
    }



}
