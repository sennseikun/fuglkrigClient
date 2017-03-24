package com.example.simon.client;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by thoma on 3/20/2017.
 */

public class ReceiveData extends Thread {

    Socket client;
    Boolean running;
    DataInputStream in;
    private boolean isStopped;
    private boolean isInit;

    private final String init_packet = "0";

    public ReceiveData(Socket client){
        this.client = client;
        running = true;
        isStopped = false;
        isInit = false;
    }


    public boolean isStopped(){
        return isStopped;
    }



    public void stopThread(){
        running = false;
    }

    @Override
    public void run(){

        Log.d("ReceiveData","starts running");

        while(running) {

            try {
                this.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.d("ReceiveData","running");


            try {

                String answer = "";

                InputStream inFromServer = client.getInputStream();
                in = new DataInputStream(inFromServer);

                if(in != null){
                    answer = in.readUTF();
                }

                JSONObject translated = null;
                String packet_number = "";


                try {
                    translated = new JSONObject(answer);
                    translated.getString("Datatype");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                switch(packet_number){
                    case init_packet:
                        try {
                            if(translated.getString("nickValid").toString().equals("0")){
                                PlayerModel.setNick("ERROR");
                            }
                            else{
                                PlayerModel.setNick(translated.getString("nick"));
                                PlayerModel.setP_id(Integer.parseInt(translated.getString("pId")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                }


            } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        Log.d("RecieveData","Jumping out of loop");

        isStopped = false;
    }
}
