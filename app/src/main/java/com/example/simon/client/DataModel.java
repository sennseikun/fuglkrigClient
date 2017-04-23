package com.example.simon.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thoma on 3/24/2017.
 */

public class DataModel {

    private static String nick = "";
    private static RequestHandler socket = null;
    private static List<Lobby> lobbys = new ArrayList<>();
    private static GameLobby lobby = null;
    private static int gameIsValid = 0;
    private static String lastSent = null;
    private static LobbyActivity lobbyList = null;
    private static CreateGameActivity CreateGame = null;
    private static GameActivity inGame = null;
    private static List<String> playersInLobby = new ArrayList<>();
    private static DataModel model = null;
    private static int p_id = -1;
    private static Player currplayer;
    private static HashMap<Integer, Player> competitors = new HashMap<>();
    private static double resolutionX = -1;
    private static double resolutionY = -1;
    private static double screenWidth = -1;
    private static double screenHeight = -1;
    private static String hostPlayer = "";
    private static double targetX = -1;
    private static double targetY = -1;
    private static double ratioX;
    private static double ratioY;
    private static List<Powerup> powerups = new ArrayList<>();
    private static GameView gameView = null;
    private static double powerupScale = -1;
    private static double birdPoopScale = -1;
    private static double wallScale = -1;
    private static double playerScale = -1;
    private static String textOnScreen = "";
    private static boolean isOver = false;
    private static boolean isVictory = false;
    private static boolean issound = true;
    private static boolean ismusic = true;
    private static int fps = 0;
    private static int server_sent_packets = 0;
    private static int powerupCountOnMap = 0;
    private static int playerCount = 0;
    private static int placement = -1;
    private static int powerup_type = -1;
    private static MediaPlayer lobbyMediaplayer;
    private static MediaPlayer diesound;
    private static MediaPlayer defeatmusic;
    private static MediaPlayer victorymusic;
    private static MediaPlayer gamemusic;
    private static MediaPlayer menumusic;
    private static int menumusiclength;

    private static JSONArray powerupsOnMap = new JSONArray();
    private static int length;
    //Map values

    private static int mapXpos = -1;
    private static int nextMapXpos = -1;
    private static int winnerMapXpos = -1;
    private static String currentMapName = "";

    private static Context gameContext;


    /**
     * @return the scaling ratio for the X axis.
     */
    public static double getRatioX() {
        return ratioX;
    }

    /**
     * Sets the scaling ratio for the X axis.
     * @param ratioX
     */
    public static void setRatioX(double ratioX) {
        DataModel.ratioX = ratioX;
    }

    /**
     * @return the scaling ratio for the Y axis.
     */
    public static double getRatioY() {
        return ratioY;
    }

    /**
     * Sets the scaling ratio for the Y axis.
     * @param ratioY
     */
    public static void setRatioY(double ratioY) {
        DataModel.ratioY = ratioY;
    }

    /**
     *
     * @return the mediaplayer for the lobby.
     */
    public static MediaPlayer getLobbyMediaplayer(){
        length=lobbyMediaplayer.getCurrentPosition();
        return lobbyMediaplayer;
    };

    /**
     * Sets the mediaplayer for the lobby
     * @param mediaplayer
     */
    public static void setLobbyMediaplayer(MediaPlayer mediaplayer){
        lobbyMediaplayer = mediaplayer;
    };

    /**
     * @return the length of the mediaplayer.
     */
    public static int getLobbyMediaplayerLength(){
        return length;
    };

    /**
     * Sets the length of the mediaplayer.
     * @param len
     */
    public static void setLobbyMediaplayerLength(int len){
        length = len;
    };

    /**
     * Sets the die sound.
     * @param mediaPlayer
     */
    public static void setDiesound(MediaPlayer mediaPlayer) {
        diesound = mediaPlayer;
    }

    /**
     *
     * @return the die sound.
     */
    public static MediaPlayer getDiesound() {
        return diesound;
    }

    /**
     * Sets the music that is played on defeat.
     * @param defeatmusic
     */
    public static void setDefeatmusic(MediaPlayer defeatmusic) {
        DataModel.defeatmusic = defeatmusic;
    }

