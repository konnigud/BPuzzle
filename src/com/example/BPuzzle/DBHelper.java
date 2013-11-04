package com.example.BPuzzle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: Konráð
 * Date: 2.11.2013
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "BPUZZLES_DB";
    public static final int DB_VERSION = 9;

    public static final String TablePuzzles = "puzzles";
    public static final String[] TablePuzzlesCol = {"_id","level","setup","open","score"};

    public static final String sqlCreateTablePuzzles =
            "CREATE TABLE "+TablePuzzles+" ("+
            TablePuzzlesCol[0]+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            TablePuzzlesCol[1]+ " INTEGER NOT NULL, "+
            TablePuzzlesCol[2]+ " TEXT, "+
            TablePuzzlesCol[3]+ " INTEGER, "+
            TablePuzzlesCol[4]+ " INTEGER"+
            ");";

    public static final  String sqlDropTablePuzzles = "DROP TABLE IF EXISTS "+TablePuzzles+";";

    public DBHelper (Context context){
        super (context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlCreateTablePuzzles);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        System.out.println("Database reset to Version: "+i2);
        sqLiteDatabase.execSQL(sqlDropTablePuzzles);
        onCreate(sqLiteDatabase);
    }
}
