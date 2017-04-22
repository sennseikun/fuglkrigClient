package com.example.simon.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static android.graphics.Color.BLUE;

public class GameActivity extends Activity implements AsyncResponse {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataModel.setGameContext(this);
        //remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DataModel.setInGame(this);

        setContentView(new GameView(this));

        DataModel.setGamemusic(MediaPlayer.create(this, R.raw.fuglkrigcombat));
        DataModel.getGamemusic().setLooping(true);
        if(DataModel.isMusicOn()) {
            DataModel.getGamemusic().start();
        }

        DataModel.setDefeatmusic(MediaPlayer.create(this, R.raw.defeatmusic));
        DataModel.setVictorymusic(MediaPlayer.create(this, R.raw.defeatmusic));
    }
    @Override
    public void onStop(){
        DataModel.getGamemusic().stop();
        super.onStop();
    }

    @Override
    public void onDestroy(){
        DataModel.getGamemusic().stop();
        super.onDestroy();
        System.out.println("On stop called in game");
        DataModel.setInGame(null);
        UpdateServer.getInstance().stopRunning();
        GameLoopThread.getInstance().stopRunning();
        DataModel.setIsOver(false);
        DataModel.setIsVictory(false);
        finish();
    }

    @Override
    public void onPause(){
        DataModel.getGamemusic().stop();
        super.onPause();
        System.out.println("On stop called in game");
        DataModel.setInGame(null);
        UpdateServer.getInstance().stopRunning();
        GameLoopThread.getInstance().stopRunning();
        DataModel.setIsOver(false);
        DataModel.setIsVictory(false);
        finish();
    }

    @Override
    public void onBackPressed(){
        if(DataModel.isOver()){
            DataModel.getGamemusic().stop();
            startActivity(new Intent(this,LobbyActivity.class));
            finish();
        }
        else{
            launchWarning();
        }
    }

    public void launchWarning(){
        new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("You can't get back in if you leave. Are you sure you want to quit the game?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RequestHandler handler = DataModel.getSocket();

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("Datatype",14);
                            handler.sendData(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(getBaseContext(),LobbyActivity.class));
                        DataModel.getGamemusic().stop();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void processFinish(String output) {

        if(output.equals("1")){
            DataModel.getGamemusic().stop();

            UpdateServer.getInstance().stopRunning();
            GameLoopThread.getInstance().stopRunning();
            startActivity(new Intent(this,MenuActivity.class));
            DataModel.getGamemusic().stop();
            finish();
        }
    }
}
