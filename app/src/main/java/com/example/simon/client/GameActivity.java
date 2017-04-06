package com.example.simon.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

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
    }
    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy(){
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
                        startActivity(new Intent(getBaseContext(),LobbyActivity.class));
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
            UpdateServer.getInstance().stopRunning();
            startActivity(new Intent(this,MenuActivity.class));
            finish();
        }
    }
}
