package com.example.simon.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    Button btn_sound;
    Button btn_music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        btn_sound = (Button)findViewById(R.id.btn_sound);

        btn_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSound();
                btn_sound.setEnabled(!btn_sound.isEnabled());
            }
        });

        btn_music = (Button)findViewById(R.id.btn_music);

        btn_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMusic();
                btn_music.setEnabled(!btn_music.isEnabled());
            }
        });

    }

    public void changeSound(){
        DataModel.setSound(!DataModel.isSoundOn(this),this);
    }
    public void changeMusic(){
        DataModel.setMusic(!DataModel.isMusicOn(this),this);
    }

}
