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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btn_sound = (Button)findViewById(R.id.btn_sound);

        btn_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSound();
                System.out.println("Sound: " + DataModel.isSoundOn(getBaseContext()));
            }
        });

        btn_music = (Button)findViewById(R.id.btn_music);

        btn_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMusic();
                System.out.println("Music: " + DataModel.isMusicOn(getBaseContext()));
            }
        });

        btn_changeName = (Button)findViewById(R.id.btn_change);

        btn_changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNick();
            }
        });
    }

    public void changeSound(){
        DataModel.setSound(!DataModel.isSoundOn(this),this);
        //Change button layout here
    }
    public void changeMusic(){
        DataModel.setMusic(!DataModel.isMusicOn(this),this);
        //Change button layout here
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

    public String getPrefName(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString("Name", "");
    }

    public void setPrefName(String prefName, Context context){
        SharedPreferences SPname = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SPname.edit();
        editor.putString("Name", prefName);
        editor.commit();
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this,MenuActivity.class));
        finish();
    }

}
