package com.example.BPuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: Konráð
 * Date: 4.11.2013
 * Time: 21:10
 * To change this template use File | Settings | File Templates.
 */
public class OptionsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
    }

    public void buttonResetGamePressed(View view){
        PuzzlesDB db = new PuzzlesDB(this);
        long reset = db.resetDatabase();
        long open = db.openPuzzle(1);

        if(reset < 1 || open < 1){
            Toast.makeText(this,"Error while game was reset!!",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Game Reset", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this,BPuzzleActivity.class);
            startActivity(intent);
        }


    }
}