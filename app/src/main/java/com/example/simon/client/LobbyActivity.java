package com.example.simon.client;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class LobbyActivity extends AppCompatActivity implements AsyncResponse {

    private ListView lv;
    private List<List<String>> list;
    private customListAdapter adapter;
    private final int AFTER_CREATE = 22;
    private List<String> item;
    private RequestHandler handler;
    private RelativeLayout loadingLayout;
    private TextView noLobbies;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        loadingLayout = (RelativeLayout)findViewById(R.id.loadingScreenLayout);

        Log.d("LobbyActivity","Setting lobbylist in PlayerModel");

        PlayerModel.setLobbyList(this);
        handler = PlayerModel.getSocket();
        noLobbies = (TextView)findViewById(R.id.noLobbyTxt);

        Log.d("LobbyActivity","Starting to load lobbies");

        LoadLobbys();
    }

    //This method updates the listview from PlayerModel's lobbylist

    public void updateList(){

        lv = (ListView) findViewById(R.id.lobbylist);
        list = PlayerModel.getLobbys();
        adapter = new customListAdapter(list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = (List<String>)parent.getItemAtPosition(position);
                if (!item.get(3).toString().equals("")){
                    launchPasswordCheck();
                }
                else {
                    launchGame(item.get(0),Integer.parseInt(item.get(2)));
                }
            }
        });

        adapter.notifyDataSetChanged();

        if(list.size() == 0){
            lv.setVisibility(View.GONE);
            noLobbies.setVisibility(View.VISIBLE);
        }

        Log.d("Update list","Was called");
    }

    //This method sends JSON object to server and asks for lobbies

    public void LoadLobbys(){

        PlayerModel.getLobbys().clear();
        String datatype = "1";

        System.out.println("Started loading lobbys");

        if(PlayerModel.getLastSent() != null){
            try {
                JSONObject lastSent = new JSONObject(PlayerModel.getLastSent());
                if(lastSent.getString("Datatype").equals("1")){
                    datatype = "10";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(handler != null){
            JSONObject json = new JSONObject();
            System.out.println("Datatype "+datatype+" sent from LobbyActivity");
            try {
                json.put("Datatype",datatype);
                handler.sendData(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //Makes sure the lobbylist is reset on quit

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("OnDestroy","Was here");
        PlayerModel.setLobbyList(null);
    }

    //Launches a game lobby, reset game lobby data on phone and lobby list data

    public void launchGame(String name, int player){

        PlayerModel.setGameLobby(null);
        PlayerModel.setLobbyList(null);

        JSONObject json = new JSONObject();

        try {
            json.put("Name",PlayerModel.getNick());
            json.put("Lobby",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        handler.sendData(json);

        Bundle bundle = new Bundle();
        bundle.putString("Name",name);
        bundle.putInt("Players",player);

        Intent intent = new Intent(this,GameLobby.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Go to create game, make sure to reset lobbylist

    public void goToCreate(View v){
        PlayerModel.setLobbyList(null);
        startActivityForResult(new Intent(this,CreateGameActivity.class),AFTER_CREATE);
        finish();
    }

    //Go to menu, reset lobby list

    public void goToMenu(){
        PlayerModel.setLobbyList(null);
        startActivity(new Intent(this,MenuActivity.class));
        finish();
    }

    //Make sure that the right logic is called on quitting the game, kill connection

    @Override
    public void onBackPressed(){

        if(PlayerModel.getSocket() != null){
            System.out.println("Gets here");
            RequestHandler handler = PlayerModel.getSocket();
            handler.stopSending();
        }

        goToMenu();
    }

    //Launches password check if game has password

    public void launchPasswordCheck(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        alertDialogBuilder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Check password with server here
                launchGame(item.get(0),Integer.parseInt(item.get(2)));
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setTitle("Password");
        alertDialogBuilder.setMessage("This game has a password");
        alertDialogBuilder.setView(input);
        alertDialogBuilder.create();
        alertDialogBuilder.show();

    }

    //Here async tasks called in receiver thread is executed

    @Override
    public void processFinish(String output) {
        System.out.println("Output from processfinish in LobbyActivity: "+output);
        loadingLayout.setVisibility(View.GONE);

        if(output.equals("1")){
            startActivity(new Intent(this,MenuActivity.class));
        }
        else{
            updateList();
        }
    }
}
