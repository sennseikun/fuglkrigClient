package com.example.simon.client;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

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

    }

    /**
     * Sends the data to the server.
     * @param json
     */
    public void sendData(JSONObject json){
        this.json = json;
    }

    /**
     * This is the method that stops sending to the server.
     */
    public void stopSending(){
        receiver.stopThread();

        while(!receiver.isStopped()){
        }

        running = false;
    }

    /**
     * This is the method that initialize the handler for the request from and to the server.
     */
    public void init(){
        String serverName = "";

        URL url = null;

        /**
         * gets ip address to server
         */
        try {
            url = new URL("URL_HERE");
            Scanner s = new Scanner(url.openStream());
            serverName = s.nextLine();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("Server IP address", serverName);


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

    /**
     * This is the method that is running the request handler.
     * It's in a loop while it should run.
     */
    @Override
    public void run(){

        init();

        while(running) {

            String message = json.toString();
            DataModel.setSocket(this);

            if(!message.equals(lastSent)){
                try {

                    lastSent = message;

                    DataModel.setLastSent(lastSent);

                    OutputStream outToServer = client.getOutputStream();
                    DataOutputStream out = new DataOutputStream(outToServer);
                    out.writeUTF(message);

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        try {

            if(client != null){
                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
