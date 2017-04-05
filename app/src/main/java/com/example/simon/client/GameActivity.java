package com.example.simon.client;

import android.app.Activity;
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
    public void processFinish(String output) {

        if(output.equals("1")){
            UpdateServer.getInstance().stopRunning();
            startActivity(new Intent(this,MenuActivity.class));
            finish();
        }
    }
}
