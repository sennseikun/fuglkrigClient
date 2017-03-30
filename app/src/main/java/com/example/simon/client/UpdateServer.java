package com.example.simon.client;

import android.os.CountDownTimer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thoma on 3/30/2017.
 */

public class UpdateServer extends Thread {

    private static boolean running = true;


    public static UpdateServer updater;

    public static UpdateServer getInstance(){
        if(updater == null){
            updater = new UpdateServer();
        }
        return updater;
    }

    public static boolean getRunning(){
        return running;
    }

    public static void stopRunning(){
        running = false;
    }

    @Override
    public void run(){

        JSONObject lastSent = null;

        while(running){

            RequestHandler handler = DataModel.getSocket();
            JSONObject sendData = new JSONObject();
            try {
                sendData.put("Datatype",12);
                sendData.put("TargetX",DataModel.getTargetX());
                sendData.put("TargetY",DataModel.getTargetY());

                if(lastSent != sendData){
                    handler.sendData(sendData);
                    lastSent = sendData;
                    System.out.println("Sent X: "+DataModel.getTargetX());
                    System.out.println("Sent Y: "+DataModel.getTargetY());
                }

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

}
