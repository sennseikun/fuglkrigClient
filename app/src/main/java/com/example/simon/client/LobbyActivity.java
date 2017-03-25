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

import org.json.JSONException;
import org.json.JSONObject;

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
    private RequestHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        Log.d("OnCreate","Was here");


    }
    @Override
    public void onStart(){
        super.onStart();
        Log.d("OnStart","Was here");
    }

    @Override
    public void onResume(){
        super.onResume();

        handler = PlayerModel.getSocket();

        lv = (ListView) findViewById(R.id.lobbylist);
        list = LoadLobbys();
        adapter = new customListAdapter(list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = (List<String>)parent.getItemAtPosition(position);
                if (!item.get(3).toString().equals("")){
                    launchPasswordCheck();
                }
                else {
                    launchGame(item.get(0),Integer.parseInt(item.get(1)));
                }
            }
        });
        adapter.notifyDataSetChanged();

        Log.d("OnResume","Was here");

    }

    public List<List<String>> LoadLobbys(){

        List<List<String>> lobbys = new ArrayList<>();
        String datatype = "1";

        if(PlayerModel.getLastSent() != null){
            try {
                JSONObject lastSent = new JSONObject(PlayerModel.getLastSent());
                if(lastSent.getString("Datatype").equals("1")){
                    datatype = "10";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(handler != null){
            JSONObject json = new JSONObject();

            try {
                json.put("Datatype",datatype);
                handler.sendData(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        long startTime = System.currentTimeMillis();
        Log.d("START_TIME: ",startTime+"");

        while(PlayerModel.getLobbys().isEmpty()){

            if((System.currentTimeMillis()- startTime) > 10000){
                Log.d("END_TIME",System.currentTimeMillis()+"");
                System.out.println("Quit from timer");
                break;
            }
        }

        lobbys = PlayerModel.getLobbys();

        System.out.println("Number of lobbys: "+lobbys.size());

        return lobbys;
    }

    @Override
    public void onPause(){
        super.onPause();
        PlayerModel.getLobbys().clear();

        Log.d("OnPause","Was here");

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        PlayerModel.getLobbys().clear();

        Log.d("OnDestroy","Was here");
    }
    @Override
    public void onStop(){
        super.onStop();
        PlayerModel.getLobbys().clear();

        Log.d("OnStop","Was here");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("Gets here","xD");

        /*if(requestCode == AFTER_CREATE){

            Bundle b = data.getExtras();

            String name = b.getString("Name");
            String max_players = b.getString("Players");

            String password = b.getString("Password");

            Log.d(name,max_players);

            addItem(name,max_players,password);
        }*/
    }

    public void launchGame(String name, int player){
        Bundle bundle = new Bundle();
        bundle.putString("Name",name);
        bundle.putInt("Players",player);

        Intent intent = new Intent(this,GameLobby.class);
        intent.putExtras(bundle);

        PlayerModel.getLobbys().clear();

        startActivity(intent);
    }

    public void goToCreate(View v){
        startActivityForResult(new Intent(this,CreateGameActivity.class),AFTER_CREATE);

        PlayerModel.getLobbys().clear();
    }

    public void goToMenu(){
        startActivity(new Intent(this,MenuActivity.class));

        PlayerModel.getLobbys().clear();
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
                launchGame(item.get(0),Integer.parseInt(item.get(1)));
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
