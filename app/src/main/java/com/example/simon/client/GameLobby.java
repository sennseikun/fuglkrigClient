package com.example.simon.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameLobby extends Activity implements AsyncResponse  {

    private TextView txtName;
    private TextView txtPlayers;
    private Button btn_cancel;
    private String playerCount;
    private String maxPlayers;
    private RequestHandler handler;
    private String name;
    private ListView lv;
    private PlayerListAdapter adapter;
    private Typeface font;
    private Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        handler = DataModel.getSocket();

        Bundle bundle = getIntent().getExtras();

        btn_start = (Button)findViewById(R.id.start_button);

        if(!DataModel.getHostPlayer().equals(DataModel.getNick()) || !(DataModel.getPlayersInLobby().size() > 1)){
            btn_start.setBackgroundResource(R.drawable.disabledstartgamebutton);
            btn_start.setEnabled(false);
        }

        txtName = (TextView)findViewById(R.id.item_class);
        txtPlayers = (TextView)findViewById(R.id.txt_count);
        btn_cancel = (Button)findViewById(R.id.cancel_gamelobby);
        lv = (ListView)findViewById(R.id.GameLobby_list);




        name = bundle.getString("Name");

        maxPlayers = DataModel.getLobby(name).getMaxPlayerCount();
        playerCount = DataModel.getLobby(name).getPlayerCount();


        font = Typeface.createFromAsset(getAssets(), "bulkypix.ttf");
        txtName.setTypeface(font);
        txtName.setText(name);
        txtPlayers.setText(playerCount+"/"+maxPlayers);

        InitListView();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        DataModel.setGameLobby(this);

    }

    public void goToLobbyList(){
        startActivity(new Intent(this,LobbyActivity.class));
        finish();
    }

    public void StartGame(View view) {
        JSONObject json = new JSONObject();
        try {
            json.put("Datatype", 11);
            handler.sendData(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void updateListView(){

        System.out.println("Should update listview");

        List<String> newList = new ArrayList<>();
        newList.addAll(DataModel.getPlayersInLobby());

        adapter.updateReceiptsList(newList);

        lv.setAdapter(null);
        lv.setAdapter(adapter);
    }

    public void InitListView(){
        List<String> players = DataModel.getPlayersInLobby();
        adapter = new PlayerListAdapter(players);
        lv.setAdapter(adapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        DataModel.setGameLobby(null);
        finish();
    }

    public void updatePlayers(int players){
        txtPlayers.setText(players+"/"+maxPlayers);
    }

    //This method sends updated info to server

    private void cancel(){

        JSONObject json = new JSONObject();

        String nick = DataModel.getNick();

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
        //updatePlayers(Integer.parseInt(DataModel.getPlayerCount(name)));
        if(output.equals("1")){
            startActivity(new Intent(this,MenuActivity.class));
            finish();
        }

        else if(output.equals("2")){
            updatePlayers(Integer.parseInt(DataModel.getPlayerCount(name)));
            if(DataModel.getNick().equals(DataModel.getHostPlayer()) && DataModel.getPlayersInLobby().size() > 1){
                btn_start.setBackgroundResource(R.drawable.startgamebutton);
                btn_start.setEnabled(true);
            }
            updateListView();
        }

        else if(output.equals("3")){
            DataModel.setInGame(null);
            startActivity(new Intent(this,GameActivity.class));
            finish();
        }

        else{
            goToLobbyList();
            finish();
        }

    }
}
