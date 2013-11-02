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
import android.widget.Toast;

public class BPuzzleActivity extends Activity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }

    public void buttonPlayPressed( View view){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity( intent );
    }

    public void buttonPuzzlesPressed( View view){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity( intent );
    }
}
