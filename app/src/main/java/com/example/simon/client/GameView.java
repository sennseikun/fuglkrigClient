package com.example.simon.client;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
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
    private ArrayList<Bitmap> buttonBitmaps = new ArrayList<Bitmap>(4);
    private ArrayList<Bitmap> greyButtonBitMaps = new ArrayList<Bitmap>();
    private ArrayList<Bitmap> regularButtonBitMaps = new ArrayList<Bitmap>();
    private boolean gameOver;

    private ArrayList<Rect> rects = new ArrayList<Rect>();
    private RequestHandler handler = DataModel.getSocket();
    private boolean isInit;
    private Bitmap currentMap;
    private Bitmap nextMap;
    private Bitmap winMap;
    private Bitmap winBackground;
    private Bitmap defeatBackground;
    double winBitMapPos = DataModel.getWinnerMapXpos()*DataModel.getRatioX() - 130;
    private Bitmap powerupIcon;
    private Paint packettextPaint = new Paint();
    private Paint gameoverPaint = new Paint();
    private Paint countdownTextPaint = new Paint();
    private String dataInfo = "";
    private boolean WinScreen;
    private boolean gameIsInited;
    private Bitmap fBrickBtn;
    private Bitmap greyFBrickBtn;
    private Bitmap rBrickBtn;
    private Bitmap greyRBrickBtn;
    private Bitmap birdpoopBtn;
    private Bitmap greyBirdpoopBtn;
    private Paint alphaPaint;

    //used to get animated birds
    private int currentBird;
    private boolean increaseBirdAnimation;
    private int framesBetweenImages;
    private int currentFrameCount;

    public GameView(Context context){
        super(context);
        currentBird = 0;
        increaseBirdAnimation = true;
        framesBetweenImages = 10;
        currentFrameCount = 0;
        gameOver = false;
        gameIsInited = false;
        isInit = false;
        WinScreen = false;
        System.out.println("Constructor called");
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
        gameoverPaint.setTextSize(48);
        gameoverPaint.setColor(Color.WHITE);

        countdownTextPaint.setTypeface(Typeface.MONOSPACE);
        countdownTextPaint.setTextSize(200);
        canvasHeight = getScreenHeight();
        canvasWidth = getScreenWidth();
        packettextPaint.setTextSize(48);
        countdownTextPaint.setTypeface(Typeface.MONOSPACE);
        countdownTextPaint.setTextSize(canvasHeight/5);

        powerupIcon = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.grey_powerup);
        powerupIcon = Bitmap.createScaledBitmap(powerupIcon, getScreenHeight()/4, getScreenHeight()/4, true);

        UpdateServer.getInstance().start();
        buttonsInit();

    }

    public void competitorsInit(HashMap hm){


        //Instantiate the competitors' birds. hm is the competitors hashmap
        for(Object id: hm.keySet()){
            System.out.println("Added competitor: " + id);
            DataModel.getCompetitors().get(id).setBitmap(R.drawable.blackbird);
            DataModel.getCompetitors().get(id).setXpos(canvasWidth/3 - DataModel.getCompetitors().get(id).getBitmap().getWidth()/2);
            DataModel.getCompetitors().get(id).setXpos(canvasHeight/3 - DataModel.getCompetitors().get(id).getBitmap().getHeight()/2);
        }
        isInit = true;
    }

    public void buttonsInit(){
        //Button 1

        System.out.println("Init buttons");


        rBrickBtn = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.bricksright);
        rBrickBtn = Bitmap.createScaledBitmap(rBrickBtn, getScreenHeight()/4, getScreenHeight()/4, true);

        regularButtonBitMaps.add(rBrickBtn);

        greyRBrickBtn = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.grayedoutbricksright);
        greyRBrickBtn = Bitmap.createScaledBitmap(greyRBrickBtn, getScreenHeight()/4, getScreenHeight()/4, true);

        greyButtonBitMaps.add(greyRBrickBtn);

        fBrickBtn = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.bricksleft);
        fBrickBtn = Bitmap.createScaledBitmap(fBrickBtn, getScreenHeight()/4, getScreenHeight()/4, true);

        regularButtonBitMaps.add(fBrickBtn);

        greyFBrickBtn = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.grayedoutbricksleft);
        greyFBrickBtn = Bitmap.createScaledBitmap(greyFBrickBtn, getScreenHeight()/4, getScreenHeight()/4, true);

        greyButtonBitMaps.add(greyFBrickBtn);

        birdpoopBtn = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.birdpoop_btn);
        birdpoopBtn = Bitmap.createScaledBitmap(birdpoopBtn, getScreenHeight()/4, getScreenHeight()/4, true);

        regularButtonBitMaps.add(birdpoopBtn);

        greyBirdpoopBtn = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.grayedoutbirdpoop_btn);
        greyBirdpoopBtn = Bitmap.createScaledBitmap(greyBirdpoopBtn, getScreenHeight()/4, getScreenHeight()/4, true);

        greyButtonBitMaps.add(greyBirdpoopBtn);

        rects.add(new Rect((int)(canvasWidth - canvasHeight/4), 0, canvasWidth, canvasHeight/4));
        rects.add(new Rect((int)(canvasWidth - canvasHeight/4), canvasHeight/4, canvasWidth, canvasHeight/2 + canvasHeight/8));
        rects.add(new Rect((int)(canvasWidth - canvasHeight/4), canvasHeight/2 - canvasHeight/4, canvasWidth, 3*canvasHeight/4));

    }

    public void recycleBitmaps(){
        for(Bitmap b: greyButtonBitMaps){
            b.recycle();
            b = null;
        }
        for(Bitmap t: regularButtonBitMaps){
            t.recycle();
            t = null;
        }
        for(Bitmap x: buttonBitmaps){
            x.recycle();
            x = null;
        }

        greyButtonBitMaps.clear();
        regularButtonBitMaps.clear();
        buttonBitmaps.clear();

        winBackground.recycle();

        winBackground = null;

        winMap.recycle();

        winMap = null;

        nextMap.recycle();

        nextMap = null;

        currentMap.recycle();

        currentMap = null;
    }

    private Bitmap getButtonBitmap(){
        switch(DataModel.getPowerup_type()){
            case 1:
                return fBrickBtn;
            case 2:
                return rBrickBtn;
            case 3:
                return birdpoopBtn;
        }
        return powerupIcon;
    }

    public void setGameOver(){
        gameOver = true;
    }

    public void initMaps(){

        System.out.println("Init maps");

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

        defeatBackground = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
        alphaPaint = new Paint();
        alphaPaint.setAlpha(42);
    }

    public void initGameOverScreens(){
        String resource = "defeattext";
        int scaler = 2;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        DataModel.getGamemusic().stop();

        if(DataModel.isVictory()){
            DataModel.getVictorymusic().start();
            resource = "wintext";
        }
        else{
            DataModel.getDefeatmusic().start();
        }

        Resources resources = getResources();
        int resourceId = resources.getIdentifier(resource, "drawable",this.getContext().getPackageName());

        winBackground = BitmapFactory.decodeResource(this.getContext().getResources(), resourceId,options);
        winBackground = Bitmap.createScaledBitmap(winBackground,(int)(winBackground.getWidth()*DataModel.getRatioX()*scaler),
                (int)(winBackground.getHeight()*DataModel.getRatioY()*scaler), true);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(Color.BLUE);

        //increase bird animation
        if (currentFrameCount >= framesBetweenImages) {
            if (increaseBirdAnimation) {
                currentBird++;
                if(currentBird == 5) {
                    increaseBirdAnimation = false;
                }
            }
            else {
                currentBird--;
                if(currentBird == 0) {
                    increaseBirdAnimation = true;
                }
            }
            //resets the counter
            currentFrameCount = 0;
        }
        else {
            currentFrameCount++;
        }



        if(DataModel.isOver()){

            if(!gameIsInited){
                initGameOverScreens();
                gameIsInited = true;
            }

            glt.setRunning(false);
            setGameOver();
            DataModel.setCompetitors(null);
            DataModel.setHostPlayer(null);
            canvas.drawBitmap(winMap,(int)winBitMapPos,0,null);
            canvas.drawBitmap(winBackground,canvasWidth/2 - winBackground.getWidth()/2,canvasHeight/2 - winBackground.getHeight()/2,null);
            canvas.drawText("Click on screen to return to lobbys", canvasWidth/2 - winBackground.getWidth()/2 - 100, canvasHeight/2 + 250, gameoverPaint);
            UpdateServer.getInstance().stopRunning();
            GameLoopThread.getInstance().stopRunning();
        }

        else{
            if(!isInit){
                competitorsInit(DataModel.getCompetitors());
                initMaps();
            }

            //Update map
            double nextBitMapPos = DataModel.getNextMapXpos()*DataModel.getRatioX();
            double currBitMapPos = DataModel.getMapXpos()*DataModel.getRatioX();
            double winBitMapPos = DataModel.getWinnerMapXpos()*DataModel.getRatioX();


            //Commented out until it starts working

            //canvas.drawBitmap(winMap,(int)winBitMapPos,0,null);
            canvas.drawBitmap(currentMap, (int)currBitMapPos,0,null);
            canvas.drawBitmap(nextMap,(int)nextBitMapPos,0,null);
            //canvas.drawBitmap(winMap,(int)winBitMapPos,0,null);

            //draw competitors
            for(Object i: DataModel.getCompetitors().keySet()){
                if(DataModel.getCompetitors().get(i).isAlive()){

                    //sets new bitmap to player
                    if (currentBird == 0) {
                        DataModel.getCompetitors().get(i).setBitmap(R.drawable.blackbird);
                    }
                    else if (currentBird == 1) {
                        DataModel.getCompetitors().get(i).setBitmap(R.drawable.blackbird1);
                    }
                    else if (currentBird == 2) {
                        DataModel.getCompetitors().get(i).setBitmap(R.drawable.blackbird2);
                    }
                    else if (currentBird == 3) {
                        DataModel.getCompetitors().get(i).setBitmap(R.drawable.blackbird3);
                    }
                    else if (currentBird == 4) {
                        DataModel.getCompetitors().get(i).setBitmap(R.drawable.blackbird4);
                    }
                    else if (currentBird == 5) {
                        DataModel.getCompetitors().get(i).setBitmap(R.drawable.blackbird5);
                    }

                    canvas.drawBitmap(DataModel.getCompetitors().get(i).getBitmap(), (int)DataModel.getCompetitors().get(i).getXpos(),
                            (int)DataModel.getCompetitors().get(i).getYpos(), null);
                }
            }

            //Draw powerups
            for(Powerup p : DataModel.getPowerups()){
                canvas.drawBitmap(p.getBitMap(this.getContext()), (int)(p.getxPos()), (int)(p.getyPos()), null);
            }


            //sets new bitmap to player
            if (currentBird == 0) {
                DataModel.getCurrplayer().setBitmap(R.drawable.bird);
            }
            else if (currentBird == 1) {
                DataModel.getCurrplayer().setBitmap(R.drawable.bird1);
            }
            else if (currentBird == 2) {
                DataModel.getCurrplayer().setBitmap(R.drawable.bird2);
            }
            else if (currentBird == 3) {
                DataModel.getCurrplayer().setBitmap(R.drawable.bird3);
            }
            else if (currentBird == 4) {
                DataModel.getCurrplayer().setBitmap(R.drawable.bird4);
            }
            else if (currentBird == 5) {
                DataModel.getCurrplayer().setBitmap(R.drawable.bird5);
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
                Bitmap button = getButtonBitmap();
                canvas.drawBitmap(button, (int)(canvasWidth - canvasHeight/4), (int) canvasHeight/2 - canvasHeight/8, null);
            }
            else{

                //Fade out screen on death
                canvas.drawARGB(125,200,200,200);
            }

            //Draw buttons




            //Draw text
            canvas.drawText(DataModel.getTextOnScreen(), canvasWidth/2 - 100, canvasHeight/2, countdownTextPaint);
            canvas.drawText("Received packets: " + dataInfo, 10, 50, packettextPaint);
            canvas.drawText("FPS: "+DataModel.getFps(), 10, 150, packettextPaint);
            canvas.drawText("Server data: " + DataModel.getServer_sent_packets(), 10, 250, packettextPaint);

        }
    }

    //@Override
    public boolean onTouchEvent(MotionEvent me) {

            if(gameOver){

                switch(me.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        UpdateServer.getInstance().stopRunning();
                        GameLoopThread.getInstance().stopRunning();
                        recycleBitmaps();
                        DataModel.setIsOver(false);
                        DataModel.setInGame(null);
                        DataModel.setIsVictory(false);

                        JSONObject sendJson = new JSONObject();

                        try {
                            sendJson.put("Datatype",14);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        handler.sendData(sendJson);
                        this.getContext().startActivity(new Intent(getContext(),LobbyActivity.class));
                        break;
                }
            }

            else {

                switch(me.getAction()){
                    case MotionEvent.ACTION_DOWN:

                    if (me.getX() < canvasWidth - canvasHeight / 4 - DataModel.getCurrplayer().getBitmap().getWidth() / 2) {
                        DataModel.setTargetX((me.getX()));
                        DataModel.setTargetY((me.getY()));

                        JSONObject inputJson = new JSONObject();
                        try {
                            inputJson.put("posX", me.getX());
                            inputJson.put("posY", me.getY());
                            inputJson.put("p_id", DataModel.getP_id());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else if (rects.get(2).contains((int) me.getX(), (int) me.getY())) {
                        Log.d("BUTTON CLICK: ", "Sendpowerup");
                        UpdateServer.getInstance().sendPowerup();
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    Log.d("Touch", "ACTION_MOVE");
                    int count = me.getPointerCount();
                    for (int i = 0; i < count; i++) {
                        if (me.getX(i) < canvasWidth - canvasHeight / 4 - DataModel.getCurrplayer().getBitmap().getWidth() / 2) {
                            DataModel.setTargetX(me.getX(i));
                            DataModel.setTargetY(me.getY(i));

                            JSONObject inputJson = new JSONObject();
                            try {
                                inputJson.put("posX", me.getX(i));
                                inputJson.put("posY", me.getY(i));
                                inputJson.put("p_id", DataModel.getP_id());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else if (rects.get(2).contains((int) me.getX(i), (int) me.getY(i))) {
                            Log.d("BUTTON CLICK: ", "Button 3 (arrow left)");

                        }
                    }
            }
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
    }


    /*
    TODO: Create correction method
    The game screen should be corrected according to the actual game status. I.e. positions, collisions and velocities
    shown by the client must match the server side game loop.
    */
}
