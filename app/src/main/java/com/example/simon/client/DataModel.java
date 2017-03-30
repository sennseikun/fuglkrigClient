package com.example.simon.client;

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
    private static int resolutionX = -1;
    private static  int resolutionY = -1;
    private static String hostPlayer = "";


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

    public static int getResolutionX() {
        return resolutionX;
    }

    public static void setResolutionX(int resolutionX) {
        DataModel.resolutionX = resolutionX;
    }

    public static int getResolutionY() {
        return resolutionY;
    }

    public static void setResolutionY(int resolutionY) {
        DataModel.resolutionY = resolutionY;
    }

    public static String getHostPlayer() {
        return hostPlayer;
    }

    public static void setHostPlayer(String hostPlayer) {
        DataModel.hostPlayer = hostPlayer;
    }
}
