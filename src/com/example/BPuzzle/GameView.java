package com.example.BPuzzle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

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
    int xOffset;
    int yOffset;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        xOffset = 0;
        yOffset = 0;

        mShapes.add(new MyShape(new Rect(0, 0, 100, 100), Color.RED, Orientation.HORIZONTAL));
        mShapes.add( new MyShape( new Rect( 200, 300, 300, 350), Color.BLUE,Orientation.VERTICAL ) );
    }

    protected void onDraw( Canvas canvas ) {
        for ( MyShape shape : mShapes ) {
            mPaint.setColor( shape.color );
            canvas.drawRect( shape.rect, mPaint );
        }
    }

    public boolean onTouchEvent( MotionEvent event ) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                mMovingShape = findShape( x, y );
                break;
            case MotionEvent.ACTION_UP:
                if ( mMovingShape != null ) {
                    mMovingShape = null;
                    // emit an custom event ....
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if ( mMovingShape != null ){

                        if(mMovingShape.type == Orientation.VERTICAL){
                            x = Math.min( x, getWidth() - mMovingShape.rect.width()+xOffset );
                            x = Math.max(x, xOffset);
                            if(!collision(mMovingShape,x-xOffset,mMovingShape.rect.top)){
                                mMovingShape.rect.offsetTo(x - xOffset, mMovingShape.rect.top);
                            }
                        }
                        else{
                            y = Math.min( y,getHeight()-mMovingShape.rect.height()+yOffset);
                            y = Math.max(y,yOffset);
                            if(!collision(mMovingShape,mMovingShape.rect.left,y-yOffset)){
                                mMovingShape.rect.offsetTo(mMovingShape.rect.left,y-yOffset);
                            }
                       }
                    invalidate();

                }
                break;
        }
        return true;
    }

    private MyShape findShape( int x, int y ) {
        for ( MyShape shape : mShapes ) {
            if ( shape.rect.contains( x, y ) ) {
                xOffset = x - shape.rect.left;
                yOffset = y - shape.rect.top;

                return shape;
            }
        }
        return null;
    }

    private boolean collision(MyShape a,int nextX,int nextY){
        for(MyShape s : mShapes){
            if(a != s){
                Rect nRect = new Rect(a.rect);
                nRect.offsetTo(nextX,nextY);
                if(nRect.intersect(s.rect)){
                    return true;
                }
            }
        }
        return false;
    }
}
