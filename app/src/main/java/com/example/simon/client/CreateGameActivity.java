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

public class CreateGameActivity extends AppCompatActivity {

    private Button btn_create;
    private Button btn_cancel;

    private EditText name;
    private EditText players;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        btn_create = (Button)findViewById(R.id.create_button);
        btn_cancel = (Button)findViewById(R.id.cancel_button);

        name = (EditText)findViewById(R.id.gamename_edit);
        players = (EditText)findViewById(R.id.players_edit);
        password = (EditText)findViewById(R.id.password_edit);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
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

    public void create(){

        System.out.println(players.getText().equals("2"));
        System.out.println(name.getText().equals(null));

        if(!name.getText().equals(null) && (players.getText().toString().equals("2") || players.getText().toString().equals("3") || players.getText().toString().equals("4")) && (password.length() == 4  || password.length() == 0)){
            Bundle bundle = new Bundle();

            bundle.putString("Name",name.getText()+"");
            bundle.putString("Players",players.getText()+"");
            bundle.putString("Password",password.getText()+"");

            Intent intent = new Intent(this,LobbyActivity.class);
            intent.putExtras(bundle);

            setResult(Activity.RESULT_OK, intent);
            finish();
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
