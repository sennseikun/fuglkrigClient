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

    /**
     * Sets the target position of the player.
     * @param x
     * @param y
     */
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

    /**
     * Runs what should happen the next tick.
     */
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

    /**
     * Sets the context for the player.
     * @param context
     */
    public void setContext(Context context){
        this.context = context;
    }

    /**
     * @return the context of the player.
     */
    public Context getContext(){
        return this.context;
    }

    /**
     * @return player id
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Sets the player id.
     * @param playerId
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     *
     * @return players X position
     */
    public double getXpos() {
        return Xpos;
    }

    /**
     * Sets the players x position
     * @param xpos
     */
    public void setXpos(double xpos) {
        Xpos = xpos;
        //Xpos = xpos * (Resources.getSystem().getDisplayMetrics().widthPixels/DataModel.getResolutionX());
    }

    /**
     * @return the players y position.
     */
    public double getYpos() {
        return Ypos;
    }

    /**
     * Sets the players y position.
     * @param ypos
     */
    public void setYpos(double ypos) {
        Ypos = ypos;
        //Ypos = ypos * (Resources.getSystem().getDisplayMetrics().heightPixels/DataModel.getResolutionY());;
    }

    /**
     * @return the players x speed.
     */
    public double getXspeed() {
        return Xspeed;
    }

    /**
     * Sets the players x speed.
     * @param xspeed
     */
    public void setXspeed(double xspeed) {
        Xspeed = xspeed;
    }

    /**
     * @return the players y speed.
     */
    public double getYspeed() { return Yspeed; }

    /**
     * Sets the players y speed.
     * @param yspeed
     */
    public void setYspeed(double yspeed) {
        Yspeed = yspeed;
    }

    /**
     * @return the angel of the player
     */
    public double getAngle() {
        return direction;
    }

    /**
     * Sets the angel of the player.
     * @param dx
     * @param dy
     */
    public void setAngle(double dx, double dy) {
        this.direction = (double) Math.atan2(dy, dx);
    }

    /**
     * Sets the playrs matrix to the given param
     * @param m
     */
    public void setMatrix(Matrix m){
        this.matrix = m;
    }

    /**
     * @return the players matrix
     */
    public Matrix getMatrix() { return matrix; }

    /**
     * Sets the direction in the x axis.
     * @param dx
     */
    public void setDx(double dx) { this.dx = dx; }

    /**
     * @return the direction in the x axis.
     */
    public double getDx() { return dx; }

    /**
     * Sets the direction in the y axis.
     * @param dy
     */
    public void setDy(double dy) { this.dy = dy; }

    /**
     * @return the direction in the y axis.
     */
    public double getDy() { return dy; }

    /**
     * Sets the bitmap of the image of the player(Bird)
     * @param pictureId
     */
    public void setBitmap(int pictureId){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        this.bitmap = BitmapFactory.decodeResource(DataModel.getGameContext().getResources(), pictureId, options);
        this.bitmap = Bitmap.createScaledBitmap(this.bitmap,(int)(this.bitmap.getWidth()*DataModel.getRatioX()*DataModel.getPlayerScale()),(int)(this.bitmap.getHeight()*DataModel.getRatioY()*DataModel.getPlayerScale()),true);
    }

    /**
     * @return of the players bird
     */
    public Bitmap getBitmap(){
        return bitmap;
    }

    /**
     * @return the boolean value if the player is alive.
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Sets the players boolean value if he is alive.
     * @param alive
     */
    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
