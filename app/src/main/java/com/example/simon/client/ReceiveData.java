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

                        String name = translated.getString("Name"+i);
                        String playerCount = translated.getString("PlayerCount"+i);
                        String maxPlayerCount = translated.getString("MaxPlayers"+i);
                        String password = translated.getString("Password"+i);

                        Lobby l = new Lobby(name,playerCount,maxPlayerCount,password);

                        PlayerModel.addLobby(l);

                        System.out.println(playerCount);
                    }

                    if(PlayerModel.getLobbyList() != null){
                        System.out.println("Gets in the right place");
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = PlayerModel.getLobbyList();
                        data.execute();
                    }
                }

                //Answer when creating a new lobby

                else if(packet_number.equals("2")){
                    int value = translated.getInt("Valid");
                    PlayerModel.setGameIsValid(value);
                    System.out.println("GameLobby: Launch async");
                    AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                    data.delegate = PlayerModel.getCreateGame();
                    data.execute();
                }

                //Leave lobby

                else if(packet_number.equals("3")){

                    String lobbyname = translated.getString("LobbyID");
                    String playerCount = translated.getString("PlayerCount");
                    String name = translated.getString("PlayerName");

                    PlayerModel.updateLobby(lobbyname,playerCount);

                    System.out.println("Removed one player from: "+lobbyname);

                    if(PlayerModel.getGameLobby() != null){
                        System.out.println("GameLobby: Launch async");
                        PlayerModel.removePlayerFromLobby(name);
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = PlayerModel.getGameLobby();

                        //If the player is the one leaving

                        if(name.equals(PlayerModel.getNick())){
                            System.out.println("Leave logic should be called");
                            data.execute();
                        }

                        //If someone else is leaving

                        else{
                            data.execute("2");
                        }
                    }
                }

                //Join lobby

                else if(packet_number.equals("4")){
                    String lobbyname = translated.getString("LobbyID");
                    String playerCount = translated.getString("PlayerCount");
                    PlayerModel.updateLobby(lobbyname,playerCount);
                    System.out.println("Added one player too: "+lobbyname);

                    if(PlayerModel.getLobbyList() != null){
                        System.out.println("Going into game lobby");

                        for(int i = 0; i < Integer.parseInt(playerCount); i++){
                            String name = translated.getString("PlayerName"+i);
                            PlayerModel.addPlayerToLobby(name);
                        }

                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = PlayerModel.getLobbyList();
                        data.execute("2");
                    }

                    //IF player is in game lobby already

                    else if(PlayerModel.getGameLobby() != null){
                        String name = translated.getString("PlayerName");
                        System.out.println("Updating gamelobby");
                        PlayerModel.addPlayerToLobby(name);
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = PlayerModel.getGameLobby();
                        data.execute("2");
                    }
                }
                else if(packet_number.equals("5")){
                    if(translated.getString("Status").equals("1")){
                        System.out.println("Deleted player on server side: Worked out");
                    }
                    else{
                        System.out.println("Deleted player on server side: Did not work");
                    }
                    if(PlayerModel.getGameLobby() != null){
                        System.out.println("Gets in the right place 2");
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = PlayerModel.getGameLobby();
                        data.execute();
                    }
                    if(PlayerModel.getLobbyList() != null){
                        System.out.println("Gets in the right place 1");
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = PlayerModel.getLobbyList();
                        data.execute();
                    }
                }
                else if(packet_number.equals("8")){
                    if(translated.getString("Status").equals("1")){
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = PlayerModel.getLobbyList();
                        data.execute("4");
                    }
                    else{
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = PlayerModel.getLobbyList();
                        data.execute("3");
                    }
                }
                else if(packet_number.equals("9")){
                    AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                    data.delegate = PlayerModel.getLobbyList();
                    data.execute("5");
                }


            } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(PlayerModel.getGameLobby() != null){
            System.out.println("Connection lost: Ending gamelobby");
            AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
            data.delegate = PlayerModel.getGameLobby();
            data.execute("1");
        }
        if(PlayerModel.getLobbyList() != null){
            System.out.println("Connection lost: Ending lobby list");
            AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
            data.delegate = PlayerModel.getLobbyList();
            data.execute("1");
        }



        Log.d("RecieveData","Jumping out of loop");
        isStopped = true;
    }
}
