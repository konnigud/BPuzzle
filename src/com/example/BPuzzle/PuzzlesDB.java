package com.example.BPuzzle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Konráð
 * Date: 2.11.2013
 * Time: 15:03
 * To change this template use File | Settings | File Templates.
 */
public class PuzzlesDB {
    SQLiteDatabase db;
    DBHelper dbHelper;
    Context context;

    public PuzzlesDB( Context c){
        context = c;
        dbHelper = new DBHelper(c);
    }

    public PuzzlesDB openToRead(){
        db = dbHelper.getReadableDatabase();
        return this;
    }

    public PuzzlesDB openToWrite(){
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        db.close();
    }

    public long insertPuzzles(ArrayList<Puzzle> puzzles){
        String[] cols = DBHelper.TablePuzzlesCol;
        boolean success = true;
        openToWrite();
        for (Puzzle p : puzzles){
            ContentValues cv = new ContentValues();
            cv.put(cols[1],((Integer)p.mLevel).toString());
            cv.put(cols[2], p.mSetup);
            cv.put(cols[3], "0");
            long value =  db.insert(DBHelper.TablePuzzles,null,cv);
            if(value < 0 && success){
                success = false;
            }
        }
        return (success) ? 0 : -1;
    }

    public long insertPuzzle(int level,String setup){
        String[] cols = DBHelper.TablePuzzlesCol;
        ContentValues cv = new ContentValues();
        cv.put(cols[1],((Integer)level).toString());
        cv.put(cols[2], setup);
        cv.put(cols[3], "0");
        openToWrite();
        long value = db.insert(DBHelper.TablePuzzles,null,cv);
        close();
        return value;
    }

    public long updatePuzzle(int id,int level,String setup,boolean open){
        String[] cols = DBHelper.TablePuzzlesCol;
        ContentValues cv = new ContentValues();
        cv.put(cols[1],((Integer)level).toString());
        cv.put(cols[2], setup);
        cv.put(cols[3], open ? "1":"0");
        openToWrite();
        long value = db.update(DBHelper.TablePuzzles,cv,cols[0] +"="+ id,null);
        close();
        return value;
    }

    public Cursor queryPuzzles(){
        openToRead();
        return db.query(DBHelper.TablePuzzles,DBHelper.TablePuzzlesCol,null,null,null,null,null);
    }

    public Cursor queryPuzzle(int id){
        openToRead();
        String[] cols = DBHelper.TablePuzzlesCol;
        return db.query(DBHelper.TablePuzzles,cols,cols[0] +"="+ id,null,null,null,null);
    }

    public Cursor queryJustId(int id){
        openToRead();
        String[] cols = {"_id"};
        return db.query(DBHelper.TablePuzzles,cols,cols[0]+"="+id,null,null,null,null);
    }

    public long openPuzzle(int id){
        String filter = DBHelper.TablePuzzlesCol[0]+"="+id;
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.TablePuzzlesCol[3],((Integer)1).toString());
        openToWrite();
        long value = db.update(DBHelper.TablePuzzles,cv,filter,null);
        close();
        return value;
    }

    public Cursor getHighestOpen(){
        return db.query(DBHelper.TablePuzzles,DBHelper.TablePuzzlesCol,DBHelper.TablePuzzlesCol[3]+"="+1,null,null,null,DBHelper.TablePuzzlesCol[0]+ " desc");
    }
}
