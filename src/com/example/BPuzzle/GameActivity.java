package com.example.BPuzzle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Konráð
 * Date: 27.10.2013
 * Time: 19:35
 * To change this template use File | Settings | File Templates.
 */
public class GameActivity extends Activity {



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        int puzzle = Integer.parseInt(extras.getSerializable("puzzle_id").toString());

        PuzzlesDB puzzlesDB = new PuzzlesDB(this);

        if(extras.getSerializable("score") != null){
            int newScore = Integer.parseInt(extras.getSerializable("score").toString());
            System.out.println("newScore: "+newScore);
            puzzlesDB.updateScore(puzzle-1,newScore);
        }

        puzzlesDB.openPuzzle(puzzle);
        Cursor cursor = puzzlesDB.queryPuzzle(puzzle);
        if(cursor.moveToFirst()){
            String setup = cursor.getString(cursor.getColumnIndex("setup"));
            int score = cursor.getInt(cursor.getColumnIndex("score"));
            SharedPreferences.Editor editor = getSharedPreferences("myState",MODE_MULTI_PROCESS).edit();
            editor.clear();
            editor.putString("setup", setup);
            editor.putInt("puzzle", puzzle);
            editor.putInt("score",score);
            editor.commit();
            cursor.close();
            setContentView(R.layout.game);
        }
        else{
            System.out.println("Could not load puzzle "+puzzle);
            Intent intent = new Intent(this, PuzzlesActivity.class);
            Toast.makeText(getApplicationContext(),"Could not load puzzle "+puzzle,Toast.LENGTH_LONG).show();
            startActivity(intent);
        }

    }
}
