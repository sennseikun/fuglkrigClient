package com.example.simon.client;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
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
    private customListAdapter adapter;
    private final int AFTER_CREATE = 22;
    private Lobby item;
    private RequestHandler handler;
    private RelativeLayout loadingLayout;
    private TextView noLobbies;
    private TextView lobbyHeader;
    Typeface font;
    private boolean firstLoad;
    private List<Lobby> list = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        firstLoad = false;
        lv = (ListView) findViewById(R.id.lobbylist);
        loadingLayout = (RelativeLayout)findViewById(R.id.loadingScreenLayout);
        lobbyHeader = (TextView) findViewById(R.id.item_class);
        font = Typeface.createFromAsset(getAssets(), "bulkypix.ttf");
        lobbyHeader.setTypeface(font);

        Log.d("LobbyActivity","Setting lobbylist in PlayerModel");

        PlayerModel.getPlayersInLobby().clear();

        PlayerModel.setLobbyList(this);
        handler = PlayerModel.getSocket();
        noLobbies = (TextView)findViewById(R.id.noLobbyTxt);

        Log.d("LobbyActivity","Starting to load lobbies");

        LoadLobbys();
    }

    //This method updates the listview from PlayerModel's lobbylist

    public void updateList() {
        // replace the array adapters contents with the ArrayList corresponding to the day

        lv.setVisibility(View.VISIBLE);

        List<Lobby> newList = new ArrayList<>();

        newList.addAll(PlayerModel.getLobbys());

        if (newList.isEmpty()) {
            lv.setVisibility(View.GONE);
            noLobbies.setVisibility(View.VISIBLE);
        } else {
            adapter.updateReceiptsList(newList);

            lv.setAdapter(null);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    item = (Lobby)parent.getItemAtPosition(position);
                    if (!item.getPassword().equals("")){
                        launchPasswordCheck();
                    }
                    else {
                        launchGame(item.getName(),Integer.parseInt(item.getMaxPlayerCount()));
                    }
                }
            });
        }
    }

    public void initList(){

        adapter = new customListAdapter(list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = (Lobby)parent.getItemAtPosition(position);
                if (!item.getPassword().equals("")){
                    launchPasswordCheck();
                }
                else {
                    launchGame(item.getName(),Integer.parseInt(item.getMaxPlayerCount()));
                }
            }
        });

        if(list.size() == 0){
            lv.setVisibility(View.GONE);
            noLobbies.setVisibility(View.VISIBLE);
        }

        firstLoad = true;

        Log.d("Init list","Was called");
    }

    //This method sends JSON object to server and asks for lobbies

    public void LoadLobbys(){

        noLobbies.setVisibility(View.GONE);

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
        finish();
    }

    //Launches a game lobby, reset game lobby data on phone and lobby list data

    public void launchGame(String name, int player){

        PlayerModel.setGameLobby(null);

        JSONObject json = new JSONObject();

        try {
            json.put("Datatype",4);
            json.put("Name",PlayerModel.getNick());
            json.put("Lobby",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        handler.sendData(json);
        lv.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
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
                lv.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.VISIBLE);


                try {
                    JSONObject json = new JSONObject();
                    json.put("Datatype",8);
                    json.put("GameName",item.getName());
                    json.put("Password",input.getText().toString());
                    handler.sendData(json);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


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

    public void RefreshLobbys(View v){
        loadingLayout.setVisibility(View.VISIBLE);
        LoadLobbys();
    }

    public void LaunchAlert(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setTitle("Wrong password");
        alertDialogBuilder.setMessage("Password is incorrect");
        alertDialogBuilder.create();
        alertDialogBuilder.show();

    }

    //Here async tasks called in receiver thread is executed

    @Override
    public void processFinish(String output) {
        System.out.println("Output from processfinish in LobbyActivity: "+output);
        list = PlayerModel.getLobbys();

        if(output.equals("1")){
            startActivity(new Intent(this,MenuActivity.class));
        }

        else if(output.equals("2")){

            Bundle b = new Bundle();

            b.putString("Name",item.getName());

            PlayerModel.setLobbyList(null);
            Intent intent = new Intent(this,GameLobby.class);

            intent.putExtras(b);

            startActivity(intent);
        }
        else if(output.equals("3")){
            loadingLayout.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            LaunchAlert();
        }
        else if(output.equals("4")){
            launchGame(item.getName(),Integer.parseInt(item.getMaxPlayerCount()));
        }

        else{
            if(!firstLoad){
                loadingLayout.setVisibility(View.GONE);
                initList();
            }
            else{
                loadingLayout.setVisibility(View.GONE);
                updateList();

            }
        }
    }
}
