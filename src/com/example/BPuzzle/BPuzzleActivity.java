package com.example.BPuzzle;

/**
 * Created with IntelliJ IDEA.
 * User: Konráð
 * Date: 27.10.2013
 * Time: 19:39
 * To change this template use File | Settings | File Templates.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BPuzzleActivity extends Activity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }

    public void buttonPressed( View view){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity( intent );
    }
}
/*
setContentView(R.layout.activity_play_game);
        Intent intent = getIntent();
      //  String puzzleNr = intent.getStringExtra(MainActivity.PUZZLE_NUMBER);
        m_bv = (BoardView) findViewById( R.id.boardView );
        m_bv.setMoveEventHandler( new OnMoveEventHandler(){
            @Override
            public void onMove(int col, int row) {
                String actionStr = "(" + col + "," + row + ")";
                }

        });

*/