    /**
     *
     * @return the music that plays when you are defeated.
     */
    public static MediaPlayer getDefeatmusic() {
        return defeatmusic;
    }

    /**
     * Sets the music played on victory.
     * @param victorymusic
     */
    public static void setVictorymusic(MediaPlayer victorymusic) {
        DataModel.victorymusic = victorymusic;
    }

    /**
     * @return the music played on victory.
     */
    public static MediaPlayer getVictorymusic() {
        return victorymusic;
    }

    /**
     * Sets the game music.
     * @param gamemusic
     */
    public static void setGamemusic(MediaPlayer gamemusic) {
        DataModel.gamemusic = gamemusic;
    }

    /**
     * @return the gamemusic.
     */
    public static MediaPlayer getGamemusic() {
        return gamemusic;
    }

    /**
     * Sets the music that is played in the menu.
     * @param mediaPlayer
     */
    public static void setMenumusic(MediaPlayer mediaPlayer) {
        menumusic = mediaPlayer;
    }

    /**
     * @return the music played in the menu.
     */
    public static MediaPlayer getMenumusic() {
        return menumusic;
    }

    /**
     * Sets the length of the music playing in the menu.
     * @param length
     */
    public static void setMenumusiclength(int length) {
        menumusiclength = length;
    }

    /**
     * @return the music played in the menu.
     */
    public static int getMenumusiclength() {
        return menumusiclength;
    }

    /**
     * Empty datamodel.
     */
    public DataModel(){

    }

    //Should this one be a singleton?

    /*public static DataModel getInstance(){
        if(model == null){
            model = new DataModel();
        }
        return model;
    }*/

    /**
     * Set the games lobby.
     * @param lobby
     */
    public static void setGameLobby(GameLobby lobby){
        DataModel.lobby = lobby;
    }

    /**
     * @return the game lobby
     */
    public static GameLobby getGameLobby(){
        return DataModel.lobby;
    }

    /**
     * Adds a lobby to the list of lobbies.
     * @param lobby
     */
    public static void addLobby(Lobby lobby){
        lobbys.add(lobby);
    }

    /**
     *
     * @param name of the wanted lobby
     * @return the game lobby by the given name.
     */
    public static Lobby getLobby(String name){
        for(Lobby l: lobbys){
            if(l.getName().equals(name)){
                return l;
            }
        }
        return null;
    }

    /**
     * Updates the given lobby.
     * @param name
     * @param playerCount
     */
    public static void updateLobby(String name,String playerCount){
        for(Lobby l: lobbys){
            if(l.getName().equals(name)){
                l.setPlayerCount(playerCount);
            }
        }
    }

    /**
     *
     * @param name
     * @return the player count of the lobbies
     */
    public static String getPlayerCount(String name){
        for(Lobby l: lobbys){
            if(l.getName().equals(name)){
                return l.getPlayerCount();
            }
        }
        return null;
    }

    /**
     * Clears the lobbylist.
     */
    public static void resetLobbyList(){
        lobbys.clear();
    }

    /**
     * @return the nick of the lobby
     */
    public static String getNick() {
        return nick;
    }

    /**
     * Sets the nick of the lobby.
     * @param nick
     */
    public static void setNick(String nick) {
        DataModel.nick = nick;
    }

    /**
     * @return the datamodel's socket.
     */
    public static RequestHandler getSocket() {
        return socket;
    }

    /**
     * set the socket of the datamodel.
     * @param socket
     */
    public static void setSocket(RequestHandler socket) {
        DataModel.socket = socket;
    }

    /**
     * @return the list of lobbies in the datamodel.
     */
    public static List<Lobby> getLobbys() {
        return lobbys;
    }

    /**
     * Sets the lobbies the the given list.
     * @param lobbys
     */
    public static void setLobbys(List<Lobby> lobbys) {
        DataModel.lobbys = lobbys;
    }

    /**
     * Sets the validstate of the game.
     * @param i
     */
    public static void setGameIsValid(int i){
        gameIsValid = i;
    }

    /**
     * @return the validstate of the game.
     */
    public static  int getGameIsValid(){
        return gameIsValid;
    }

    /**
     * Clears the lobbylist.
     */
    public static  void cleanLobbyList(){
        lobbys.clear();
    }

