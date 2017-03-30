package com.example.simon.client;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oskar on 23.03.2017.
 */

public class GameView extends SurfaceView {
    private SurfaceHolder holder;
    private GameLoopThread glt;
    private int canvasHeight, canvasWidth;
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Bitmap> buttonBitmaps = new ArrayList<Bitmap>();
    private ArrayList<Rect> rects = new ArrayList<Rect>();

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
        canvasWidth = getScreenWidth();
        canvasHeight = getScreenHeight();

        buttonsInit();
        initTestPlayers();
    }

    public void initTestPlayers(){
        //Testplayer
        testPlayer = new Player(this.getContext(), R.drawable.bird);
        players.add(0,testPlayer);
        //Define testPlayer's start position
        testPlayer.setXpos(canvasWidth/2 - testPlayer.getBitmap().getHeight()/2);
        testPlayer.setYpos(canvasHeight/2 - testPlayer.getBitmap().getWidth()/2);
        players.add(1, new Player(this.getContext(), R.drawable.blackbird));
    }

    public void playersInit(List l){
        //Instantiate this client's bird.
        players.add(0, new Player(this.getContext(), R.drawable.bird));
        //Instantiate the competitors' birds.
        for(int i = 1; i < l.size(); i++){
            players.add(0, new Player(this.getContext(), R.drawable.blackbird));
        }
    }

    public void buttonsInit(){
        //Button 1
        buttonBitmaps.add(BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.arrow_left));
        rects.add(new Rect((int)(canvasWidth*0.9), 0, canvasWidth, canvasHeight/4));
        //Button 2
        buttonBitmaps.add(BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.arrow_right));
        rects.add(new Rect((int)(canvasWidth*0.9), canvasHeight/4, canvasWidth, canvasHeight/2));
        //Button 3
        buttonBitmaps.add(BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.arrow_left));
        rects.add(new Rect((int)(canvasWidth*0.9), canvasHeight/2, canvasWidth, 3*canvasHeight/4));
        //Button 4
        buttonBitmaps.add(BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.arrow_right));
        rects.add(new Rect((int)(canvasWidth*0.9), 3*canvasHeight/4, canvasWidth, canvasHeight));
    }

    @Override
    public void onDraw(Canvas canvas){
        canvas.drawColor(Color.BLUE);
        for(int i = 0; i < players.size(); i++){
            canvas.drawBitmap(players.get(i).getBitmap(), players.get(i).getXpos(), players.get(i).getYpos(), null);
        }
        for(int i = 0; i < buttonBitmaps.size(); i++){
            canvas.drawBitmap(buttonBitmaps.get(i), (float)(0.9*canvasWidth), (float) canvasHeight*i/4, null);
        }
    }

    //@Override
    public boolean onTouchEvent(MotionEvent me) {
        switch(me.getAction()){
            case  MotionEvent.ACTION_DOWN:
                if(me.getX() < canvasWidth * 0.9 - players.get(0).getBitmap().getWidth()/2) {
                    //players.get(0) is the bird belonging to this client
                    players.get(0).setTargetPos(me.getX(), me.getY());
                }else if(rects.get(0).contains((int) me.getX(),(int) me.getY())){
                    Log.d("BUTTON CLICK: ","Button 1 (arrow left)");
                }else if (rects.get(1).contains((int) me.getX(),(int) me.getY())){
                    Log.d("BUTTON CLICK: ","Button 2 (arrow right)");
                }else if(rects.get(2).contains((int) me.getX(), (int) me.getY())){
                    Log.d("BUTTON CLICK: ", "Button 3 (arrow left)");
                }else if (rects.get(3).contains((int) me.getX(), (int) me.getY())){
                    Log.d("BUTTON CLICK: ", "Button 4 (arrow right)");
                }
                break;
        }
        return false;
    }

    public double calculateScreenWidthRatio(int x){
        return x/canvasWidth;
    }
    public double calculateScreenHeightRatio(int y){
        return y/canvasHeight;
    }

    public List<Player> getPlayers(){
        return players;
    }
    private static int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    private static int getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /*
    TODO: Create correction method
    The game screen should be corrected according to the actual game status. I.e. positions, collisions and velocities
    shown by the client must match the server side game loop.
    */
}
