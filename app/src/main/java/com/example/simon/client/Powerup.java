package com.example.simon.client;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

/**
 * Created by thoma on 4/2/2017.
 */

public class Powerup {

    int xPos = -1;
    int yPos = -1;
    int Id = -1;
    int type = -1;

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
        }



        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(DataModel.getCurrentMapName(), "drawable",context.getPackageName());


        return null;
    }
}
