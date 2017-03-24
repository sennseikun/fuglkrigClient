package com.example.simon.client;

import java.net.Socket;

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

    public PlayerModel (){

    }


    public static String getNick() {
        return nick;
    }

    public static void setNick(String nick) {
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
}
