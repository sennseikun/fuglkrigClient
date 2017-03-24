package com.example.simon.client;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by oskar on 23.03.2017.
 */

public class GameLoopThread extends Thread {
    private GameView view;
    private static GameLoopThread glt = new GameLoopThread();
    private boolean running = false;
    static final long FPS = 30;

    public GameLoopThread() {
    }

    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime, sleepTime;
        while (running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.onDraw(c);
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS -(System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0) {
                    sleep(sleepTime);
                }
                else {
                    sleep(10);
                }
            }
            catch (Exception e) {

            }
        }
    }

    //@Override
    public boolean onTouchEvent(MotionEvent me) {
        switch(me.getAction()){
            case  MotionEvent.ACTION_DOWN:
                //These methods may well be of no use.
                float clickX = me.getX();
                float clickY = me.getY();
                float currentXPos = view.getTestPlayer().getXpos();
                float currentYPos = view.getTestPlayer().getYpos();

                view.getTestPlayer().setXspeed(200);
                view.getTestPlayer().setYspeed(200);

                Log.i("CLICK", "X " + me.getX());
                Log.i("CLICK","Y " + me.getY());
                break;
        }
        return false;
    }

    public void setView(GameView view){
        this.view = view;
    }

    public static GameLoopThread getInstance() {
        return glt;
    }

    public void setRunning(boolean run){
        running = run;
    }

}
