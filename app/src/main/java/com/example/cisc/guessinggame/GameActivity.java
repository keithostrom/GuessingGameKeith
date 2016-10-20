package com.example.cisc.guessinggame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends Activity implements View.OnClickListener{
    SharedPreferences myPrefsObject;
    SharedPreferences.Editor editor;
    String myPrefsFName = "MyData";
    String intName = "MyHighScore";
    Button [] buttonAnswers;
    Button buttonObjectAgain;
    TextView textObjectLevel;
    TextView textObjectScore;
    TextView textObjectStatus1,textObjectStatus2;
    int levelPoints;
    int guessedNumber;
    int gameLevel = 1;
    int gameScore = 0;
    int highScore = 0;
    int theRandomNumber;
    Random randInt = new Random();
    Animation wobble,splat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        buttonAnswers = new Button[10]; //Don't forget zero based
        buttonAnswers[0] = (Button) findViewById(R.id.button1);
        buttonAnswers[1] = (Button) findViewById(R.id.button2);
        buttonAnswers[2] = (Button) findViewById(R.id.button3);
        buttonAnswers[3] = (Button) findViewById(R.id.button4);
        buttonAnswers[4] = (Button) findViewById(R.id.button5);
        buttonAnswers[5] = (Button) findViewById(R.id.button6);
        buttonAnswers[6] = (Button) findViewById(R.id.button7);
        buttonAnswers[7] = (Button) findViewById(R.id.button8);
        buttonAnswers[8] = (Button) findViewById(R.id.button9);
        buttonAnswers[9] = (Button) findViewById(R.id.button10);
        for(int a=0;a<10;a++){
            buttonAnswers[a].setOnClickListener(this);
        }
        textObjectLevel = (TextView) findViewById(R.id.textLevel);
        textObjectScore = (TextView) findViewById(R.id.textScore);

        textObjectStatus1 = (TextView) findViewById(R.id.textStatus1);
        textObjectStatus1.setText("Please guess a number");

        textObjectStatus2 = (TextView) findViewById(R.id.textStatus2);
        textObjectStatus2.setText("");

        buttonObjectAgain = (Button) findViewById(R.id.buttonAgain);
        buttonObjectAgain.setVisibility(View.INVISIBLE);
        buttonObjectAgain.setOnClickListener(this);

        // Prefs stuff
        myPrefsObject = getSharedPreferences(myPrefsFName,MODE_PRIVATE);
        editor = myPrefsObject.edit();
        highScore = myPrefsObject.getInt(intName,0);

        wobble = AnimationUtils.loadAnimation(this,R.anim.wobble);
        splat = AnimationUtils.loadAnimation(this,R.anim.splat);

        prepareLevel();

    } // End onCreate

    @Override
    public void onClick(View view) {
        Button tempButton;
        String theText;

        switch( gameLevel ) {
            case 1: // Game in progress, level 1
            case 2: // Game in progress, level 2
            case 3: // Game in progress, level 3
                tempButton = (Button) findViewById(view.getId());
                theText = (String) tempButton.getText();
                guessedNumber = Integer.parseInt(theText);
                Log.i("info","Guessed Number: "+guessedNumber+" theRandomNumber: "+theRandomNumber);
                if(guessedNumber == theRandomNumber){
                    Log.i("info","The numbers match");
                    tempButton.startAnimation(splat);
                    MainActivity.mySoundPool.play(MainActivity.soundHappy,1,1,0,0,(float)0.5);
                    gameScore += levelPoints;
                    textObjectScore.setText("Score: "+gameScore);
                    gameLevel++;
                    Log.i("info","game level now: "+gameLevel);
                    prepareLevel();
                } // End if(guessedNumber == theRandomNumber)
                else {  // guessedNumber != theRandomNumber
                    tempButton.setVisibility(View.INVISIBLE);
                    MainActivity.mySoundPool.play(MainActivity.soundUnhappy,1,1,0,0,(float)0.5);
                    tempButton.startAnimation(wobble);
                    --levelPoints;
                    //Toast.makeText(getApplicationContext(),"Sorry, try again.",Toast.LENGTH_SHORT).show();
                } // End guessedNumber != theRandomNumber
                break;
            case 4: // Done with all 3 levels check results
                if( view.getId() == R.id.buttonAgain) {
                    Intent myIntent = new Intent(this, MainActivity.class);
                    startActivity( myIntent);
                }
                break;

        } // End switch (gameLevel)

    } // End onClick

    void prepareLevel(){
        Log.i("info","Calling prepareLevel");
        textObjectScore.setText("Score: "+gameScore);
        textObjectLevel.setText("Level: "+gameLevel);
        switch(gameLevel){
            case 1:
                theRandomNumber = randInt.nextInt(3);
                theRandomNumber++;
                levelPoints = 3;
                for(int a=0;a<10;a++){
                    if(a<3) {
                        buttonAnswers[a].setVisibility(View.VISIBLE);
                    }
                    else{
                        buttonAnswers[a].setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case 2:
                theRandomNumber = randInt.nextInt(5);
                theRandomNumber++;
                levelPoints = 5;
                for(int a=0;a<10;a++){
                    if(a<5) {
                        buttonAnswers[a].setVisibility(View.VISIBLE);
                    }
                    else{
                        buttonAnswers[a].setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case 3:
                theRandomNumber = randInt.nextInt(10);
                theRandomNumber++;
                levelPoints = 10;
                for(int a=0;a<10;a++){
                    buttonAnswers[a].setVisibility(View.VISIBLE);
                }
                break;
            case 4:
                // Hide all and show Replay button
                for(int a=0;a<10;a++) {
                    buttonAnswers[a].setVisibility(View.INVISIBLE);
                }
                textObjectStatus1.setText("Game over");
                buttonObjectAgain.setVisibility(View.VISIBLE);
                textObjectLevel.setVisibility(View.INVISIBLE);

                // Check high score
                Log.i("info","Check high score");
                if(gameScore>highScore) {
                    Log.i("info","Set new high score");
                    highScore = gameScore;

                    //Save high score in prefs
                    editor.putInt(intName,highScore); // High score exists now
                    editor.commit(); // Write it
                    textObjectStatus2.setText("***   New High Score   ***");
                    MainActivity.mySoundPool.play(MainActivity.soundHighScore,1,1,0,0,(float)0.5);
                }
                else{
                    textObjectStatus2.setText("      You win      ");
                }

        } // End switch
        Log.i("info","theRandomNumber: "+theRandomNumber);
    } // End prepareLevel

} // End GameActivity