    /**
     * @return the time when something was last sent.
     */
    public static String getLastSent() {
        return lastSent;
    }

    /**
     * Sets the lastSent value of the datamodel the the input.
     * @param lastSent
     */
    public static void setLastSent(String lastSent) {
        DataModel.lastSent = lastSent;
    }

    /**
     * @return the lobbylist.
     */
    public static LobbyActivity getLobbyList() {
        return lobbyList;
    }

    /**
     * Sets the lobbylist the given list.
     * @param lobbyList
     */
    public static void setLobbyList(LobbyActivity lobbyList) {
        DataModel.lobbyList = lobbyList;
    }

    /**
     * @return the createGameActivity creatGame.
     */
    public static CreateGameActivity getCreateGame() {
        return CreateGame;
    }

    /**
     * Sets the createGameActivity to the given param.
     * @param createGame
     */
    public static void setCreateGame(CreateGameActivity createGame) {
        CreateGame = createGame;
    }

    /**
     * Adds a player to the lobby
     * @param name
     */
    public static void addPlayerToLobby(String name){
        if(!playersInLobby.contains(name)){
            playersInLobby.add(name);

        }
    }

    /**
     * Removes the given player from the lobby.
     * @param name
     */
    public static void removePlayerFromLobby(String name){

        if(playersInLobby.contains(name)){
            playersInLobby.remove(name);

        }
    }

    /**
     * @return the players in the lobby
     */
    public static List<String> getPlayersInLobby() {
        return playersInLobby;
    }

    /**
     * Sets the players in the lobby to the list given.
     * @param playersInLobby
     */
    public static void setPlayersInLobby(List<String> playersInLobby) {
        DataModel.playersInLobby = playersInLobby;
    }

    /**
     * @return the player id.
     */
    public static int getP_id() {
        return p_id;
    }

    /**
     * Sets the player id.
     * @param p_id
     */
    public static void setP_id(int p_id) {
        DataModel.p_id = p_id;
    }

    /**
     * @return the current player.
     */
    public static Player getCurrplayer() {
        return currplayer;
    }

    /**
     * Sets the currentplayer to the given param.
     * @param currplayer
     */
    public static void setCurrplayer(Player currplayer) {
        DataModel.currplayer = currplayer;
    }

    /**
     * @return the hasmap of the competitors.
     */
    public static HashMap<Integer, Player> getCompetitors() {
        return competitors;
    }

    /**
     * Sets the competitors.
     * @param competitors
     */
    public static void setCompetitors(HashMap<Integer, Player> competitors) {
        DataModel.competitors = competitors;
    }

    /**
     * Adds a competitor to the list of competitors.
     * @param id
     * @param player
     */
    public static void addCompetitor(Integer id,Player player){
        DataModel.competitors.put(id,player);
    }

    /**
     * Remove the competitor by the given id.
     * @param id
     */
    public static void removeCompetitor(Integer id){
        DataModel.competitors.remove(id);
    }

    /**
     * @return the resolution on the X axis.
     */
    public static double getResolutionX() {
        return resolutionX;
    }

    /**
     * Sets the resolution on the X axis.
     * @param resolutionX
     */
    public static void setResolutionX(double resolutionX) {
        DataModel.resolutionX = resolutionX;
    }

    /**
     * @return the resolution on the Y axis.
     */
    public static double getResolutionY() {
        return resolutionY;
    }

    /**
     * Sets the resolution on the Y axis.
     * @param resolutionY
     */
    public static void setResolutionY(double resolutionY) {
        DataModel.resolutionY = resolutionY;
    }

    /**
     * @return the host player.
     */
    public static String getHostPlayer() {
        return hostPlayer;
    }

    /**
     * Sets the hostplayer.
     * @param hostPlayer
     */
    public static void setHostPlayer(String hostPlayer) {
        DataModel.hostPlayer = hostPlayer;
    }

    /**
     * @return the target X.
     */
    public static double getTargetX() {
        return targetX;
    }

    /**
     * Sets the target x.
     * @param targetX
     */
    public static void setTargetX(double targetX) {
        DataModel.targetX = targetX;
    }

    /**
     * @return the target y.
     */
    public static double getTargetY() {
        return targetY;
    }

