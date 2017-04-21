package com.example.simon.client;

import android.media.MediaPlayer;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
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
    long startTime = 0;
    int packages = 0;
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

        while(running) {

            try {

                String answer = "";

                if(System.currentTimeMillis() > startTime + 1000){
                    startTime = System.currentTimeMillis();
                    if(DataModel.getGameView() != null){
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = DataModel.getGameView();
                        data.execute(packages+ "");
                    }
                    packages = 0;
                }
                else{
                    packages++;
                }

                InputStream inFromServer = client.getInputStream();
                in = new DataInputStream(inFromServer);

                answer = in.readUTF();


                JSONObject translated = null;
                String packet_number = "";


                try {
                    translated = new JSONObject(answer);
                    packet_number = translated.getString("Datatype");

                } catch (JSONException e) {
                    e.printStackTrace();
                    break;
                }

                //Setup player on server side, verify name

                if(packet_number.equals("0")){
                    if(translated.getString("userValid").equals("0")){
                        DataModel.setNick("ERROR");
                        DataModel.setP_id(translated.getInt("pId"));
                    }
                    else{
                        DataModel.setNick(translated.getString("nick"));
                        DataModel.setP_id(translated.getInt("pId"));
                    }
                }

                //Get all lobbys

                else if(packet_number.equals("1")){
                    int count = translated.getInt("Count");

                    for(int i = 1; i < count + 1; i++){

                        String name = translated.getString("Name"+i);
                        String playerCount = translated.getString("PlayerCount"+i);
                        String maxPlayerCount = translated.getString("MaxPlayers"+i);
                        String password = translated.getString("Password"+i);

                        Lobby l = new Lobby(name,playerCount,maxPlayerCount,password);

                        DataModel.addLobby(l);
                    }

                    if(DataModel.getLobbyList() != null){
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
                    System.out.println("Host player: " + hostPlayer);
                    DataModel.setHostPlayer(hostPlayer);

                    DataModel.updateLobby(lobbyname,playerCount);


                    if(DataModel.getGameLobby() != null){
                        DataModel.removePlayerFromLobby(name);
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = DataModel.getGameLobby();

                        //If the player is the one leaving

                        if(name.equals(DataModel.getNick())){
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

                    if(DataModel.getLobbyList() != null){

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
                        DataModel.addPlayerToLobby(name);
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = DataModel.getGameLobby();
                        data.execute("2");
                    }
                }
                else if(packet_number.equals("5")){
                    if(translated.getString("Status").equals("1")){
                    }
                    else{
                    }
                    if(DataModel.getGameLobby() != null){
                        AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                        data.delegate = DataModel.getGameLobby();
                        data.execute();
                    }
                    if(DataModel.getLobbyList() != null){
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


                    int mapType = translated.getInt("MapType");

                    String mapname = "";

                    if(mapType == 1){
                        mapname = "forestlevelbackground";
                    }
                    else if(mapType == 2){
                        mapname = "egyptlevelbackground";
                    }
                    else if(mapType == 3){
                        mapname = "mountainslevelbackground";
                    }

                    else{
                        mapname = "mountainslevelbackground";
                    }

                    DataModel.setCurrentMapName(mapname);

                    int mapXpos = translated.getInt("MapXPos");
                    int nextXpos = translated.getInt("NextMapXPos");
                    int winXpos = translated.getInt("WinMapXPos");

                    double playerScale = translated.getDouble("PlayerScale");
                    double birdPoopScale = translated.getDouble("BirdPoopScale");
                    double wallScale = translated.getDouble("WallScale");
                    double powerupScape = translated.getDouble("PowerupScale");

                    DataModel.setPlayerScale(playerScale);
                    DataModel.setPowerupScale(powerupScape);
                    DataModel.setWallScale(wallScale);
                    DataModel.setBirdPoopScale(birdPoopScale);

                    DataModel.setMapXpos(mapXpos);
                    DataModel.setNextMapXpos(nextXpos);
                    DataModel.setWinnerMapXpos(winXpos);

                    int width = translated.getInt("Width");
                    int height = translated.getInt("Height");

                    DataModel.setResolutionX(width);
                    DataModel.setResolutionY(height);

                    double ratioX = DataModel.getScreenWidth()/width;
                    double ratioY = DataModel.getScreenHeight()/height;

                    DataModel.setRatioX(ratioX);
                    DataModel.setRatioY(ratioY);

                    JSONArray players = translated.getJSONArray("Players");

                    for(int i = 0; i < players.length(); i++){
                        JSONObject player = players.getJSONObject(i);

                        Integer pid = player.getInt("PlayerID");

                        if(pid == DataModel.getP_id()){
                            Player me = new Player();
                            DataModel.setCurrplayer(me);
                        }
                        else{
                            DataModel.addCompetitor(pid, new Player());
                        }
                    }


                    AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
                    data.delegate = DataModel.getGameLobby();
                    data.execute("3");
                }

                //Update game package

                else if(packet_number.equals("15")){

                    String textOnScreen = translated.getString("PrintToPlayer");

                    DataModel.setTextOnScreen(textOnScreen);

                    int mapXpos = translated.getInt("MapXPos");
                    int nextXpos = translated.getInt("NextMapXPos");
                    int winXpos = translated.getInt("WinMapXPos");


                    int server_sent_packets = translated.getInt("ServerSendPackets");

                    DataModel.setServer_sent_packets(server_sent_packets);
                    DataModel.setMapXpos(mapXpos);
                    DataModel.setNextMapXpos(nextXpos);
                    DataModel.setWinnerMapXpos(winXpos);


                    HashMap<Integer,Player> playerMap = DataModel.getCompetitors();
                    JSONArray jsonArray = translated.getJSONArray("Players");

                    int playerCount = jsonArray.length();

                    //Failcheck for drawing to many competitors

                    if(playerCount != DataModel.getCompetitors().size() + 1 && !DataModel.getCompetitors().isEmpty() && jsonArray.length() > 1){

                        System.out.println("Difference between client state and server data");

                        for(Integer key: DataModel.getCompetitors().keySet()){

                            boolean exist = false;
                            Integer removeKey = key;

                            for(Integer i = 0; i < jsonArray.length(); i++){

                                if(key == jsonArray.getJSONObject(i).getInt("PlayerId")){
                                    exist = true;
                                    removeKey = -1;
                                    break;
                                }
                            }
                            if(!exist && removeKey != -1){
                                DataModel.getCompetitors().remove(removeKey);
                            }
                        }
                    }

                    int alivePlayerCount = 0;

                    for(int i = 0; i < jsonArray.length(); i++){

                        JSONObject compJSON = jsonArray.getJSONObject(i);
                        Integer id = compJSON.getInt("PlayerID");

                        int playerX = compJSON.getInt("PosX");
                        int playerY = compJSON.getInt("PosY");
                        boolean alive = compJSON.getBoolean("Alive");

                        if(alive){
                            alivePlayerCount++;
                        }


                        if(id == DataModel.getP_id()){

                            Player me = DataModel.getCurrplayer();

                            JSONArray powerups_inventory  = compJSON.getJSONArray("Powerups");

                            if(powerups_inventory.length() == 0){
                                DataModel.setPowerup_type(-1);
                            }

                            else{
                                for(int j = 0; j < powerups_inventory.length(); j++){
                                    int powerup = powerups_inventory.getInt(j);
                                    DataModel.setPowerup_type(powerup);
                                }
                            }

                            me.setXpos(playerX*DataModel.getRatioX());
                            me.setYpos(playerY*DataModel.getRatioY());
                            me.setAlive(alive);
                        }
                        else{
                            Player competitor = playerMap.get(id);
                            competitor.setXpos((playerX *DataModel.getRatioX()));
                            competitor.setYpos((playerY)*DataModel.getRatioY());
                            competitor.setAlive(alive);
                        }
                    }

                    if(alivePlayerCount != DataModel.getPlayerCount()){

                        System.out.println("Player died editing alive player count");

                        //Play player died sound
                        DataModel.getDiesound().start();

                        DataModel.setPlayerCount(alivePlayerCount);

                    }

                    JSONArray powerupArray = translated.getJSONArray("PowerupData");
                    
                    List<Powerup> powerups = new ArrayList<>();

                    for(int i = 0; i < powerupArray.length(); i++){
                        JSONObject powerup = powerupArray.getJSONObject(i);

                        int id = powerup.getInt("Id");
                        int type = powerup.getInt("Type");
                        int xPos = powerup.getInt("XPos");
                        int yPos = powerup.getInt("YPos");

                        Powerup p = new Powerup((int)(xPos*DataModel.getRatioX()),(int)(yPos*DataModel.getRatioY()),id,type);
                        powerups.add(p);

                    }
                    DataModel.setPowerups(powerups);
                }

                //You died

                else if(packet_number.equals("16")){
                    boolean victory = translated.getBoolean("Victory");
                    DataModel.setIsOver(true);
                    DataModel.setPlacement(translated.getInt("Placement"));
                    System.out.println("Placement: "+translated.getInt("Placement"));
                    DataModel.setIsVictory(victory);
                    System.out.println("Got package 16");
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
            AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
            data.delegate = DataModel.getGameLobby();
            data.execute("1");
        }
        if(DataModel.getLobbyList() != null){
            AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
            data.delegate = DataModel.getLobbyList();
            data.execute("1");
        }
        if(DataModel.getGameView() != null && DataModel.getInGame() != null){
            System.out.println("Connection lost: Ending game activity");
            AsyncUpdateLobbyList data = new AsyncUpdateLobbyList();
            data.delegate = DataModel.getInGame();
            data.execute("1");
        }



        isStopped = true;
    }
}
