package com.example.BPuzzle;

import android.*;
import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Konráð
 * Date: 27.10.2013
 * Time: 21:10
 * To change this template use File | Settings | File Templates.
 */
public class GameView extends View {

    private class MyShape{

        MyShape(Rect r, int c, Orientation t){
            rect = r;
            color = c;
            type = t;
        }
        Rect rect;
        int color;
        Orientation type;
    }

    Paint mPaint = new Paint();
    ArrayList<MyShape> mShapes = new ArrayList<MyShape>();
    MyShape mMovingShape = null;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mShapes.add(new MyShape(new Rect(0, 0, 100, 100), Color.RED, Orientation.HORIZONTAL));
        mShapes.add( new MyShape( new Rect( 200, 300, 300, 350), Color.BLUE,Orientation.VERTICAL ) );
    }

    protected void onDraw( Canvas canvas ) {
        for ( MyShape shape : mShapes ) {
            mPaint.setColor( shape.color );
            canvas.drawRect( shape.rect, mPaint );
        }
    }
}
