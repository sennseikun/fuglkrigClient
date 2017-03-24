package com.example.simon.client;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SendingActivity extends AppCompatActivity implements AsyncResponse {

    private Button btnSend;
    private EditText key;
    private EditText data;
    private RequestHandler runnable;
    private ClientTest socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending);

        JSONObject json = new JSONObject();

        try {
            json.put("Key","Data");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        runnable = new RequestHandler(json);
        runnable.start();

        Log.d("SendingActivity","Starting thread");



        btnSend = (Button)findViewById(R.id.btn_send);
        key = (EditText)findViewById(R.id.txt_key);
        data = (EditText)findViewById(R.id.txt_data);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                if(key.getText() != null && data.getText() != null){
                    try {
                        json.put(key.getText().toString(),data.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    send(json);
                }
            }
        });
    }

    public void send(JSONObject json){
        runnable.sendData(json);
    }

    @Override
    public void processFinish(String output) {
        Toast.makeText(this,output.toString(),Toast.LENGTH_LONG).show();
    }
}
