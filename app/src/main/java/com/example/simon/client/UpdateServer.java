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
    //public static double ratioX = DataModel.getResolutionX()/ DataModel.getRatioX();
    //public static double ratioY = DataModel.getResolutionY()/ DataModel.getRatioY();

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

    public boolean compareJson(JSONObject obj1, JSONObject obj2) {
        boolean different = false;

        //makes sure the objects are not null (which they are in the start of the game
        if (obj1 == null || obj2 == null) {
            System.out.println("Didnt send data because null");
            return true;
        }

        try {
            //checks if X is different
            if (!obj1.get("TargetX").equals(obj2.get("TargetX"))) {
                System.out.println("X is different " + obj1.get("TargetX") + " " + obj2.get("TargetX"));
                different = true;
            }
            //checks if Y is different
            else if (!obj1.get("TargetY").equals(obj2.get("TargetY"))) {
                System.out.println("Y is different " + obj1.get("TargetY") + " " + obj2.get("TargetY"));
                different = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return different;
    }

    @Override
    public void run(){

        JSONObject lastSent = null;

        while(running){

            RequestHandler handler = DataModel.getSocket();
            JSONObject sendData = new JSONObject();
            try {
                sendData.put("Datatype",12);
                sendData.put("TargetX",DataModel.getTargetX() / DataModel.getRatioX());
                sendData.put("TargetY",DataModel.getTargetY() / DataModel.getRatioY());

                if(compareJson(lastSent, sendData)){
                    System.out.println("last: " + lastSent);
                    System.out.println("New: " + sendData);
                    handler.sendData(sendData);
                    lastSent = sendData;
                    //System.out.println("Sent X: "+DataModel.getTargetX());
                    //System.out.println("Sent Y: "+DataModel.getTargetY());
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
