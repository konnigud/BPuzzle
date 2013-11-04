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
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class BPuzzleActivity extends Activity {

    public PuzzlesDB puzzleDB;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        puzzleDB = new PuzzlesDB(this);
        if (puzzleDB.queryJustId(1).getCount() < 1){
            System.out.println("Database crated and populated");
            try {
                InputStream in = this.getAssets().open("challenge_classic40.xml");
                PuzzleXmlParser pXmlP = new PuzzleXmlParser();
                System.out.println(puzzleDB.insertPuzzles(pXmlP.parse(in)));
            }catch (IOException ioe){
                System.out.println(ioe.getMessage());
                ioe.printStackTrace();
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error loading puzzles",Toast.LENGTH_LONG);
                System.out.println(e.getMessage());
                e.printStackTrace();

            }
            puzzleDB.openPuzzle(1);
        }
        else{
            System.out.println("Database already exists");
        }
        setContentView(R.layout.main);

    }

    public void buttonPlayPressed( View view){
        Intent intent = new Intent(this, GameActivity.class);

        Cursor cursor = puzzleDB.getHighestOpen();
        cursor.moveToFirst();
        int puzzle = cursor.getInt(cursor.getPosition());

        Bundle extras = new Bundle();
        extras.putSerializable("puzzle_id",puzzle);
        intent.putExtras(extras);

        startActivity(intent);
    }

    public void buttonPuzzlesPressed( View view){
        Intent intent = new Intent(this, PuzzlesActivity.class);
        startActivity( intent );
    }

    public void buttonOptionsPressed(View view){
        Intent intent = new Intent(this,OptionsActivity.class);
        startActivity(intent);
    }
}
