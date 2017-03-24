package com.example.simon.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

/**
 * Created by oskar on 23.03.2017.
 *
* The int "pictureId" in this class' constructor should refer to the bird picture's id, e.g. R.drawable.bird.
* This class will be instantiated in GameView, hence the context should be the same as the one in GameView(Context context).
*/

public class Player {
    private int playerId;
    private float Xpos, Ypos, Xspeed, Yspeed, XtargetPos, YtargetPos, dx, dy, direction;
    private Bitmap bitmap;
    private Matrix matrix;

    public Player(Context context, int pictureId){
        //TODO: set initial position and speed

        //draw bird
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), pictureId);
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
    }

    public float getYpos() {
        return Ypos;
    }

    public void setYpos(float ypos) {
        Ypos = ypos;
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

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setMatrix(Matrix m){
        this.matrix = m;
    }

    public Matrix getMatrix() { return matrix; }

    public void setDx(float dx) { this.dx = dx; }

    public float getDx() { return dx; }

    public void setDy(float dy) { this.dy = dy; }

    public float getDy() { return dy; }


}
