package com.example.simon.client;

import android.provider.ContactsContract;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
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
   // private static int ratioX = DataModel.getRatioX() / DataModel.getResolutionX();
   // private static int ratioY = DataModel.getRatioY() / DataModel.getResolutionY();

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
                        DataModel.setNick("ERROR");
                    }
                    else{
                        System.out.println("PLAYER_NAME:" + translated.getString("nick"));
                        DataModel.setNick(translated.getString("nick"));
                        DataModel.setP_id(translated.getInt("pId"));
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

                        DataModel.addLobby(l);

                        System.out.println(playerCount);
                    }

                    if(DataModel.getLobbyList() != null){
                        System.out.println("Gets in the right place");
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = DataModel.getLobbyList();
                        data.execute();
                    }
                }

                //Answer when creating a new lobby

                else if(packet_number.equals("2")){
                    int value = translated.getInt("Valid");
                    DataModel.setGameIsValid(value);
                    DataModel.setHostPlayer(DataModel.getNick());
                    System.out.println("GameLobby: Launch async");
                    AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                    data.delegate = DataModel.getCreateGame();
                    data.execute();
                }

                //Leave lobby

                else if(packet_number.equals("3")){

                    String lobbyname = translated.getString("LobbyID");
                    String playerCount = translated.getString("PlayerCount");
                    String name = translated.getString("PlayerName");

                    String hostPlayer = translated.getString("hostPlayer");
                    DataModel.setHostPlayer(hostPlayer);

                    DataModel.updateLobby(lobbyname,playerCount);

                    System.out.println("Removed one player from: "+lobbyname);

                    if(DataModel.getGameLobby() != null){
                        System.out.println("GameLobby: Launch async");
                        DataModel.removePlayerFromLobby(name);
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = DataModel.getGameLobby();

                        //If the player is the one leaving

                        if(name.equals(DataModel.getNick())){
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

                    String hostPlayer = translated.getString("hostPlayer");
                    DataModel.setHostPlayer(hostPlayer);

                    DataModel.updateLobby(lobbyname,playerCount);
                    System.out.println("Added one player too: "+lobbyname);

                    if(DataModel.getLobbyList() != null){
                        System.out.println("Going into game lobby");

                        for(int i = 0; i < Integer.parseInt(playerCount); i++){
                            String name = translated.getString("PlayerName"+i);
                            DataModel.addPlayerToLobby(name);
                        }

                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = DataModel.getLobbyList();
                        data.execute("2");
                    }

                    //IF player is in game lobby already

                    else if(DataModel.getGameLobby() != null){
                        String name = translated.getString("PlayerName");
                        System.out.println("Updating gamelobby");
                        DataModel.addPlayerToLobby(name);
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = DataModel.getGameLobby();
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
                    if(DataModel.getGameLobby() != null){
                        System.out.println("Gets in the right place 2");
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = DataModel.getGameLobby();
                        data.execute();
                    }
                    if(DataModel.getLobbyList() != null){
                        System.out.println("Gets in the right place 1");
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = DataModel.getLobbyList();
                        data.execute();
                    }
                }
                else if(packet_number.equals("8")){
                    if(translated.getString("Status").equals("1")){
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = DataModel.getLobbyList();
                        data.execute("4");
                    }
                    else{
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = DataModel.getLobbyList();
                        data.execute("3");
                    }
                }

                //Lobby is full package

                else if(packet_number.equals("9")){
                    AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                    data.delegate = DataModel.getLobbyList();
                    data.execute("5");
                }

                //Game doesn't exist package

                else if(packet_number.equals("20")){
                    AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                    data.delegate = DataModel.getLobbyList();
                    data.execute("6");
                }

                //Game is started package

                else if(packet_number.equals("21")){
                    AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                    data.delegate = DataModel.getLobbyList();
                    data.execute("7");
                }

                //Begin game package

                else if(packet_number.equals("14")){

                    System.out.println("PACKAGE 14: " +translated);

                    int width = translated.getInt("Width");
                    int height = translated.getInt("Height");

                    System.out.println("Height from server " + height);
                    System.out.println("Width from server " + width);

                    System.out.println("Screenwidth: " + DataModel.getScreenWidth());
                    System.out.println("ScreenHeight: " + DataModel.getScreenHeight());

                    DataModel.setResolutionX(width);
                    DataModel.setResolutionY(height);

                    double ratioX = DataModel.getScreenWidth()/width;
                    double ratioY = DataModel.getScreenHeight()/height;

                    System.out.println("RatioX set: " + ratioX);
                    System.out.println("RatioY set: "+ ratioY);

                    DataModel.setRatioX(ratioX);
                    DataModel.setRatioY(ratioY);

                    JSONArray players = translated.getJSONArray("Players");

                    for(int i = 0; i < players.length(); i++){
                        JSONObject player = players.getJSONObject(i);

                        Integer pid = player.getInt("PlayerID");

                        if(pid == DataModel.getP_id()){
                            Player me = new Player();
                            DataModel.setCurrplayer(me);
                            System.out.println("currplayer added");
                        }
                        else{
                            DataModel.addCompetitor(pid, new Player());
                            System.out.println("Competitors added");
                        }
                    }


                    AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                    data.delegate = DataModel.getGameLobby();
                    data.execute("3");
                }

                //Update game package

                else if(packet_number.equals("15")){

                    System.out.println("PACKAGE 15: " +translated);

                    HashMap<Integer,Player> playerMap = DataModel.getCompetitors();
                    JSONArray jsonArray = translated.getJSONArray("Players");

                    for(int i = 0; i < jsonArray.length(); i++){

                        JSONObject compJSON = jsonArray.getJSONObject(i);
                        Integer id = compJSON.getInt("PlayerID");

                        int playerX = compJSON.getInt("PosX");
                        int playerY = compJSON.getInt("PosY");

                        if(id == DataModel.getP_id()){
                            Player me = DataModel.getCurrplayer();

                            System.out.println("RatioX receive: " + DataModel.getRatioX());

                            me.setXpos(playerX*DataModel.getRatioX());
                            me.setYpos(playerY*DataModel.getRatioY());
                        }
                        else{
                            Player competitor = playerMap.get(id);
                            competitor.setXpos((playerX *DataModel.getRatioX()));
                            competitor.setYpos((playerY)*DataModel.getRatioY());
                        }
                    }

                }

                //You died

                else if(packet_number.equals("16")){
                    //
                }

                //You won

                else if(packet_number.equals("17")){
                    //
                }


            } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (JSONException e) {
                e.printStackTrace();
                break;
            }
        }
        if(DataModel.getGameLobby() != null){
            System.out.println("Connection lost: Ending gamelobby");
            AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
            data.delegate = DataModel.getGameLobby();
            data.execute("1");
        }
        if(DataModel.getLobbyList() != null){
            System.out.println("Connection lost: Ending lobby list");
            AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
            data.delegate = DataModel.getLobbyList();
            data.execute("1");
        }
        if(DataModel.getInGame() != null){
            System.out.println("Connection lost: Ending game activity");
            AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
            data.delegate = DataModel.getLobbyList();
            data.execute("1");
        }



        Log.d("RecieveData","Jumping out of loop");
        isStopped = true;
    }
}
