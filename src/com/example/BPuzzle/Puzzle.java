package com.example.BPuzzle;

/**
 * Created with IntelliJ IDEA.
 * User: Konráð
 * Date: 1.11.2013
 * Time: 01:10
 * To change this template use File | Settings | File Templates.
 */
public class Puzzle {

    int mId;
    int mLevel;
    String mSetup;

    public Puzzle(String id, String lvl,String setup){
        mId = Integer.parseInt(id);
        mLevel = Integer.parseInt(lvl);
        mSetup = setup;
    }
}
