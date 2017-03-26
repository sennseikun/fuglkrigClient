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


    public Lobby(String name, String pc,String mpc, String password){
        this.name = name;
        this.playerCount = pc;
        this.maxPlayerCount = mpc;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(String playerCount) {
        this.playerCount = playerCount;
    }

    public String getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public void setMaxPlayerCount(String maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
