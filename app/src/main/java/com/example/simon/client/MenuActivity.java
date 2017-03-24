package com.example.simon.client;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MenuActivity extends AppCompatActivity {

    private RequestHandler handler;
    private PlayerModel player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onClick(View v){
        startActivity(new Intent(this, GameActivity.class));
    }
    public void onClick2(View v){
        startActivity(new Intent(this,SendingActivity.class));
    }
    public void onClick3(View v){
    }

    public void initializeConnection(String name){
        JSONObject initValue = new JSONObject();
        try {
            initValue.put("Datatype","0");
            initValue.put("nick",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        handler = new RequestHandler(initValue);
        handler.start();

        boolean run = true;
        int count = 0;

        while(PlayerModel.getNick().equals("") && run){
            try {
                wait(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;

            if(count > 10){
                run = false;
            }

            System.out.println("Waiting for nick");
        }
    }

    public void launchLobby(){
        startActivity(new Intent(this,LobbyActivity.class));
    }

    public void launchToast(){
        Toast.makeText(this,"Nick taken",Toast.LENGTH_LONG).show();
    }

    public void launchNick(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        alertDialogBuilder.setPositiveButton("Move on", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String name = input.getText().toString();
                initializeConnection(name);

                if(PlayerModel.getNick().equals("ERROR")){
                    launchToast();
                }
                else{
                    launchLobby();
                }

            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setTitle("Choose name");
        alertDialogBuilder.setMessage("Choose a name for your character");
        alertDialogBuilder.create();
        alertDialogBuilder.show();

    }

}
