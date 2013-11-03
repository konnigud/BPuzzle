package com.example.BPuzzle;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Konráð
 * Date: 2.11.2013
 * Time: 00:19
 * To change this template use File | Settings | File Templates.
 */
public class PuzzlesActivity extends ListActivity {

    private PuzzlesDB puzzlesDB;
    private SimpleCursorAdapter mCursorAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        puzzlesDB = new PuzzlesDB(this);
        Cursor cursor = puzzlesDB.queryPuzzles();
        String cols[] = DBHelper.TablePuzzlesCol;
        String from[] = {cols[3],cols[0],cols[1]};
        int to [] = {R.id.p_image,R.id.p_id,R.id.p_level};
        startManagingCursor(cursor);
        mCursorAdapter = new SimpleCursorAdapter(this,R.layout.puzzlerow,cursor,from,to);

        mCursorAdapter.setViewBinder( new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                System.out.println("setViewValue i:" +i);
                if ( i==3 ) {
                    System.out.println("cursor.getInt(i): "+cursor.getInt(i));
                    ((ImageView) view).setImageResource(
                            (cursor.getInt(i) == 0) ? R.drawable.lock : R.drawable.hero);
                    return true;
                }

                return false;
            }
        });
        setListAdapter(mCursorAdapter);
    }


    @Override
    protected void onListItemClick(ListView l,View v,int p,long id){
        System.out.println("p: "+p);
        Intent intent = new Intent(this, GameActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("puzzle_id",p+1);
        intent.putExtras(extras);
        startActivity( intent );

    }
}