package com.example.cisc.guessinggame;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener{
    SharedPreferences myPrefsObject;
    SharedPreferences.Editor editor;
    String myPrefsFName = "MyData";
    String intName = "MyHighScore";
    int highScore;

    AssetManager myAssets;
    AssetFileDescriptor myFileDescriptor;
    public static SoundPool mySoundPool;
    public static int soundHappy     = -1;
    public static int soundUnhappy   = -1;
    public static int soundJump      = -1;
    public static int soundHighScore = -1;

    Button buttonObjectPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Read preferences
        myPrefsObject = getSharedPreferences(myPrefsFName,MODE_PRIVATE);
        highScore = myPrefsObject.getInt(intName,-1);
        Log.i("info","Read of highScore before update prefs: "+highScore);
        if( highScore == -1) {
            //No value, So write default of zero.
            highScore = 0;
            editor = myPrefsObject.edit();
            editor.putInt(intName,highScore); // High score exists now
            editor.commit(); // Write it
        }

        Log.i("info","Read of highScore after update prefs: "+highScore);
        TextView highScoreObject = (TextView) findViewById(R.id.highScore);
        highScoreObject.setText("High Score: "+highScore);

        // Load Sounds
        mySoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        myAssets = getAssets();
        soundHappy   = loadSound("happy.wav");
        soundUnhappy = loadSound("unhappy.wav");
        soundJump = loadSound("Jump.wav");
        soundHighScore = loadSound("Ocean.wav");

        // Init buttons
        buttonObjectPlay = (Button) findViewById(R.id.buttonPlay);
        buttonObjectPlay.setOnClickListener(this);

    } //End onCreate

    public int loadSound(String theSound){
        int tempVal = -1;
        try {
            myFileDescriptor = myAssets.openFd(theSound);
            tempVal = mySoundPool.load(myFileDescriptor,0);
        }
        catch( IOException e) {
            Toast.makeText(getApplicationContext(),"Missing Asset: "+theSound,Toast.LENGTH_LONG).show();
        }
        return tempVal;
    } // End loadsound()

    @Override
    public void onClick(View view) {
        switch( view.getId()){
            case R.id.buttonPlay:
                Log.i("info","onClick.buttonPlay");
                mySoundPool.play(soundJump,1,1,0,0,(float)1.0);
                Intent theIntent = new Intent(this,GameActivity.class);
                startActivity(theIntent);
                break;
        } //End switch
    } //End onClick
} //End MainActivity
