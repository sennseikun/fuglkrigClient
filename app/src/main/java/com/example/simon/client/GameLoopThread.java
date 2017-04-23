package com.example.simon.client;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by oskar on 23.03.2017.
 */

public class GameLoopThread extends Thread {
    private GameView view;
    private static GameLoopThread glt;
    private boolean running = false;
    static final long FPS = 60;
    private int currentFPS = 0;
    private long startTimeFPS = System.currentTimeMillis();

    public static GameLoopThread getInstance() {
        if(glt == null){
            glt = new GameLoopThread();
        }
        return glt;
    }

    /**
     * This is the method that goes while the game runs does all the calculations.
     */
    @Override
    public void run() {

        long ticksPS = 1000 / FPS;
        long startTime, sleepTime;
        while (running) {
            currentFPS++;

            if(System.currentTimeMillis() > startTimeFPS + 1000){
                DataModel.setFps(currentFPS);
                currentFPS = 0;
                startTimeFPS = System.currentTimeMillis();
            }
            Canvas c = null;
            //startTime = System.currentTimeMillis();
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    DataModel.getCurrplayer().nextTick();
                    view.draw(c);
                }

            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
            //sleepTime = ticksPS -(System.currentTimeMillis() - startTime);
            /*try {
                /*if (sleepTime > 0) {
                    sleep(sleepTime);
                }
            }
            catch (Exception e) {

            }*/
        }
        System.out.println("GameLoop is stopped");
    }

    /**
     * Sets this view to the given param
     * @param view
     */
    public void setView(GameView view){
        this.view = view;
    }

    /**
     * Setting the running state to the given param.
     * @param run
     */
    public void setRunning(boolean run){
        running = run;
    }

    /**
     * Stops the game from running.
     */
    public void stopRunning(){
        running = false;
        glt = null;
    }

}
