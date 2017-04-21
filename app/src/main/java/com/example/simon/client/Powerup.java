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

    @Override
    public String toString(){
        return this.type + "";
    }

    public Bitmap getBitMap(Context context){

        String drawableName = "";
        double scale = 1;

        switch(type){
            case 0:
                drawableName = "powerup";
                scale = DataModel.getPowerupScale();
                break;
            case 1:
                drawableName = "brickwall";
                scale = DataModel.getWallScale();
                break;
            case 2:
                drawableName = "brickwall";
                scale = DataModel.getWallScale();
                break;
            case 3:
                drawableName ="birdpoop";
                scale = DataModel.getBirdPoopScale();
                break;
            default: break;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(drawableName, "drawable",context.getPackageName());

        Bitmap powerup_img = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        powerup_img = Bitmap.createScaledBitmap(powerup_img ,(int)(powerup_img.getWidth()*DataModel.getRatioX()*scale),(int)(powerup_img.getHeight()*DataModel.getRatioY()*scale), true);

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
