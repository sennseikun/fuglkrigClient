package com.example.simon.client;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oskar on 23.03.2017.
 */

public class GameView extends SurfaceView {
    private SurfaceHolder holder;
    private GameLoopThread glt;
    private int screenX, screenY;
    private float clickX, clickY, dx, dy;
    private ArrayList<Player> players = new ArrayList<Player>();

    //Player object for testing
    private Player testPlayer;

    public GameView(Context context){
        super(context);
        glt = GameLoopThread.getInstance();
        glt.setView(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder holder){
                glt.setRunning(true);
                glt.start();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                glt.setRunning(false);
                while (retry) {
                    try {
                        glt.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
        });

        //Testplayer
        testPlayer = new Player(this.getContext(), R.drawable.bird);
        players.add(0,testPlayer);
        testPlayer.setXpos(1080/2 - testPlayer.getBitmap().getWidth()/2);
        testPlayer.setYpos(1920/2 - testPlayer.getBitmap().getHeight()/2);

    }

    //TODO: finish instantiatePlayers() method.
    public void instantiatePlayers(List l){
        //List l is a list of skinIds to be accessed in R.drawables.
        //Instantiate this client's bird.
        players.add(0, new Player(this.getContext(), R.drawable.bird));
        //Instantiate the competitors' birds.
        for(int i = 0; i < l.size(); i++){
            //Player player = new Player(this.getContext(), #Input to access right picture in R.drawables#);
            //players.add(i, player);
        }
    }

    @Override
    public void onDraw(Canvas canvas){
        screenX = canvas.getWidth();
        screenY = canvas.getHeight();

        canvas.drawColor(Color.BLUE);
        canvas.drawBitmap(players.get(0).getBitmap(), players.get(0).getXpos()/2, players.get(0).getYpos()/2 ,new Paint());

    }

    //@Override
    public boolean onTouchEvent(MotionEvent me) {
       if(me.getAction() == MotionEvent.ACTION_DOWN){
           testPlayer.setTargetPos(me.getX(), me.getY());
       }
/*
        switch(me.getAction()){
            case  MotionEvent.ACTION_DOWN:
                testPlayer.setTargetPos(me.getX(), me.getY());
                Log.i("CLICK", "X " + me.getX());
                Log.i("CLICK","Y " + me.getY());
                break;
        }
        */
        return false;
    }

    public Player getTestPlayer() {
        return testPlayer;
    }


    /*
    TODO: Create correction method
    The game screen should be corrected according to the actual game status, i.e. positions, collisions and velocities
    shown by the client must match the server side game loop.
    */
}
