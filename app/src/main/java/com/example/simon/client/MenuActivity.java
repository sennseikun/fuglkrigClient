package com.example.simon.client;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.DTDHandler;

import java.util.List;
import java.util.UUID;

public class MenuActivity extends Activity {

    private RequestHandler handler;
    Context context = this;
    String PrefName = "Name";
    Button startButton;
    Button settingsButton;
    String uniqueID;
    boolean ispaused;


    /**
     * This is the method that is called when the menu is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);

        DataModel.setNick("");
        DataModel.setLobbyList(null);
        DataModel.setGameLobby(null);


        if(getPrefID(this).equals("")){
            uniqueID = UUID.randomUUID().toString();
            setPrefID(uniqueID,this);
        }

        System.out.println("Unique ID: " + getPrefID(this));

        DataModel.setDiesound(MediaPlayer.create(this, R.raw.hitsound));

        DataModel.setMenumusic(MediaPlayer.create(this, R.raw.birds));
        DataModel.getMenumusic().seekTo(DataModel.getLobbyMediaplayerLength());
        DataModel.getMenumusic().setLooping(true);
        if(DataModel.isMusicOn()) {
            DataModel.getMenumusic().start();
        }
        ispaused = false;

        startButton = (Button)findViewById(R.id.button3);
        settingsButton = (Button)findViewById(R.id.button4);

        settingsButton.setEnabled(true);
        startButton.setEnabled(true);

        if(DataModel.getSocket() != null){
            RequestHandler s = DataModel.getSocket();
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

    /**
     * This is the method that is called when you pause the game in the menu.
     */
    @Override
    protected void onPause(){
        super.onPause();
//        if(this.isFinishing()){
//            mediaPlayer.pause();
//        }
//        Context context = getApplicationContext();
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
//        if (!taskInfo.isEmpty()) {
//            ComponentName topActivity = taskInfo.get(0).topActivity;
//            if (!topActivity.getPackageName().equals(context.getPackageName())) {
//                mediaPlayer.stop();
//            }
//        }
        DataModel.getMenumusic().pause();
        DataModel.setMenumusiclength(DataModel.getMenumusic().getCurrentPosition());
        ispaused = true;

    }

    /**
     * This is the method that is called when you try to run the app after you paused it in the menu.
     */
    @Override
    protected void onResume(){
        super.onResume();
        if(ispaused) {
            DataModel.getMenumusic().start();
        }
    }

    /**
     * Sets the prefered name for the player.
     * @param prefName
     * @param context
     */
    public void setPrefName(String prefName, Context context){
        SharedPreferences SPname = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SPname.edit();
        editor.putString(PrefName, prefName);
        editor.commit();
    }

    /**
     * Sets the prefered ID for a player.
     * @param prefID
     * @param context
     */
    public void setPrefID(String prefID, Context context){
        SharedPreferences SPID = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SPID.edit();
        editor.putString("GUID", prefID);
        editor.commit();
    }

    /**
     * @param context
     * @return return the prefered ID.
     */
    public String getPrefID(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString("GUID", "");
    }

    /**
     * Disable the buttons.
     */
    public void disableButton(){
        startButton.setEnabled(false);
        settingsButton.setEnabled(false);
    }

    /**
     * Enable the buttons.
     */
    public void enableButton(){
        startButton.setEnabled(true);
        settingsButton.setEnabled(true);
    }

    /**
     * @param context
     * @return the prefered name of the player.
     */
    public String getPrefName(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PrefName, "");
    }

    /**
     * This is the method that is called when you press the back button.
     */
    @Override
    public void onBackPressed(){
        finish();
    }


    /**
     * This method is called when you click button #1.
     * @param v
     */
    public void onClick(View v){
        DataModel.getMenumusic().stop();
        startActivity(new Intent(this, GameActivity.class));

    }

    /**
     * This method is called when you click button #2.
     * @param v
     */
    public void onClick2(View v){
        disableButton();
        startActivity(new Intent(this,SettingsActivity.class));
        finish();
    }

    /**
     * This method is called when you click button #3.
     * @param v
     */
    public void onClick3(View v){
        disableButton();

        CountDownTimer timer = new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                enableButton();
            }
        };

        timer.start();

        if(getPrefName(context).isEmpty()) {
            launchNick();
        }
        else {
            String nick = getPrefName(context);
            initializeConnection(nick);
            if(DataModel.getNick().equals("ERROR")){
                launchToast();
                restart();
            }
            else if(DataModel.getNick().equals("")){
                launchServerError();
            }
            else{
                launchLobby();
            }
        }
    }

    /**
     * This method intitialize the connection to the server.
     * @param name
     */
    public void initializeConnection(String name){
        JSONObject initValue = new JSONObject();
        try {

            System.out.println("Sent unique ID: " + getPrefID(this));

            initValue.put("Datatype","0");
            initValue.put("nick",name);
            initValue.put("UniqueID",getPrefID(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        handler = new RequestHandler(initValue);
        handler.start();

        boolean run = true;
        long startTime = System.currentTimeMillis();

        while(DataModel.getNick().equals("") && run){

            if((System.currentTimeMillis()- startTime) > 10000){
                System.out.println("Quit from timer");
                JSONObject removeReq = new JSONObject();
                try {
                    removeReq.put("Datatype","5");
                    System.out.println("Gets into here");
                    handler.sendData(removeReq);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * This method launches the lobby.
     */
    public void launchLobby(){

        DataModel.getMenumusic().stop();
        startActivity(new Intent(this,LobbyActivity.class));
    }

    /**
     * This method launches an error msg.
     */
    public void launchToast(){
        Toast.makeText(this,"Illegal name",Toast.LENGTH_LONG).show();
    }

    /**
     * This method launches the server error msg.
     */
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

    /**
     * This method resets the nick of the player.
     */
    public void restart(){
        DataModel.setNick("");
        setPrefName("",getBaseContext());
        //startActivity(new Intent(this,MenuActivity.class));
        //finish();
        handler.stopSending();
    }

    /**
     * This method launches the nicks of a player.
     */
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
                setPrefName(input.getText().toString(), context);
                if(DataModel.getNick().equals("ERROR")){
                    launchToast();
                    restart();
                }
                else if(!DataModel.getNick().equals("")){
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
