package com.example.simon.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateGameActivity extends Activity implements AsyncResponse {

    private Button btn_create;
    private Button btn_cancel;

    private EditText name;
    private EditText players;
    private EditText password;
    private TextView header;

    private RequestHandler handler;
    private RelativeLayout loadingLayout;
    private LinearLayout regularScreen;

    private Typeface font;

    private boolean wasPaused;


    /**
     * This is the method that runs when the game activity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        btn_create = (Button)findViewById(R.id.create_button);
        btn_cancel = (Button)findViewById(R.id.cancel_button);

        DataModel.setGameIsValid(0);
        DataModel.setLobbyList(null);
        DataModel.setGameLobby(null);
        DataModel.setCreateGame(this);

        name = (EditText)findViewById(R.id.gamename_edit);
        players = (EditText)findViewById(R.id.players_edit);
        password = (EditText)findViewById(R.id.password_edit);
        loadingLayout = (RelativeLayout)findViewById(R.id.create_game_loading);
        regularScreen = (LinearLayout)findViewById(R.id.create_view);
        header = (TextView)findViewById(R.id.textView5);

        font = Typeface.createFromAsset(getAssets(), "bulkypix.ttf");
        header.setTypeface(font);

        handler = DataModel.getSocket();

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(handler != null){
                    create();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        DataModel.getLobbyMediaplayer().seekTo(DataModel.getLobbyMediaplayerLength());
        if(DataModel.isMusicOn()) {
            DataModel.getLobbyMediaplayer().start();
        }

    }

    /**
     * Shows alert if there is something wrong.
     */
    public void LaunchAlert(){

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setTitle("Warning");
        alertDialogBuilder.setMessage("There was an error with the fields");
        alertDialogBuilder.create();
        alertDialogBuilder.show();

    }

    /**
     * Shows alert if there is an error at launch.
     */
    public void LaunchError(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setTitle("Error");
        alertDialogBuilder.setMessage("It was a error creating the game");
        alertDialogBuilder.create();
        alertDialogBuilder.show();

    }

    /**
     * This method create a game.
     */
    public void create(){


        if(!name.getText().equals(null) && (players.getText().toString().equals("2") || players.getText().toString().equals("3") || players.getText().toString().equals("4")) && (password.length() == 4  || password.length() == 0)){

            if(DataModel.getSocket() == null){
                Toast.makeText(this,"Connection lost",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,MenuActivity.class));
            }

            JSONObject json = new JSONObject();

            try {
                json.put("Datatype",2);
                json.put("Name",name.getText().toString());
                json.put("PlayerName", DataModel.getNick());
                json.put("Players",players.getText().toString());
                json.put("Password",password.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            handler.sendData(json);
            regularScreen.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.VISIBLE);
        }
        else{
            LaunchAlert();
        }
    }

    /**
     * This method shut down the music if the game is canceled.
     */
    public void cancel(){
        DataModel.getLobbyMediaplayer().pause();
        DataModel.getLobbyMediaplayer().seekTo(DataModel.getLobbyMediaplayerLength());
        Intent intent = new Intent(this,LobbyActivity.class);
        startActivity(intent);
    }

    /**
     * This is the alert when you launch the game.
     * @param title
     * @param message
     */
    public void LaunchAlert(String title, String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.create();
        alertDialogBuilder.show();

    }


    /**
     * This method is called when the backbutton is pressed.
     * Returns you to the lobby.
     */
    @Override
    public void onBackPressed(){
        Log.d("jeifj", "huedjie");
        DataModel.getLobbyMediaplayer().pause();
        DataModel.getLobbyMediaplayer().seekTo(DataModel.getLobbyMediaplayerLength());
        startActivity(new Intent(this,LobbyActivity.class));

        finish();
    }

    /**
     * This method is called when the game is finished.
     * And returns you to the lobby.
     * @param output
     */
    @Override
    public void processFinish(String output) {

        if(DataModel.getGameIsValid() == 1){
            Bundle b = new Bundle();

            Lobby l = new Lobby(name.getText().toString(),"1",players.getText().toString(),password.getText().toString());

            DataModel.addLobby(l);
            DataModel.addPlayerToLobby(DataModel.getNick());
            b.putString("Name",name.getText().toString());
            Intent intent = new Intent(this,GameLobby.class);
            intent.putExtras(b);

            startActivity(intent);
        }
        else{
            LaunchError();
        }
    }

    /**
     * This method is called when the game is paused.
     * Shut down the music.
     */
    @Override
    protected void onPause(){
        super.onPause();
        DataModel.getLobbyMediaplayer().pause();
        wasPaused = true;
    }

    /**
     * This method is called when you resume the game.
     * Start the mediaplayer if the lobby is still active.
     * Gives an error msg if the connection is lost.
     */
    @Override
    public void onResume(){
        super.onResume();
        if(wasPaused && handler.isAlive()){
            DataModel.getLobbyMediaplayer().start();
            wasPaused = false;
        }
        else if(wasPaused){
            wasPaused = false;
            LaunchAlert("Connection error","Lost connection, please log in again");
            startActivity(new Intent(this,MenuActivity.class));
            finish();
        }
    }
}
