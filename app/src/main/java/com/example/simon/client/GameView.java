package com.example.simon.client;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by oskar on 23.03.2017.
 */

public class GameView extends SurfaceView {
    private SurfaceHolder holder;
    private GameLoopThread glt;
    private int canvasHeight, canvasWidth;
    private double ratioX, ratioY;
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Bitmap> buttonBitmaps = new ArrayList<Bitmap>();
    private ArrayList<Rect> rects = new ArrayList<Rect>();
    private RequestHandler handler = DataModel.getSocket();
    private boolean isInit = false;
    private Bitmap currentMap;
    private Bitmap nextMap;
    private Bitmap winMap;

    //Player object for testing
   // private Player testPlayer;

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

        buttonsInit();

        canvasHeight = getScreenHeight();
        canvasWidth = getScreenWidth();

        //initTestPlayers();

        UpdateServer.getInstance().start();
    }
/*
    public void initTestPlayers(){
        //Testplayer
        testPlayer = new Player();
        players.add(0,testPlayer);
        players.get(0).setContext(this.getContext());
        players.get(0).setBitmap(R.drawable.bird);
        //Define testPlayer's start position
        testPlayer.setXpos(canvasWidth/2 - testPlayer.getBitmap().getHeight()/2);
        testPlayer.setYpos(canvasHeight/2 - testPlayer.getBitmap().getWidth()/2);
        players.add(1, new Player());
        players.get(1).setContext(this.getContext());
        players.get(1).setBitmap(R.drawable.blackbird);
    }
*/

    public void competitorsInit(HashMap hm){
        //Instantiate the competitors' birds. hm is the competitors hashmap
        for(Object id: hm.keySet()){
            DataModel.getCompetitors().get(id).setBitmap(R.drawable.blackbird);
            DataModel.getCompetitors().get(id).setXpos(canvasWidth/3 - DataModel.getCompetitors().get(id).getBitmap().getWidth()/2);
            DataModel.getCompetitors().get(id).setXpos(canvasHeight/3 - DataModel.getCompetitors().get(id).getBitmap().getHeight()/2);
        }
        isInit = true;
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

    public void initMaps(){

        Resources resources = getResources();
        int resourceId = resources.getIdentifier(DataModel.getCurrentMapName(), "drawable",this.getContext().getPackageName());

        nextMap = BitmapFactory.decodeResource(this.getContext().getResources(), resourceId);
        currentMap = BitmapFactory.decodeResource(this.getContext().getResources(), resourceId);
        winMap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.winbackground);

        currentMap = Bitmap.createScaledBitmap(currentMap, getScreenWidth(),
                getScreenHeight(), true);
        nextMap = Bitmap.createScaledBitmap(currentMap, getScreenWidth(),
                getScreenHeight(), true);
        winMap = Bitmap.createScaledBitmap(currentMap, getScreenWidth(),
                getScreenHeight(), true);
    }

    @Override
    public void onDraw(Canvas canvas){
        canvas.drawColor(Color.BLUE);

        if(!isInit){
            competitorsInit(DataModel.getCompetitors());
            initMaps();
        }

        double nextBitMapPos = DataModel.getNextMapXpos()*DataModel.getRatioX();
        double currBitMapPos = DataModel.getMapXpos()*DataModel.getRatioX();
        double winBitMapPos = DataModel.getWinnerMapXpos()*DataModel.getRatioX();

        canvas.drawBitmap(currentMap, (int)currBitMapPos,0,null);
        canvas.drawBitmap(nextMap,(int)nextBitMapPos,0,null);
        canvas.drawBitmap(winMap,(int)winBitMapPos,0,null);


        for(Object i: DataModel.getCompetitors().keySet()){
            //canvas.drawBitmap(players.get(i).getBitmap(), players.get(i).getXpos(), players.get(i).getYpos(), null);
            canvas.drawBitmap(DataModel.getCompetitors().get(i).getBitmap(), (int) DataModel.getCompetitors().get(i).getXpos(),
                    (int) DataModel.getCompetitors().get(i).getYpos(), null);
        }

        if(DataModel.getCurrplayer().getBitmap() == null){
            DataModel.getCurrplayer().setBitmap(R.drawable.bird);
            DataModel.getCurrplayer().setXpos(canvasWidth/2 - DataModel.getCurrplayer().getBitmap().getWidth()/2);
            DataModel.getCurrplayer().setYpos(canvasHeight/2 - DataModel.getCurrplayer().getBitmap().getHeight()/2);
        }
        //canvas.drawBitmap(players.get(0).getBitmap(), players.get(0).getXpos(), players.get(0).getYpos(), null);
        canvas.drawBitmap(DataModel.getCurrplayer().getBitmap(), (int)DataModel.getCurrplayer().getXpos(),
                (int) DataModel.getCurrplayer().getYpos(), null);
        System.out.println("Xpos" + DataModel.getCurrplayer().getXpos());
        System.out.println("Ypos" + DataModel.getCurrplayer().getYpos());

        for(int i = 0; i < buttonBitmaps.size(); i++){
            canvas.drawBitmap(buttonBitmaps.get(i), (int)(0.9*canvasWidth), (int) canvasHeight*i/4, null);
        }
    }

    //@Override
    public boolean onTouchEvent(MotionEvent me) {
        switch(me.getAction()){
            case  MotionEvent.ACTION_DOWN:
                if(me.getX() < canvasWidth * 0.9 - DataModel.getCurrplayer().getBitmap().getWidth()/2) {
                    //players.get(0) is the bird belonging to this client
                    //players.get(0).setTargetPos(me.getX(), me.getY());

                    DataModel.setTargetX((me.getX()));
                    DataModel.setTargetY((me.getY()));

                    JSONObject inputJson = new JSONObject();
                    try {
                        inputJson.put("posX",me.getX());
                        inputJson.put("posY",me.getY());
                        inputJson.put("p_id",DataModel.getP_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
