package com.example.simon.client;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class LobbyActivity extends AppCompatActivity {

    private ListView lv;
    private List<List<String>> list;
    private customListAdapter adapter;
    private final int AFTER_CREATE = 22;
    private List<String> item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        lv = (ListView) findViewById(R.id.lobbylist);

        list = new ArrayList<>();

        System.out.println("Name: " + PlayerModel.getNick());

//        for(int i = 0; i < 5; i++){
//            List<String> data = new ArrayList<>();
//
//            data.add("Game: "+i);
//            data.add(i+"");
//
//            list.add(data);
//        }


        Log.d("LobbyActivity","Gets here");

        adapter = new customListAdapter(list);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = (List<String>)parent.getItemAtPosition(position);
                if (!item.get(2).equals("")){
                    launchPasswordCheck();
                }
                else {
                    launchGame(item.get(0),item.get(1));
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("Gets here","xD");

        if(requestCode == AFTER_CREATE){

            Bundle b = data.getExtras();

            String name = b.getString("Name");
            String max_players = b.getString("Players");

            String password = b.getString("Password");

            Log.d(name,max_players);

            addItem(name,max_players,password);
        }
    }

    public void launchGame(String name, String player){
        Bundle bundle = new Bundle();
        bundle.putString("Name",name);
        bundle.putString("Players",player);

        Intent intent = new Intent(this,GameLobby.class);
        intent.putExtras(bundle);



        startActivity(intent);
    }

    public void goToCreate(View v){
        startActivityForResult(new Intent(this,CreateGameActivity.class),AFTER_CREATE);
    }

    public void goToMenu(){
        startActivity(new Intent(this,MenuActivity.class));

    }

    @Override
    public void onBackPressed(){

        if(PlayerModel.getSocket() != null){
            System.out.println("Gets here");
            RequestHandler handler = PlayerModel.getSocket();
            handler.stopSending();
        }

        goToMenu();
    }

    public void addItem(String name, String max_players, String password) {

        List<String> item = new ArrayList<>();

        item.add(name);
        item.add(max_players);
        item.add(password);

        list.add(item);
        adapter.notifyDataSetChanged();
    }

    public void launchPasswordCheck(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        alertDialogBuilder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Check password with server here
                launchGame(item.get(0),item.get(1));
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
}
