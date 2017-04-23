package com.example.simon.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

    private Button btn_sound;
    private Button btn_music;
    private Button btn_changeName;

    /**
     * This is the method that is called on the creation of the activity for the settings.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        DataModel.getMenumusic().setLooping(true);
        DataModel.getMenumusic().seekTo(DataModel.getMenumusiclength());


        btn_sound = (Button)findViewById(R.id.btn_sound);

        btn_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSound();

            }
        });

        btn_music = (Button)findViewById(R.id.btn_music);

        btn_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMusic();

            }
        });

        btn_changeName = (Button)findViewById(R.id.btn_change);

        btn_changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNick();
            }
        });

        if(DataModel.isMusicOn()) {
            btn_music.setBackgroundResource(R.drawable.whenmusiconpressed);
            DataModel.getMenumusic().start();
        }
        else{
            btn_music.setBackgroundResource(R.drawable.whenmusicoffpressed);
        }

        if(DataModel.isSoundOn()){
            btn_sound.setBackgroundResource(R.drawable.whensoundonpressed);
        }
        else {
            btn_sound.setBackgroundResource(R.drawable.whensoundoffpressed);
        }
    }

    /**
     * This method sets if you want sounds or not.
     */
    public void changeSound(){
        DataModel.setSound(!DataModel.isSoundOn());
        DataModel.setSavedSound(this,DataModel.isSoundOn());
        /**
         * Change button layout here
         */
        if(DataModel.isSoundOn()){
            btn_sound.setBackgroundResource(R.drawable.whensoundonpressed);
        }
        else {
            btn_sound.setBackgroundResource(R.drawable.whensoundoffpressed);
        }

    }

    /**
     * This method sets if you want the music or not.
     */
    public void changeMusic(){
        DataModel.setMusic(!DataModel.isMusicOn());
        DataModel.setSavedMusic(this,DataModel.isMusicOn());
        if(DataModel.isMusicOn()) {
            btn_music.setBackgroundResource(R.drawable.whenmusiconpressed);
            DataModel.setMenumusiclength(DataModel.getMenumusic().getCurrentPosition());
            DataModel.getMenumusic().pause();
        }
        else{
            btn_music.setBackgroundResource(R.drawable.whenmusicoffpressed);
            DataModel.getMenumusic().seekTo(DataModel.getMenumusiclength());
            DataModel.getMenumusic().start();
        }


    }

    /**
     * This method gives you the opertunity to change nick.
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
                setPrefName(name,getBaseContext());
                Toast.makeText(getBaseContext(),"Name successfully changed",Toast.LENGTH_LONG).show();
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

    /**
     * @param context
     * @return your nick thats stored in the application.
     */
    public String getPrefName(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString("Name", "");
    }

    /**
     * Sets the value for your nick so you got the same all the time.
     * @param prefName
     * @param context
     */
    public void setPrefName(String prefName, Context context){
        SharedPreferences SPname = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SPname.edit();
        editor.putString("Name", prefName);
        editor.commit();
    }

    /**
     * This method is called when you push the back button and returns you to the menu.
     */
    @Override
    public void onBackPressed(){
        startActivity(new Intent(this,MenuActivity.class));
        finish();
    }

    /**
     * This method pauses the game if you close the screen.
     */
    @Override
    protected void onPause(){
        super.onPause();
        DataModel.setMenumusiclength(DataModel.getMenumusic().getCurrentPosition());
        DataModel.getMenumusic().pause();
    }

}
