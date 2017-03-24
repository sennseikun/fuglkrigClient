package com.example.simon.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * Created by oskar on 23.03.2017.
 *
* The int "pictureId" in this class' constructor should refer to the bird picture's id, e.g. R.drawable.bird.
* This class will be instantiated in GameView, hence the context should be the same as the one in GameView(Context context).
*/

public class Player {
    private int playerId;
    private float Xpos, Ypos, Xspeed, Yspeed;
    private Bitmap bitmap;
    private Matrix matrix;

    public Player(Context context, int pictureId){
        //TODO: set initial position and speed

        //draw bird
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), pictureId);
        this.setMatrix(new Matrix());
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

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setMatrix(Matrix m){
        this.matrix = m;
    }

    public Matrix getMatrix() { return matrix; }
}
