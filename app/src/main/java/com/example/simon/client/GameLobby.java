package com.example.simon.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameLobby extends AppCompatActivity {

    private TextView txtName;
    private TextView txtPlayers;
    private Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        Bundle bundle = getIntent().getExtras();

        txtName = (TextView)findViewById(R.id.txt_name);
        txtPlayers = (TextView)findViewById(R.id.txt_count);
        btn_cancel = (Button)findViewById(R.id.cancel_gamelobby);

        txtName.setText(bundle.getString("Name"));
        txtPlayers.setText("1/"+bundle.get("Players"));

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

    }

    private void cancel(){
        startActivity(new Intent(this,LobbyActivity.class));
    }
}
