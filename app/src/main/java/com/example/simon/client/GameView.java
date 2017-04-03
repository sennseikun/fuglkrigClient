package com.example.simon.client;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by oskar on 23.03.2017.
 */

public class GameView extends SurfaceView implements AsyncResponse {
    private SurfaceHolder holder;
    private GameLoopThread glt;
    private int canvasHeight, canvasWidth;
    private ArrayList<Bitmap> buttonBitmaps = new ArrayList<Bitmap>();
    private ArrayList<Rect> rects = new ArrayList<Rect>();
    private RequestHandler handler = DataModel.getSocket();
    private boolean isInit = false;
    private Bitmap currentMap;
    private Bitmap nextMap;
    private Bitmap winMap;
    double winBitMapPos = DataModel.getWinnerMapXpos()*DataModel.getRatioX() - 130;
    private Bitmap powerupIcon;
    private Paint packettextPaint = new Paint();
    private Paint countdownTextPaint = new Paint();
    private String countdownString = "5";
    private String dataInfo = "";

    public GameView(Context context){
        super(context);
        DataModel.setGameView(this);
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

        packettextPaint.setTextSize(48);
        countdownTextPaint.setTypeface(Typeface.MONOSPACE);
        countdownTextPaint.setTextSize(200);
        canvasHeight = getScreenHeight();
        canvasWidth = getScreenWidth();

        initPowerups();
        buttonsInit();

        UpdateServer.getInstance().start();
    }

    public void initPowerups(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        powerupIcon = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.powerup,options);
        powerupIcon = Bitmap.createScaledBitmap(powerupIcon,(int)(powerupIcon.getWidth()*DataModel.getRatioX()*DataModel.getPowerupScale()),
                (int)(powerupIcon.getHeight()*DataModel.getRatioY()*DataModel.getPowerupScale()), true);
    }

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
        buttonBitmaps.add(BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.bricksleft));
        rects.add(new Rect((int)(canvasWidth - canvasHeight/4), 0, canvasWidth, canvasHeight/4));
        //Button 2
        buttonBitmaps.add(BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.bricksright));
        rects.add(new Rect((int)(canvasWidth - canvasHeight/4), canvasHeight/4, canvasWidth, canvasHeight/2));
        //Button 3
        buttonBitmaps.add(BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.birdpoop));
        rects.add(new Rect((int)(canvasWidth - canvasHeight/4), canvasHeight/2, canvasWidth, 3*canvasHeight/4));
        //Button 4
        buttonBitmaps.add(BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.arrow_right));
        rects.add(new Rect((int)(canvasWidth - canvasHeight/4), 3*canvasHeight/4, canvasWidth, canvasHeight));

        for(int i = 0; i < buttonBitmaps.size(); i++){
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(buttonBitmaps.get(i), getScreenHeight()/4, getScreenHeight()/4, true);
            buttonBitmaps.set(i, scaledBitmap);
        }
    }

    public void initMaps(){

        Resources resources = getResources();
        int resourceId = resources.getIdentifier(DataModel.getCurrentMapName(), "drawable",this.getContext().getPackageName());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        nextMap = BitmapFactory.decodeResource(this.getContext().getResources(), resourceId,options);
        currentMap = BitmapFactory.decodeResource(this.getContext().getResources(), resourceId,options);
        winMap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.winbackground,options);

        currentMap = Bitmap.createScaledBitmap(currentMap,(int)(currentMap.getWidth()*DataModel.getRatioX()),
                (int)(currentMap.getHeight()*DataModel.getRatioY()), true);
        nextMap = Bitmap.createScaledBitmap(nextMap,(int)(nextMap.getWidth()*DataModel.getRatioX()),
                (int)(nextMap.getHeight()*DataModel.getRatioY()), true);
        winMap = Bitmap.createScaledBitmap(winMap,(int)(winMap.getWidth()*DataModel.getRatioX()),
                (int)(winMap.getHeight()*DataModel.getRatioY()), true);

        currentMap = Bitmap.createScaledBitmap(currentMap, getScreenWidth(),
                getScreenHeight(), true);
        nextMap = Bitmap.createScaledBitmap(currentMap, getScreenWidth(),
                getScreenHeight(), true);
        winMap = Bitmap.createScaledBitmap(winMap, getScreenWidth() + 130,
                getScreenHeight(), true);
    }

    @Override
    public void onDraw(Canvas canvas){
        canvas.drawColor(Color.BLUE);

        if(!isInit){
            competitorsInit(DataModel.getCompetitors());
            initMaps();
        }

        //Update map
        double nextBitMapPos = DataModel.getNextMapXpos()*DataModel.getRatioX();
        double currBitMapPos = DataModel.getMapXpos()*DataModel.getRatioX();

        canvas.drawBitmap(winMap,(int)winBitMapPos,0,null);
        canvas.drawBitmap(currentMap, (int)currBitMapPos,0,null);
        canvas.drawBitmap(nextMap,(int)nextBitMapPos,0,null);

        //draw competitors
        for(Object i: DataModel.getCompetitors().keySet()){
            if(DataModel.getCompetitors().get(i).isAlive()){
                canvas.drawBitmap(DataModel.getCompetitors().get(i).getBitmap(), (int)DataModel.getCompetitors().get(i).getXpos(),
                        (int)DataModel.getCompetitors().get(i).getYpos(), null);
            }
        }

        //Draw powerups
        for(int i = 0; i < DataModel.getPowerups().size(); i++){
            canvas.drawBitmap(DataModel.getPowerups().get(i).getBitMap(this.getContext()), DataModel.getPowerups().get(i).getxPos(),
                    DataModel.getPowerups().get(i).getyPos(), null);
        }

        //draw current player, i.e. me-player
        if(DataModel.getCurrplayer().getBitmap() == null){
            DataModel.getCurrplayer().setBitmap(R.drawable.bird);
            DataModel.getCurrplayer().setXpos(canvasWidth/2 - DataModel.getCurrplayer().getBitmap().getWidth()/2);
            DataModel.getCurrplayer().setYpos(canvasHeight/2 - DataModel.getCurrplayer().getBitmap().getHeight()/2);
        }

        if(DataModel.getCurrplayer().isAlive()){
            canvas.drawBitmap(DataModel.getCurrplayer().getBitmap(), (int)DataModel.getCurrplayer().getXpos(),
                    (int) DataModel.getCurrplayer().getYpos(), null);
            System.out.println("Xpos" + DataModel.getCurrplayer().getXpos());
            System.out.println("Ypos" + DataModel.getCurrplayer().getYpos());
        }

        //Draw buttons
        for(int i = 0; i < buttonBitmaps.size(); i++){
            canvas.drawBitmap(buttonBitmaps.get(i), (int)(canvasWidth - canvasHeight/4), (int) canvasHeight*i/4, null);
        }

        //Draw text
        canvas.drawText(countdownString, canvasWidth/2, canvasHeight/2, countdownTextPaint);
        canvas.drawText(dataInfo, 10, 50, packettextPaint);
    }

    //@Override
    public boolean onTouchEvent(MotionEvent me) {
        switch(me.getAction()){
            case  MotionEvent.ACTION_DOWN:
                if(me.getX() < canvasWidth - canvasHeight/4 - DataModel.getCurrplayer().getBitmap().getWidth()/2) {
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

    private static int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    private static int getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void processFinish(String output) {
        dataInfo = output;
        System.out.println("DATA_INFO: "+ dataInfo);
    }


    /*
    TODO: Create correction method
    The game screen should be corrected according to the actual game status. I.e. positions, collisions and velocities
    shown by the client must match the server side game loop.
    */
}
