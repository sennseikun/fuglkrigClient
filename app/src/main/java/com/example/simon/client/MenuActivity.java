package com.example.simon.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
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

public class MenuActivity extends Activity {

    private RequestHandler handler;
    private PlayerModel player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        PlayerModel.setNick("");
        PlayerModel.setLobbyList(null);
        PlayerModel.setGameLobby(null);

        if(PlayerModel.getSocket() != null){
            RequestHandler s = PlayerModel.getSocket();
            JSONObject json = new JSONObject();
            try {
                json.put("Datatype",5);
                s.sendData(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            s.stopSending();
        }

    }

    public void onClick(View v){
        startActivity(new Intent(this, GameActivity.class));
    }
    public void onClick2(View v){ launchNick();}
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
        long startTime = System.currentTimeMillis();

        while(PlayerModel.getNick().equals("") && run){

            if((System.currentTimeMillis()- startTime) > 10000){
                System.out.println("Quit from timer");
                JSONObject removeReq = new JSONObject();
                try {
                    removeReq.put("Datatype","5");
                    handler.sendData(removeReq);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void launchLobby(){
        startActivity(new Intent(this,LobbyActivity.class));
    }

    public void launchToast(){
        Toast.makeText(this,"Illegal name",Toast.LENGTH_LONG).show();
    }
    public void launchServerError(){
        new AlertDialog.Builder(this)
                .setTitle("Error connecting to server")
                .setMessage("The server might be down")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void launchNick(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogBuilder.setPositiveButton("Move on", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String name = input.getText().toString();
                initializeConnection(name);

                if(PlayerModel.getNick().equals("ERROR")){
                    launchToast();
                }
                else if(!PlayerModel.getNick().equals("")){
                    launchLobby();
                }
                else{
                    dialog.dismiss();
                    launchServerError();
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
        alertDialogBuilder.setView(input);

        alertDialogBuilder.create();
        alertDialogBuilder.show();

    }

}
