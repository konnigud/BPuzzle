package com.example.BPuzzle;

import android.app.Activity;
import android.os.Bundle;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Konráð
 * Date: 27.10.2013
 * Time: 19:35
 * To change this template use File | Settings | File Templates.
 */
public class GameActivity extends Activity {
    private GameView gv;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        gv = (GameView) findViewById( R.id.gameView );
        gv.setMoveEventHandler( new OnMoveEventHandler(){
            @Override
            public void onMove(int col, int row) {
                String actionStr = "(" + col + "," + row + ")";
            }

        });
    }
}
