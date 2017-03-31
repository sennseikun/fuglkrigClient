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
    static final long FPS = 60;

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
                    DataModel.getCurrplayer().nextTick();
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
            }
            catch (Exception e) {

            }
        }
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
