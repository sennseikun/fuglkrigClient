package com.example.simon.client;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by thoma on 4/2/2017.
 */

public class Powerup {

    private int xPos = -1;
    private int yPos = -1;
    private int Id = -1;
    private int type = -1;

    public Powerup(int xPos, int yPos, int Id, int type){
        this.xPos = xPos;
        this.yPos = yPos;
        this.Id = Id;
        this.type = type;
    }

    public Bitmap getBitMap(Context context){

        String drawableName = "";

        switch(Id){
            case 0:
                drawableName = "brickwall";
                break;
            case 1:
                drawableName = "birdpoop";
                break;
            default: break;
        }



        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(drawableName, "drawable",context.getPackageName());

        Bitmap powerup_img = BitmapFactory.decodeResource(context.getResources(), resourceId);
        powerup_img = Bitmap.createScaledBitmap(powerup_img ,(int)(powerup_img.getWidth()*DataModel.getRatioX()),(int)(powerup_img.getHeight()*DataModel.getRatioY()), true);

        return powerup_img;
    }


    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getId() {
        return Id;
    }

    public int getType() {
        return type;
    }
}