    /**
     * Sets the target Y.
     * @param targetY
     */
    public static void setTargetY(double targetY) {
        DataModel.targetY = targetY;
    }

    /**
     * @return the gameActivity inGame.
     */
    public static GameActivity getInGame() {
        return inGame;
    }

    /**
     * Sets the GameActivity inGame to the given param.
     * @param inGame
     */
    public static void setInGame(GameActivity inGame) {
        DataModel.inGame = inGame;
    }

    /**
     * @return the game context
     */
    public static Context getGameContext() {
        return gameContext;
    }

    /**
     * Sets the game context to the given context.
     * @param gameContext
     */
    public static void setGameContext(Context gameContext) {
        DataModel.gameContext = gameContext;
    }

    /**
     * @return the screenwidth.
     */
    public static double getScreenWidth() {
        return screenWidth;
    }

    /**
     * Sets the screenwidth.
     * @param screenWidth
     */
    public static void setScreenWidth(double screenWidth) {
        DataModel.screenWidth = screenWidth;
    }

    /**
     * @return the screenheight.
     */
    public static double getScreenHeight() {
        return screenHeight;
    }

    /**
     * Sets the screenheight.
     * @param screenHeight
     */
    public static void setScreenHeight(double screenHeight) {
        DataModel.screenHeight = screenHeight;
    }

    /**
     * @return the maps X pos.
     */
    public static int getMapXpos() {
        return mapXpos;
    }

    /**
     * Sets the games X pos.
     * @param mapXpos
     */
    public static void setMapXpos(int mapXpos) {
        DataModel.mapXpos = mapXpos;
    }

    /**
     * @return te maps next x pos.
     */
    public static int getNextMapXpos() {
        return nextMapXpos;
    }

    /**
     * Sets the maps next x pos.
     * @param nextMapXpos
     */
    public static void setNextMapXpos(int nextMapXpos) {
        DataModel.nextMapXpos = nextMapXpos;
    }

    /**
     * @return the winner maps x pos.
     */
    public static int getWinnerMapXpos() {
        return winnerMapXpos;
    }

    /**
     * Sets the winner maps x pos.
     * @param winnerMapXpos
     */
    public static void setWinnerMapXpos(int winnerMapXpos) {
        DataModel.winnerMapXpos = winnerMapXpos;
    }

    /**
     * @return the current maps name.
     */
    public static String getCurrentMapName() {
        return currentMapName;
    }

    /**
     * Sets the current maps name.
     * @param currentMapName
     */
    public static void setCurrentMapName(String currentMapName) {
        DataModel.currentMapName = currentMapName;
    }

    /**
     * @return the list of powerups.
     */
    public static List<Powerup> getPowerups() {
        return powerups;
    }

    /**
     * Sets the list of powerups the the given list.
     * @param powerups
     */
    public static void setPowerups(List<Powerup> powerups) {
        DataModel.powerups = powerups;
    }

    /**
     * @return the game view.
     */
    public static GameView getGameView() {
        return gameView;
    }

    /**
     * Sets the game view.
     * @param gameView
     */
    public static void setGameView(GameView gameView) {
        DataModel.gameView = gameView;
    }

    /**
     * @return the scale of the powerup.
     */
    public static double getPowerupScale() {
        return powerupScale;
    }

    /**
     * Sets the scale of the powerup.
     * @param powerupScale
     */
    public static void setPowerupScale(double powerupScale) {
        DataModel.powerupScale = powerupScale;
    }

    /**
     * @return the bird poops scale.
     */
    public static double getBirdPoopScale() {
        return birdPoopScale;
    }

    /**
     * Sets the scale of the birdpoop.
     * @param birdPoopScale
     */
    public static void setBirdPoopScale(double birdPoopScale) {
        DataModel.birdPoopScale = birdPoopScale;
    }

    /**
     * @return the scale of the wall.
     */
    public static double getWallScale() {
        return wallScale;
    }

    /**
     * Sets the scale of the wall.
     * @param wallScale
     */
    public static void setWallScale(double wallScale) {
        DataModel.wallScale = wallScale;
    }

    /**
     * @return the scale of the player(bird).
     */
    public static double getPlayerScale() {
        return playerScale;
    }

