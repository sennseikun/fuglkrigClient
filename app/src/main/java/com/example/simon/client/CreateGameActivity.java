package com.example.simon.client;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateGameActivity extends AppCompatActivity {

    private Button btn_create;
    private Button btn_cancel;

    private EditText name;
    private EditText players;
    private EditText password;

    private RequestHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        btn_create = (Button)findViewById(R.id.create_button);
        btn_cancel = (Button)findViewById(R.id.cancel_button);

        name = (EditText)findViewById(R.id.gamename_edit);
        players = (EditText)findViewById(R.id.players_edit);
        password = (EditText)findViewById(R.id.password_edit);

        handler = PlayerModel.getSocket();

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
    }

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

    public void create(){

        System.out.println(players.getText().equals("2"));
        System.out.println(name.getText().equals(null));

        if(!name.getText().equals(null) && (players.getText().toString().equals("2") || players.getText().toString().equals("3") || players.getText().toString().equals("4")) && (password.length() == 4  || password.length() == 0)){

            if(PlayerModel.getSocket() == null){
                Toast.makeText(this,"Connection lost",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,MenuActivity.class));
            }

            JSONObject json = new JSONObject();

            try {
                json.put("Datatype",2);
                json.put("Name",name.getText().toString());
                json.put("Players",players.getText().toString());
                json.put("Password",password.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            handler.sendData(json);

            long startTime = System.currentTimeMillis();

            boolean success = true;

            while(PlayerModel.getGameIsValid() == 0){
                if((System.currentTimeMillis()- startTime) > 5000){
                    success = false;
                    break;
                }
            }

            if(success){

                Bundle b = new Bundle();

                b.putInt("Players",Integer.parseInt(players.getText().toString()));
                b.putString("Name",name.getText().toString());


                Intent intent = new Intent(this,GameLobby.class);

                intent.putExtras(b);

                startActivity(intent);
            }
            else{
                LaunchError();
            }
        }
        else{
            LaunchAlert();
        }
    }

    public void cancel(){
        Bundle bundle = new Bundle();

        bundle.putString("Name","CLOSE");
        bundle.putString("Players","CLOSE");
        bundle.putString("Password","CLOSE");

        Intent intent = new Intent(this,LobbyActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }
    @Override
    public void onBackPressed(){
        Bundle bundle = new Bundle();

        bundle.putString("Name","CLOSE");
        bundle.putString("Players","CLOSE");
        bundle.putString("Password","CLOSE");

        Intent intent = new Intent(this,LobbyActivity.class);
        intent.putExtras(bundle);

        startActivity(new Intent(this,LobbyActivity.class));
    }
}
