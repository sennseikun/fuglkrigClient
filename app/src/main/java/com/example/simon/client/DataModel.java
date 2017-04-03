package com.example.simon.client;

import android.content.Context;

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

    //Map values

    private static int mapXpos = -1;
    private static int nextMapXpos = -1;
    private static int winnerMapXpos = -1;
    private static String currentMapName = "";

    private static Context gameContext;


    public static double getRatioX() {
        return ratioX;
    }

    public static void setRatioX(double ratioX) {
        DataModel.ratioX = ratioX;
    }

    public static double getRatioY() {
        return ratioY;
    }

    public static void setRatioY(double ratioY) {
        DataModel.ratioY = ratioY;
    }

    public DataModel(){

    }

    //Should this one be a singleton?

    /*public static DataModel getInstance(){
        if(model == null){
            model = new DataModel();
        }
        return model;
    }*/


    public static void setGameLobby(GameLobby lobby){
        DataModel.lobby = lobby;
    }

    public static GameLobby getGameLobby(){
        return DataModel.lobby;
    }

    public static void addLobby(Lobby lobby){
        lobbys.add(lobby);
    }

    public static Lobby getLobby(String name){
        for(Lobby l: lobbys){
            if(l.getName().equals(name)){
                return l;
            }
        }
        return null;
    }

    public static void updateLobby(String name,String playerCount){
        for(Lobby l: lobbys){
            if(l.getName().equals(name)){
                l.setPlayerCount(playerCount);
            }
        }
    }

    public static String getPlayerCount(String name){
        for(Lobby l: lobbys){
            if(l.getName().equals(name)){
                return l.getPlayerCount();
            }
        }
        return null;
    }

    public static void resetLobbyList(){
        lobbys.clear();
    }


    public static String getNick() {
        return nick;
    }

    public static void setNick(String nick) {
        System.out.println("Nick set: " + nick);
        DataModel.nick = nick;
    }

    public static RequestHandler getSocket() {
        return socket;
    }

    public static void setSocket(RequestHandler socket) {
        DataModel.socket = socket;
    }

    public static List<Lobby> getLobbys() {
        return lobbys;
    }

    public static void setLobbys(List<Lobby> lobbys) {
        DataModel.lobbys = lobbys;
    }

    public static void setGameIsValid(int i){
        gameIsValid = i;
    }

    public static  int getGameIsValid(){
        return gameIsValid;
    }

    public static  void cleanLobbyList(){
        lobbys.clear();
    }

    public static String getLastSent() {
        return lastSent;
    }

    public static void setLastSent(String lastSent) {
        DataModel.lastSent = lastSent;
    }

    public static LobbyActivity getLobbyList() {
        return lobbyList;
    }

    public static void setLobbyList(LobbyActivity lobbyList) {
        DataModel.lobbyList = lobbyList;
    }

    public static CreateGameActivity getCreateGame() {
        return CreateGame;
    }

    public static void setCreateGame(CreateGameActivity createGame) {
        CreateGame = createGame;
    }

    public static void addPlayerToLobby(String name){
        if(!playersInLobby.contains(name)){
            playersInLobby.add(name);

        }
    }

    public static void removePlayerFromLobby(String name){

        if(playersInLobby.contains(name)){
            playersInLobby.remove(name);

        }
    }

    public static List<String> getPlayersInLobby() {
        return playersInLobby;
    }

    public static void setPlayersInLobby(List<String> playersInLobby) {
        DataModel.playersInLobby = playersInLobby;
    }

    public static int getP_id() {
        return p_id;
    }

    public static void setP_id(int p_id) {
        DataModel.p_id = p_id;
    }

    public static Player getCurrplayer() {
        return currplayer;
    }

    public static void setCurrplayer(Player currplayer) {
        DataModel.currplayer = currplayer;
    }

    public static HashMap<Integer, Player> getCompetitors() {
        return competitors;
    }

    public static void setCompetitors(HashMap<Integer, Player> competitors) {
        DataModel.competitors = competitors;
    }

    public static void addCompetitor(Integer id,Player player){
        DataModel.competitors.put(id,player);
    }

    public static void removeCompetitor(Integer id){
        DataModel.competitors.remove(id);
    }

    public static double getResolutionX() {
        return resolutionX;
    }

    public static void setResolutionX(double resolutionX) {
        DataModel.resolutionX = resolutionX;
    }

    public static double getResolutionY() {
        return resolutionY;
    }

    public static void setResolutionY(double resolutionY) {
        DataModel.resolutionY = resolutionY;
    }

    public static String getHostPlayer() {
        return hostPlayer;
    }

    public static void setHostPlayer(String hostPlayer) {
        DataModel.hostPlayer = hostPlayer;
    }

    public static double getTargetX() {
        return targetX;
    }

    public static void setTargetX(double targetX) {
        DataModel.targetX = targetX;
    }

    public static double getTargetY() {
        return targetY;
    }

    public static void setTargetY(double targetY) {
        DataModel.targetY = targetY;
    }

    public static GameActivity getInGame() {
        return inGame;
    }

    public static void setInGame(GameActivity inGame) {
        DataModel.inGame = inGame;
    }

    public static Context getGameContext() {
        return gameContext;
    }

    public static void setGameContext(Context gameContext) {
        DataModel.gameContext = gameContext;
    }

    public static double getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(double screenWidth) {
        DataModel.screenWidth = screenWidth;
    }

    public static double getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(double screenHeight) {
        DataModel.screenHeight = screenHeight;
    }

    public static int getMapXpos() {
        return mapXpos;
    }

    public static void setMapXpos(int mapXpos) {
        DataModel.mapXpos = mapXpos;
    }

    public static int getNextMapXpos() {
        return nextMapXpos;
    }

    public static void setNextMapXpos(int nextMapXpos) {
        DataModel.nextMapXpos = nextMapXpos;
    }

    public static int getWinnerMapXpos() {
        return winnerMapXpos;
    }

    public static void setWinnerMapXpos(int winnerMapXpos) {
        DataModel.winnerMapXpos = winnerMapXpos;
    }

    public static String getCurrentMapName() {
        return currentMapName;
    }

    public static void setCurrentMapName(String currentMapName) {
        DataModel.currentMapName = currentMapName;
    }

    public static List<Powerup> getPowerups() {
        return powerups;
    }

    public static void setPowerups(List<Powerup> powerups) {
        DataModel.powerups = powerups;
    }

    public static GameView getGameView() {
        return gameView;
    }

    public static void setGameView(GameView gameView) {
        DataModel.gameView = gameView;
    }

    public static double getPowerupScale() {
        return powerupScale;
    }

    public static void setPowerupScale(double powerupScale) {
        DataModel.powerupScale = powerupScale;
    }

    public static double getBirdPoopScale() {
        return birdPoopScale;
    }

    public static void setBirdPoopScale(double birdPoopScale) {
        DataModel.birdPoopScale = birdPoopScale;
    }

    public static double getWallScale() {
        return wallScale;
    }

    public static void setWallScale(double wallScale) {
        DataModel.wallScale = wallScale;
    }

    public static double getPlayerScale() {
        return playerScale;
    }

    public static void setPlayerScale(double playerScale) {
        DataModel.playerScale = playerScale;
    }
}
