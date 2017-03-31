package com.example.simon.client;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

/**
 * Created by oskar on 23.03.2017.
*/

public class Player {
    private int playerId;
    private float Xpos, Ypos, Xspeed, Yspeed, XtargetPos, YtargetPos, dx, dy, direction;
    private Bitmap bitmap;
    private Matrix matrix;
    Context context;

    public Player(){
        this.setMatrix(new Matrix());
        this.setDx(0);
        this.setDy(0);
    }



    public void setTargetPos(float x, float y) {
        dx = x - Xpos;
        dy = y - Ypos;

        //normalize
        float targetPosLength = (float) Math.sqrt(dx*dx + dy*dy);
        this.Xspeed = dx/targetPosLength;
        this.Yspeed = dy/targetPosLength;

        this.XtargetPos = x;
        this.YtargetPos = y;
    }

    public void nextTick() {
       if ((dx > 0 && Xpos >= XtargetPos) || (dx < 0 && Xpos <= XtargetPos)){
            this.Xspeed = 0;
        }
        if ((dy > 0 && Ypos >= YtargetPos) || (dy < 0 && Ypos <= YtargetPos)){
            this.Yspeed = 0;
        }

        Xpos += Xspeed * 15;
        Ypos += Yspeed * 15;


    }

    public void setContext(Context context){
        this.context = context;
    }

    public Context getContext(){
        return this.context;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public float getXpos() {
        return Xpos;
    }

    public void setXpos(float xpos) {
        Xpos = xpos;
        //Xpos = xpos * (Resources.getSystem().getDisplayMetrics().widthPixels/DataModel.getResolutionX());
    }

    public float getYpos() {
        return Ypos;
    }

    public void setYpos(float ypos) {
        //Ypos = ypos * (Resources.getSystem().getDisplayMetrics().heightPixels/DataModel.getResolutionY());;
    }

    public float getXspeed() {
        return Xspeed;
    }

    public void setXspeed(float xspeed) {
        Xspeed = xspeed;
    }

    public float getYspeed() { return Yspeed; }

    public void setYspeed(float yspeed) {
        Yspeed = yspeed;
    }

    public float getAngle() {
        return direction;
    }

    public void setAngle(float dx, float dy) {
        this.direction = (float) Math.atan2(dy, dx);
    }

    public void setMatrix(Matrix m){
        this.matrix = m;
    }

    public Matrix getMatrix() { return matrix; }

    public void setDx(float dx) { this.dx = dx; }

    public float getDx() { return dx; }

    public void setDy(float dy) { this.dy = dy; }

    public float getDy() { return dy; }

    public void setBitmap(int pictureId){

        this.bitmap = BitmapFactory.decodeResource(getContext().getResources(), pictureId);
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

}
