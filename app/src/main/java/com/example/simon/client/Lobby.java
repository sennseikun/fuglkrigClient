package com.example.simon.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 3/26/2017.
 */

public class Lobby {

    private String name = null;
    private String playerCount = null;
    private String maxPlayerCount = null;
    private String password;


    public Lobby(String name, String pc, String mpc, String password){
        this.name = name;
        this.playerCount = pc;
        this.maxPlayerCount = mpc;
        this.password = password;
    }

    /**
     * @return the name of the lobby
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the lobby
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the amount of players in the lobby.
     */
    public String getPlayerCount() {
        return playerCount;
    }

    /**
     * Sets the number of players in the lobby.
     * @param playerCount
     */
    public void setPlayerCount(String playerCount) {
        this.playerCount = playerCount;
    }

    /**
     * @return the max number of players in this lobby.
     */
    public String getMaxPlayerCount() {
        return maxPlayerCount;
    }

    /**
     * Sets the max player in the lobby.
     * @param maxPlayerCount
     */
    public void setMaxPlayerCount(String maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    /**
     * @return the password for the lobby.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the lobby.
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
