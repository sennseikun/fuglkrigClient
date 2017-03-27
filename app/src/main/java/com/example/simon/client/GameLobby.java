package com.example.simon.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameLobby extends AppCompatActivity implements AsyncResponse  {

    private TextView txtName;
    private TextView txtPlayers;
    private Button btn_cancel;
    private String playerCount;
    private String maxPlayers;
    private RequestHandler handler;
    private String name;
    private ListView lv;
    private PlayerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        handler = PlayerModel.getSocket();

        Bundle bundle = getIntent().getExtras();

        txtName = (TextView)findViewById(R.id.txt_name);
        txtPlayers = (TextView)findViewById(R.id.txt_count);
        btn_cancel = (Button)findViewById(R.id.cancel_gamelobby);
        lv = (ListView)findViewById(R.id.GameLobby_list);



        name = bundle.getString("Name");

        maxPlayers = PlayerModel.getLobby(name).getMaxPlayerCount();
        playerCount = PlayerModel.getLobby(name).getPlayerCount();

        txtName.setText(name);
        txtPlayers.setText(playerCount+"/"+maxPlayers);

        InitListView();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        PlayerModel.setGameLobby(this);

    }

    public void goToLobbyList(){
        startActivity(new Intent(this,LobbyActivity.class));
        finish();
    }

    public void updateListView(){

        System.out.println("Should update listview");

        List<String> newList = new ArrayList<>();
        newList.addAll(PlayerModel.getPlayersInLobby());

        adapter.updateReceiptsList(newList);

        lv.setAdapter(null);
        lv.setAdapter(adapter);
    }

    public void InitListView(){
        List<String> players = PlayerModel.getPlayersInLobby();
        adapter = new PlayerListAdapter(players);
        lv.setAdapter(adapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        PlayerModel.setGameLobby(null);
        finish();
    }

    public void updatePlayers(int players){
        txtPlayers.setText(players+"/"+maxPlayers);
    }

    //This method sends updated info to server

    private void cancel(){

        JSONObject json = new JSONObject();

        String nick = PlayerModel.getNick();

        try {
            json.put("Datatype",3);
            json.put("Name",nick);
            json.put("Lobby",name);

            handler.sendData(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        cancel();
    }

    //If connection is killed a asynctask is fired to kill the process

    @Override
    public void processFinish(String output) {
        //updatePlayers(Integer.parseInt(PlayerModel.getPlayerCount(name)));
        if(output.equals("1")){
            startActivity(new Intent(this,MenuActivity.class));
            finish();
        }

        else if(output.equals("2")){
            updatePlayers(Integer.parseInt(PlayerModel.getPlayerCount(name)));
            updateListView();
        }

        else{
            goToLobbyList();
            finish();
        }

    }
}
