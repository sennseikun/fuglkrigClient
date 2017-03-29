package com.example.simon.client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thoma on 3/24/2017.
 */

public class PlayerModel {

    private static String nick = "";
    private static int p_id = -1;
    private static int hp = -1;
    private static int skin = -1;
    private static float targetPosX = -1;
    private static float targetPosY = -1;
    private static float coordX = -1;
    private static float coordY = -1;
    private static int direction = -1;
    private static int speed = -1;
    private static int alive = -1;
    private static RequestHandler socket = null;
    private static List<Lobby> lobbys = new ArrayList<>();
    private static GameLobby lobby = null;
    private static int gameIsValid = 0;
    private static String lastSent = null;
    private static LobbyActivity lobbyList = null;
    private static CreateGameActivity CreateGame = null;
    private static List<String> playersInLobby = new ArrayList<>();
    private static PlayerModel model = null;

    public PlayerModel (){

    }

    //Should this one be a singleton?

    /*public static PlayerModel getInstance(){
        if(model == null){
            model = new PlayerModel();
        }
        return model;
    }*/


    public static void setGameLobby(GameLobby lobby){
        PlayerModel.lobby = lobby;
    }

    public static GameLobby getGameLobby(){
        return PlayerModel.lobby;
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
        PlayerModel.nick = nick;
    }

    public static int getP_id() {
        return p_id;
    }

    public static void setP_id(int p_id) {
        PlayerModel.p_id = p_id;
    }

    public static int getHp() {
        return hp;
    }

    public static void setHp(int hp) {
        PlayerModel.hp = hp;
    }

    public static int getSkin() {
        return skin;
    }

    public static void setSkin(int skin) {
        PlayerModel.skin = skin;
    }

    public static float getTargetPosX() {
        return targetPosX;
    }

    public static void setTargetPosX(float targetPosX) {
        PlayerModel.targetPosX = targetPosX;
    }

    public static float getTargetPosY() {
        return targetPosY;
    }

    public static void setTargetPosY(float targetPosY) {
        PlayerModel.targetPosY = targetPosY;
    }

    public static float getCoordX() {
        return coordX;
    }

    public static void setCoordX(float coordX) {
        PlayerModel.coordX = coordX;
    }

    public static float getCoordY() {
        return coordY;
    }

    public static void setCoordY(float coordY) {
        PlayerModel.coordY = coordY;
    }

    public static int getDirection() {
        return direction;
    }

    public static void setDirection(int direction) {
        PlayerModel.direction = direction;
    }

    public static int getSpeed() {
        return speed;
    }

    public static void setSpeed(int speed) {
        PlayerModel.speed = speed;
    }

    public static int isAlive() {
        return alive;
    }

    public static void setAlive(int alive) {
        PlayerModel.alive = alive;
    }

    public static RequestHandler getSocket() {
        return socket;
    }

    public static void setSocket(RequestHandler socket) {
        PlayerModel.socket = socket;
    }

    public static List<Lobby> getLobbys() {
        return lobbys;
    }

    public static void setLobbys(List<Lobby> lobbys) {
        PlayerModel.lobbys = lobbys;
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
        PlayerModel.lastSent = lastSent;
    }

    public static LobbyActivity getLobbyList() {
        return lobbyList;
    }

    public static void setLobbyList(LobbyActivity lobbyList) {
        PlayerModel.lobbyList = lobbyList;
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
        PlayerModel.playersInLobby = playersInLobby;
    }
}
