package com.example.simon.client;

import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Simon on 16.03.2017.
 */

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class RequestHandler extends Thread {

    public AsyncResponse delegate = null;
    private Socket client;
    private boolean running;
    private String lastSent;
    private JSONObject json;
    private ReceiveData receiver;
    private boolean isInit;

    public RequestHandler(JSONObject json){
        this.json = json;
        lastSent = "";
        isInit = false;

        Log.d("Called all the time","lol");
    }

    public void sendData(JSONObject json){
        this.json = json;
    }

    public void stopSending(){
        receiver.stopThread();

        while(!receiver.isStopped()){
            Log.d("ReceiveThread","STOPPING");
        }

        Log.d("RequestHandler","STOPPING");

        running = false;
    }

    public void init(){
        String serverName = "104.199.108.89";
        int port = 5555;
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            client = new Socket(serverName, port);
            System.out.println("Just connected to " + client.getRemoteSocketAddress());

            this.running = true;

            receiver = new ReceiveData(client);
            receiver.start();

            isInit = true;

        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){

        init();

        while(running) {

            String message = json.toString();
            PlayerModel.setSocket(this);

            try {
                this.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

            if(!message.equals(lastSent)){
                try {

                    Log.d("Message",message);
                    Log.d("LastMessage",lastSent);

                    lastSent = message;

                    PlayerModel.setLastSent(lastSent);

                    Log.d("RequestHandler",message);

                    OutputStream outToServer = client.getOutputStream();
                    DataOutputStream out = new DataOutputStream(outToServer);
                    out.writeUTF(message);

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        Log.d("RequestHandler","Jumping out of loop");

        try {

            if(client != null){
                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
