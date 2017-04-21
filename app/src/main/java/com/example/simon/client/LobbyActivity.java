package com.example.simon.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LobbyActivity extends Activity implements AsyncResponse {

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
    private boolean wasPaused = false;
    private Button createGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataModel.setLobbyList(null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lobby);
        firstLoad = false;

        createGame = (Button)findViewById(R.id.btn_create);
        createGame.setEnabled(true);
        if (DataModel.getLobbyMediaplayerLength() == 0){
            DataModel.setLobbyMediaplayer(MediaPlayer.create(this, R.raw.elevatormusic));
        }
        DataModel.getLobbyMediaplayer().seekTo(DataModel.getLobbyMediaplayerLength());
        DataModel.getLobbyMediaplayer().start();


        lv = (ListView) findViewById(R.id.lobbylist);
        loadingLayout = (RelativeLayout)findViewById(R.id.loadingScreenLayout);
        lobbyHeader = (TextView) findViewById(R.id.item_class);
        font = Typeface.createFromAsset(getAssets(), "bulkypix.ttf");
        lobbyHeader.setTypeface(font);

        Log.d("LobbyActivity","Setting lobbylist in DataModel");

        DataModel.getPlayersInLobby().clear();

        DataModel.setLobbyList(this);
        handler = DataModel.getSocket();
        noLobbies = (TextView)findViewById(R.id.noLobbyTxt);

        Log.d("LobbyActivity","Starting to load lobbies");

        LoadLobbys();
    }


    @Override
    public void onPause(){
        super.onPause();
        DataModel.getLobbyMediaplayer().pause();
        wasPaused = true;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(wasPaused && handler.isAlive()){
            DataModel.getLobbyMediaplayer().start();
            LoadLobbys();
            wasPaused = false;
        }
        else if(wasPaused){
            wasPaused = false;
            LaunchAlert("Connection error","Lost connection, please log in again");
            startActivity(new Intent(this,MenuActivity.class));
            finish();
        }
    }

    //This method updates the listview from DataModel's lobbylist

    public void updateList() {
        // replace the array adapters contents with the ArrayList corresponding to the day

        lv.setVisibility(View.VISIBLE);

        List<Lobby> newList = new ArrayList<>();

        newList.addAll(DataModel.getLobbys());

        if (newList.isEmpty()) {
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


                lv.setEnabled(false);


                CountDownTimer timer = new CountDownTimer(1000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        lv.setEnabled(true);
                        System.out.println("Listview can be clicked again");
                    }
                };

                timer.start();

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
            //lv.setVisibility(View.GONE);
            noLobbies.setVisibility(View.VISIBLE);
        }

        firstLoad = true;

        Log.d("Init list","Was called");
    }

    //This method sends JSON object to server and asks for lobbies

    public void LoadLobbys(){

        noLobbies.setVisibility(View.GONE);

        DataModel.getLobbys().clear();
        String datatype = "1";

        if(DataModel.getLastSent() != null){
            try {
                JSONObject lastSent = new JSONObject(DataModel.getLastSent());
                if(lastSent.getString("Datatype").equals("1")){
                    datatype = "10";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(handler != null){
            JSONObject json = new JSONObject();
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
        DataModel.setLobbyList(null);
        finish();
    }

    //Launches a game lobby, reset game lobby data on phone and lobby list data

    public void launchGame(String name, int player){
        DataModel.setGameLobby(null);

        JSONObject json = new JSONObject();

        try {
            json.put("Datatype",4);
            json.put("Name", DataModel.getNick());
            json.put("Lobby",name);
            json.put("PlayerCount",item.getPlayerCount());
            json.put("MaxPlayerCount",item.getMaxPlayerCount());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        handler.sendData(json);
        lv.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    //Go to create game, make sure to reset lobbylist

    public void goToCreate(View v){
        createGame.setEnabled(false);

        CountDownTimer timer = new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                createGame.setEnabled(true);
            }
        };

        timer.start();

        DataModel.setLobbyList(null);
        startActivityForResult(new Intent(this,CreateGameActivity.class),AFTER_CREATE);
        finish();
    }

    //Go to menu, reset lobby list

    public void goToMenu(){
        DataModel.setLobbyList(null);
        startActivity(new Intent(this,MenuActivity.class));
        finish();
    }

    //Make sure that the right logic is called on quitting the game, kill connection

    @Override
    public void onBackPressed(){

        if(DataModel.getSocket() != null){
            System.out.println("Gets here");
            RequestHandler handler = DataModel.getSocket();
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

    public void LaunchAlert(String title, String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoadLobbys();
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.create();
        alertDialogBuilder.show();

    }

    //Here async tasks called in receiver thread is executed

    @Override
    public void processFinish(String output) {
        System.out.println("Output from processfinish in LobbyActivity: "+output);
        list = DataModel.getLobbys();

        if(output.equals("1")){
            startActivity(new Intent(this,MenuActivity.class));
        }

        else if(output.equals("2")){

            System.out.println("Gets here lobby activity");

            Bundle b = new Bundle();

            b.putString("Name",item.getName());

            DataModel.setLobbyList(null);
            Intent intent = new Intent(this,GameLobby.class);

            intent.putExtras(b);

            startActivity(intent);
        }
        else if(output.equals("3")){
            loadingLayout.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            LaunchAlert("Wrong password","You entered the a incorrect password");
        }
        else if(output.equals("4")){
            launchGame(item.getName(),Integer.parseInt(item.getMaxPlayerCount()));
        }
        else if(output.equals("5")){
            LaunchAlert("Lobby full","This lobby appears to be full already");
        }
        else if(output.equals("6")){
            LaunchAlert("Lobby doesn't exist anymore","This lobby appears to have been deleted please refresh");
        }
        else if(output.equals("7")){
            LaunchAlert("Game started","This game has already started");
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
