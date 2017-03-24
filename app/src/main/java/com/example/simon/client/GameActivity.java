package com.example.simon.client;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import static android.graphics.Color.BLUE;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));

    }
    //User input
    @Override
    public boolean onTouchEvent(MotionEvent me) {
        switch(me.getAction()){
            case  MotionEvent.ACTION_DOWN:
                Log.i("CLICK", "X " + me.getX());
                Log.i("CLICK","Y " + me.getY());
                break;
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_1:
                Log.i("BUTTON CLICK", "button_1");
                break;
            case R.id.button_2:
                Log.i("BUTTON CLICK", "button_2");
                break;
            case R.id.button_3:
                Log.i("BUTTON CLICK", "button_3");
        }
    }
}
