package com.example.simon.client;

import android.os.CountDownTimer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thoma on 3/30/2017.
 */

public class UpdateServer extends Thread {


    public static UpdateServer updater;
    //public static double ratioX = DataModel.getResolutionX()/ DataModel.getMyScreenSizeX();
    //public static double ratioY = DataModel.getResolutionY()/ DataModel.getMyScreenSizeY();

    public static UpdateServer getInstance(){
        if(updater == null){
            updater = new UpdateServer();
        }
        return updater;
    }

    @Override
    public void run(){
        RequestHandler handler = DataModel.getSocket();
        JSONObject sendData = new JSONObject();
        try {
            sendData.put("Datatype",12);
            sendData.put("TargetX",DataModel.getTargetX());
            sendData.put("TargetY",DataModel.getTargetY());

            handler.sendData(sendData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //TODO: Change input to come from server

        try {
            this.sleep(1000/30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
