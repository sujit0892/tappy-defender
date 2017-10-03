package com.example.root.tappydefender;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by root on 10/2/17.
 */


public class TdView extends SurfaceView implements Runnable {
    volatile boolean playing;
    Thread gameThread = null;
    private PlayerShip player;
    private Canvas canvas;
    private Paint paint;
    private SurfaceHolder ourholder;
    private EenmyShip enemy1;
    private EenmyShip enemy2;
    private EenmyShip enemy3;
    private EenmyShip enemy4;
    private EenmyShip enemy5;
    ArrayList<StarDust> stardust = new ArrayList<>();

    private Context context;

    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;
    private int screenX;
    private int screenY;
    private boolean gameEnded;
    private SoundPool soundPool;
    int start = -1;
    int bump = -1;
    int destroyed = -1;
    int win = -1;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    public TdView(Context context, int x, int y) {
        super(context);
        prefs = context.getSharedPreferences("HiScores",
                context.MODE_PRIVATE);
        editor = prefs.edit();
        fastestTime = prefs.getLong("fastestTime", 1000000);
        paint = new Paint();
        ourholder = getHolder();

        this.context = context;
        int num = 40;


        screenX =x;
        screenY = y;
        startGame();

    }

    public void pause(){
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume(){

        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
     while (playing) {
         update();
         draw();
         control();
     }
    }

    private void update() {
        player.update();
        enemy1.update(player.getSpeed());
        enemy2.update(player.getSpeed());
        enemy3.update(player.getSpeed());
        for (StarDust sd : stardust){
            sd.update(player.getSpeed());
        }

        boolean hitDetected = false;

        if(Rect.intersects(player.getHitBox(),enemy1.getHitBox()))
        {   hitDetected =true;
            enemy1.setX(-100);
        }
        if(Rect.intersects(player.getHitBox(),enemy2.getHitBox()))
        {   hitDetected =true;
            enemy2.setX(-100);
        }

        if(Rect.intersects(player.getHitBox(),enemy3.getHitBox()))
        {   hitDetected =true;
            enemy3.setX(-100);
        }

        if (screenX < 1000){
            if(Rect.intersects(player.getHitBox(),enemy4.getHitBox()))
            {   hitDetected =true;
                enemy4.setX(-100);
            }
        }
        if (screenX < 1200){
            if(Rect.intersects(player.getHitBox(),enemy5.getHitBox()))
            {   hitDetected =true;
                enemy5.setX(-100);
            }
        }

        if(hitDetected)
        {  //soundPool.play(bump,1,1,0,0,1);
            player.reduceShieldStrength();
            if(player.getShieldStrength() < 0){
              //  soundPool.play(destroyed,1,1,0,0,1);
             gameEnded = true;
            }
        }
        if(!gameEnded) {
            distanceRemaining -= player.getSpeed();
            timeTaken = System.currentTimeMillis() - timeStarted;

        }

        if(distanceRemaining < 0){
            //soundPool.play(win,1,1,0,0,1);
            if(timeTaken<fastestTime){
                editor.putLong("fastestTime", timeTaken);
                editor.commit();
                fastestTime =timeTaken;
            }
            player.update();
            distanceRemaining = 0;
// Now end the game
            gameEnded = true;
        }


    }
    private void draw() {
    if(ourholder.getSurface().isValid()){
        canvas = ourholder.lockCanvas();
        canvas.drawColor(Color.argb(255,0,0,0));
        canvas.drawBitmap(player.getBitmap(),player.getX(),player.getY(),paint);
        canvas.drawBitmap(enemy3.getBitmap(),enemy3.getX(),enemy3.getY(),paint);
        canvas.drawBitmap(enemy2.getBitmap(),enemy2.getX(),enemy2.getY(),paint);
        canvas.drawBitmap(enemy1.getBitmap(),enemy1.getX(),enemy1.getY(),paint);

        if(screenX < 1000){
            canvas.drawBitmap(enemy4.getBitmap(),enemy4.getX(),enemy4.getY(),paint);
        }
        if(screenX < 1200){
            canvas.drawBitmap(enemy5.getBitmap(),enemy5.getX(),enemy5.getY(),paint);
        }
        paint.setColor(Color.argb(255, 255, 255, 255));
        for(StarDust sd : stardust){
            canvas.drawPoint(sd.getX(), sd.getY(),paint);
        }

        if(!gameEnded) {


            paint.setTextAlign(Paint.Align.LEFT);
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(25);
            canvas.drawText("Fastest:" +
                    formatTime(fastestTime) +
                    "s", 10, 20, paint);

            canvas.drawText("Time:" +
                    formatTime(timeTaken) +
                    "s", screenX / 2, 20, paint);
            canvas.drawText("Distance:" +
                    distanceRemaining / 1000 +
                    " KM", screenX / 3, screenY - 20, paint);
            canvas.drawText("Shield:" +
                    player.getShieldStrength(), 10, screenY - 20, paint);
            canvas.drawText("Speed:" +
                    player.getSpeed() * 60 +
                    " MPS", (screenX / 3) * 2, screenY - 20, paint);
        } else
        {
            paint.setTextSize(80);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Game Over", screenX/2, 100, paint);
            paint.setTextSize(25);
            canvas.drawText("Fastest:"+
                    formatTime(fastestTime) + "s", screenX/2, 160, paint);
            canvas.drawText("Time:"
                    + formatTime(timeTaken) + "s", screenX / 2, 200, paint);
            canvas.drawText("Distance remaining:" +
                    distanceRemaining/1000 + " KM",screenX/2, 240, paint);
            paint.setTextSize(80);
            canvas.drawText("Tap to replay!", screenX/2, 350, paint);
        }
        ourholder.unlockCanvasAndPost(canvas);

    }
    }
    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch (motionEvent.getAction() & motionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                if(gameEnded)
                {
                    startGame();
                }
                break;
        }
        return true;
    }

    public void startGame(){
   //   soundPool.play(start, 1, 1, 0, 0, 1);
        player = new PlayerShip(context, screenX,screenY);

        enemy1 = new EenmyShip(context, screenX,screenY);
        enemy2 = new EenmyShip(context, screenX,screenY);
        enemy3 = new EenmyShip(context, screenX,screenY);
        if(screenX>1000)
        {
            enemy4 = new EenmyShip(context, screenX,screenY);
        }
        if(screenX>1200)
        {
            enemy5 = new EenmyShip(context, screenX,screenY);
        }

        int num = 40;
        for(int i=1;i<=40;i++){
            StarDust starDust = new StarDust(screenX,screenY);
            stardust.add(starDust);
        }
        distanceRemaining = 100000;// 10 km
        timeTaken = 0;

        timeStarted = System.currentTimeMillis();
        gameEnded =false;


    }
    private String formatTime(long time){
        long seconds = (time) / 1000;
        long thousandths = (time) - (seconds * 1000);
        String strThousandths = "" + thousandths;
        if (thousandths < 100){strThousandths = "0" + thousandths;}
        if (thousandths < 10){strThousandths = "0" + strThousandths;}
        String stringTime = "" + seconds + "." + strThousandths;
        return stringTime;
    }

}