    /**
     * Sets the scale of the bird.
     * @param playerScale
     */
    public static void setPlayerScale(double playerScale) {
        DataModel.playerScale = playerScale;
    }

    /**
     * @return the text on screen.
     */
    public static String getTextOnScreen() {
        return textOnScreen;
    }

    /**
     * Sets the text on screen.
     * @param textOnScreen
     */
    public static void setTextOnScreen(String textOnScreen) {
        DataModel.textOnScreen = textOnScreen;
    }

    /**
     * @return the boolean value if the game is over or not.
     */
    public static boolean isOver() {
        return isOver;
    }

    /**
     * Sets the boolean value if the game is over or not.
     * @param isOver
     */
    public static void setIsOver(boolean isOver) {
        DataModel.isOver = isOver;
    }

    /**
     * @return the boolean value if there is a victory.
     */
    public static boolean isVictory() {
        return isVictory;
    }

    /**
     * Sets the boolean value if there is a victory.
     * @param isVictory
     */
    public static void setIsVictory(boolean isVictory) {
        DataModel.isVictory = isVictory;
    }

    /**
     * @return the fps.
     */
    public static int getFps() {
        return fps;
    }

    /**
     * Sets the fps.
     * @param fps
     */
    public static void setFps(int fps) {
        DataModel.fps = fps;
    }

    /**
     * @return the value of sent packets from the server.
     */
    public static int getServer_sent_packets() {
        return server_sent_packets;
    }

    /**
     * Sets the value of sent packets to the server.
     * @param server_sent_packets
     */
    public static void setServer_sent_packets(int server_sent_packets) {
        DataModel.server_sent_packets = server_sent_packets;
    }

    /**
     * @return the placement by int value.
     */
    public static int getPlacement() {
        return placement;
    }

    /**
     * Sets the placement to the int value.
     * @param placement
     */
    public static void setPlacement(int placement) {
        DataModel.placement = placement;
    }

    /**
     * @return the powerup type.
     */
    public static int getPowerup_type() {
        return powerup_type;
    }

    /**
     * Sets the powerup type.
     * @param powerup_type
     */
    public static void setPowerup_type(int powerup_type) {
        DataModel.powerup_type = powerup_type;
    }

    /**
     * @return number of powerups on the screen.
     */
    public static int getPowerupCountOnMap() {
        return powerupCountOnMap;
    }

    /**
     * Sets the number of powerups on the screen.
     * @param powerupCountOnMap
     */
    public static void setPowerupCountOnMap(int powerupCountOnMap) {
        DataModel.powerupCountOnMap = powerupCountOnMap;
    }

    /**
     * @return the count of players.
     */
    public static int getPlayerCount() {
        return playerCount;
    }

    /**
     * Sets the number of players.
     * @param playerCount
     */
    public static void setPlayerCount(int playerCount) {
        DataModel.playerCount = playerCount;
    }

    /**
     * Sound methods
     */


    /**
     * Sets the setting if you want music.
     * @param sound
     */
    public static void setSound(boolean sound){
        issound = sound;
//        SharedPreferences SPname = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = SPname.edit();
//        editor.putBoolean("Sound", on);
//        editor.commit();
    }

    /**
     * @return the boolean value if the player wants sound or not.
     */
    public static boolean isSoundOn(){
        //return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("Sound", false);
        return issound;
    }

    /**
     * Music methods
     */

    /**
     * Sets the music to the param.
     * @param music
     */
    public static void setMusic(boolean music){
        ismusic = music;
//        SharedPreferences SPname = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = SPname.edit();
//        editor.putBoolean("Music", on);
//        editor.commit();
    }

    /**
     * @return the boolean value if the player wants music or not.
     */
    public static boolean isMusicOn(){
        return ismusic;
        //return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("Music", false);
    }

    /**
     * @return the JSONArray of powerups on the map.
     */
    public static JSONArray getPowerupsOnMap() {
        return powerupsOnMap;
    }

    /**
     * Sets the JSONArray powerupsOnMap the the given param.
     * @param powerupsOnMap
     */
    public static void setPowerupsOnMap(JSONArray powerupsOnMap) {
        DataModel.powerupsOnMap = powerupsOnMap;
    }
}
