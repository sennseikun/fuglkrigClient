package com.example.simon.client;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
        try {
            client.close();
            isStopped = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){

        Log.d("ReceiveData","starts running");

        while(running) {

            Log.d("ReceiveData","running");

            try {

                String answer = "";

                InputStream inFromServer = client.getInputStream();
                in = new DataInputStream(inFromServer);

                answer = in.readUTF();


                JSONObject translated = null;
                String packet_number = "";


                try {
                    translated = new JSONObject(answer);
                    packet_number = translated.getString("Datatype");

                    System.out.println("DATATYPE_RECEIVED: "+translated.getString("Datatype"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    break;
                }

                System.out.println("Gets here");

                //Setup player on server side, verify name

                if(packet_number.equals("0")){
                    if(translated.getString("userValid").equals("0")){
                        System.out.println("PLAYER_NAME: ERROR");
                        PlayerModel.setNick("ERROR");
                    }
                    else{
                        System.out.println("PLAYER_NAME:" + translated.getString("nick"));
                        PlayerModel.setNick(translated.getString("nick"));
                        PlayerModel.setP_id(Integer.parseInt(translated.getString("pId")));
                    }
                }

                //Get all lobbys

                else if(packet_number.equals("1")){
                    int count = translated.getInt("Count");

                    System.out.println(count);

                    for(int i = 1; i < count + 1; i++){
                        List<String> lobby = new ArrayList<>();
                        String name = translated.getString("Name"+i);
                        String playerCount = translated.getString("PlayerCount"+i);
                        String maxPlayerCount = translated.getString("MaxPlayers"+i);
                        String password = translated.getString("Password"+i);

                        lobby.add(name);
                        lobby.add(playerCount);
                        lobby.add(maxPlayerCount);
                        lobby.add(password);

                        PlayerModel.addLobby(lobby);
                    }

                }
                else if(packet_number.equals("2")){
                    int value = translated.getInt("Valid");
                    PlayerModel.setGameIsValid(value);
                }


            } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d("RecieveData","Jumping out of loop");
        isStopped = true;
    }
}
