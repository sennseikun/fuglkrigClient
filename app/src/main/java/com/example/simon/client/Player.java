package com.example.simon.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.constraint.solver.widgets.Rectangle;

/**
 * Created by oskar on 23.03.2017.
*/

public class Player {
    private int playerId;
    private double Xpos;
    private double Ypos;
    private double Xspeed;
    private double Yspeed;
    private double XtargetPos;
    private double YtargetPos;
    private double dx;
    private double dy;
    private double direction;
    private Bitmap bitmap = null;
    private Matrix matrix;
    private Context context;
    private boolean isAlive = true;

    public Player(){
        this.setMatrix(new Matrix());
        this.setDx(0);
        this.setDy(0);
    }



    public void setTargetPos(double x, double y) {
        dx = x - Xpos;
        dy = y - Ypos;

        //normalize
        double targetPosLength = (double) Math.sqrt(dx*dx + dy*dy);
        this.Xspeed = dx/targetPosLength;
        this.Yspeed = dy/targetPosLength;

        this.XtargetPos = x;
        this.YtargetPos = y;
    }

    public void nextTick() {
       /*if ((dx > 0 && Xpos >= XtargetPos) || (dx < 0 && Xpos <= XtargetPos)){
            this.Xspeed = 0;
        }
        if ((dy > 0 && Ypos >= YtargetPos) || (dy < 0 && Ypos <= YtargetPos)){
            this.Yspeed = 0;
        }

        Xpos += Xspeed * 15;
        Ypos += Yspeed * 15;

*/
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

    public double getXpos() {
        return Xpos;
    }

    public void setXpos(double xpos) {
        Xpos = xpos;
        //Xpos = xpos * (Resources.getSystem().getDisplayMetrics().widthPixels/DataModel.getResolutionX());
    }

    public double getYpos() {
        return Ypos;
    }

    public void setYpos(double ypos) {
        Ypos = ypos;
        //Ypos = ypos * (Resources.getSystem().getDisplayMetrics().heightPixels/DataModel.getResolutionY());;
    }

    public double getXspeed() {
        return Xspeed;
    }

    public void setXspeed(double xspeed) {
        Xspeed = xspeed;
    }

    public double getYspeed() { return Yspeed; }

    public void setYspeed(double yspeed) {
        Yspeed = yspeed;
    }

    public double getAngle() {
        return direction;
    }

    public void setAngle(double dx, double dy) {
        this.direction = (double) Math.atan2(dy, dx);
    }

    public void setMatrix(Matrix m){
        this.matrix = m;
    }

    public Matrix getMatrix() { return matrix; }

    public void setDx(double dx) { this.dx = dx; }

    public double getDx() { return dx; }

    public void setDy(double dy) { this.dy = dy; }

    public double getDy() { return dy; }

    public void setBitmap(int pictureId){
        this.bitmap = BitmapFactory.decodeResource(DataModel.getGameContext().getResources(), pictureId);
        this.bitmap = Bitmap.createScaledBitmap(this.bitmap ,(int)(this.bitmap.getWidth()*DataModel.getRatioX()),(int)(this.bitmap.getHeight()*DataModel.getRatioY()), true);
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
