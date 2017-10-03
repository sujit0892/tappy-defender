package com.example.root.tappydefender;

import android.content.Context;

import java.util.Random;

/**
 * Created by root on 10/2/17.
 */

public class StarDust {

    private int x,y;
    private int speed;
    private int maxX, minX;
    private int maxY, minY;
    private int MAX_SPEED, MIN_SPEED;

    public StarDust(int screenX, int screenY){
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        Random generator = new Random();
        speed = generator.nextInt(10);
        x = generator.nextInt(maxX);
        y = generator.nextInt(maxY);
    }

    public void update(int playerspeed){
        x -= playerspeed;
        x -= speed;

        if(x < 0){
            x= maxX;
            Random generator = new Random();
            speed = generator.nextInt(15);
            y = generator.nextInt(maxY);
        }



    }
    public int getX(){
        return  x;
    }

    public int getY(){
        return  y;
    }



}
