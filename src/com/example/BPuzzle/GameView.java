package com.example.BPuzzle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Konráð
 * Date: 27.10.2013
 * Time: 21:10
 * To change this template use File | Settings | File Templates.
 */
public class GameView extends View {

    private class MyShape{

        Rect baseRect;
        Rect rect;
        int color;
        Orientation type;
        Bitmap bm;

        MyShape(Rect r, int c, Orientation t){
            baseRect = r;
            color = c;
            type = t;

        }
        MyShape(String input){
            type = input.charAt(1) == 'V' ? Orientation.VERTICAL : Orientation.HORIZONTAL;
            // x y length
            int tX = Character.getNumericValue(input.charAt(3));
            int tY = Character.getNumericValue(input.charAt(5));
            int tS = Character.getNumericValue(input.charAt(7));
            int right;
            int bottom;
            if(type == Orientation.VERTICAL){
                right = tX+1;
                bottom = tY+tS;
            }
            else{
                right = tX+tS;
                bottom = tY+1;
            }
            baseRect = new Rect(tX,tY,right,bottom);

            if(!playerDrawn) {
                bm = BitmapFactory.decodeResource(getResources(),R.drawable.hero);
                playerDrawn = true;
            }
            else {
                bm = getCar(input.charAt(1), tS);
            }



        }

    }

    Paint mPaint = new Paint();
    Boolean playerDrawn = false;
    Paint m_Paint = new Paint();
    ArrayList<MyShape> mShapes = new ArrayList<MyShape>();
    MyShape mMovingShape = null;
    int xOffset;
    int yOffset;
    Bitmap background = BitmapFactory.decodeResource(getResources(),R.drawable.game);
    int mSize = 0;
    private char[][] m_board = new char[6][6];
    private int m_cellWidth = 0;
    private int m_cellHeight = 0;
    private OnMoveEventHandler m_moveHandler = null;
    Rect m_rect = new Rect();
    ShapeDrawable m_shape = new ShapeDrawable( new OvalShape() );
    int puzzle;
    int score;
    int highScore;
    int startX;
    int startY;
   // Bitmap bm = BitmapFactory.decodeFile("drawable/game_bckgrn.png");

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        System.out.println("context: " + context.isRestricted());
        m_Paint.setColor(Color.WHITE);
       // m_Paint.
        m_Paint.setStyle( Paint.Style.STROKE );

        xOffset = 0;
        yOffset = 0;

        score = 0;

        String nextSetup = context.getSharedPreferences("myState",Context.MODE_MULTI_PROCESS).getString("setup", null);
        puzzle = context.getSharedPreferences("myState",Context.MODE_MULTI_PROCESS).getInt("puzzle",0);
        highScore = context.getSharedPreferences("myState",Context.MODE_MULTI_PROCESS).getInt("score",0);

        String[] setup = nextSetup.split(", ");

        for(String s : setup){
            mShapes.add(new MyShape(s));
        }

        //mShapes.add(new MyShape(new Rect(80, 160, 240, 240), Color.RED, Orientation.HORIZONTAL));
        /*mShapes.add(new MyShape("(H 1 2 2)"));
        mShapes.add( new MyShape( "(V 0 1 3") );
        mShapes.add(new MyShape("(H 0 0 2)"));
        mShapes.add( new MyShape( "(V 3 1 3)" ));
        mShapes.add(new MyShape("(H 2 5 3)"));
        mShapes.add( new MyShape( "(V 0 4 2)") );
        mShapes.add(new MyShape("(H 4 4 2)"));
        mShapes.add( new MyShape( "(V 5 0 3)" ) );*/

        //(H 1 2 2), (V 0 1 3), (H 0 0 2), (V 3 1 3), (H 2 5 3), (V 0 4 2), (H 4 4 2), (V 5 0 3)
    }



    protected void onDraw( Canvas canvas ) {
       canvas.drawBitmap(background, 0, 0, null);
        /*for ( int r=5; r>=0; --r ) {
            for ( int c=0; c<6; ++c ) {
                m_rect.set( c * m_cellWidth, r * m_cellHeight,
                        c * m_cellWidth + m_cellWidth, r * m_cellHeight + m_cellHeight );

                canvas.drawRect( m_rect, m_Paint );
                m_rect.inset( (int)(m_rect.width() * 0.1), (int)(m_rect.height() * 0.1) );
                m_shape.setBounds(m_rect);

            }
        }*/

        System.out.println("MaxHeight: " + canvas.getHeight());
        System.out.println("MaxWidth: " + canvas.getWidth());

        for ( MyShape shape : mShapes ) {
            mPaint.setColor( shape.color );
            canvas.drawBitmap(shape.bm,shape.rect.left,shape.rect.top,null);
        }
    }

    private void scaleRect(){
        for (MyShape s : mShapes){
            s.rect = new Rect(s.baseRect.left*m_cellWidth,s.baseRect.top*m_cellHeight,s.baseRect.right*m_cellWidth,s.baseRect.bottom*m_cellHeight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(mSize, mSize);
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        m_cellWidth = xNew / 6;
        m_cellHeight = yNew / 6;
        scaleRect();
    }

    public boolean onTouchEvent( MotionEvent event ) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                mMovingShape = findShape( x, y );
                startX = mMovingShape.rect.left/m_cellWidth;
                startY = mMovingShape.rect.top/m_cellHeight;
                System.out.println("x,y: "+startX+","+startY);
                break;
            case MotionEvent.ACTION_UP:
                if ( mMovingShape != null ) {
                    boolean win = false;
                    if(mMovingShape.type == Orientation.VERTICAL){
                        int newTop = Math.round(((float)mMovingShape.rect.top)/((float)m_cellHeight));
                        mMovingShape.rect.offsetTo(mMovingShape.rect.left,newTop*m_cellHeight);
                        score += Math.abs(startY-mMovingShape.rect.top/m_cellHeight);
                    }
                    else{
                        int newLeft = Math.round(((float)mMovingShape.rect.left)/((float)m_cellWidth));
                        mMovingShape.rect.offsetTo(newLeft*m_cellWidth,mMovingShape.rect.top);
                        score += Math.abs(startX-mMovingShape.rect.left/m_cellWidth);
                        if(mMovingShape.rect.right >= mSize && mMovingShape == mShapes.get(0))
                            win = true;
                    }
                    System.out.println("Score: "+score);
                    invalidate();
                    mMovingShape = null;

                    if(win){
                        Toast.makeText(super.getContext(),"YOU WIN!!!",Toast.LENGTH_LONG).show();
                        Activity host = (Activity) this.getContext();
                        Intent intent = new Intent(host,GameActivity.class);
                        Bundle extras = new Bundle();
                        extras.putSerializable("puzzle_id",puzzle+1);
                        if(score < highScore){
                            extras.putSerializable("score",score);
                        }
                        intent.putExtras(extras);
                        host.startActivity(intent);
                    }
                    // emit an custom event ....
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if ( mMovingShape != null ){

                        if(mMovingShape.type == Orientation.VERTICAL){
                            y = Math.min( y,getHeight()-mMovingShape.rect.height()+yOffset);
                            y = Math.max(y,yOffset);
                            if(!collision(mMovingShape,mMovingShape.rect.left,y-yOffset)){
                                mMovingShape.rect.offsetTo(mMovingShape.rect.left,y-yOffset);
                            }
                        }
                        else{

                            x = Math.min( x, getWidth() - mMovingShape.rect.width()+xOffset );
                            x = Math.max(x, xOffset);
                            if(!collision(mMovingShape,x-xOffset,mMovingShape.rect.top)){
                                mMovingShape.rect.offsetTo(x - xOffset, mMovingShape.rect.top);
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

    public void setMoveEventHandler( OnMoveEventHandler handler ) {

        m_moveHandler = handler;
    }

    public int randomColor() {
        int colorInt = (int) (Math.random()*4);

            switch(colorInt) {
                case 0:
                    return Color.BLUE;
                case 1:
                    return Color.RED;
                case 2:
                    return Color.CYAN;
                case 3:
                    return Color.GREEN;

            }
            return Color.BLUE;

    }

    public Bitmap getCar(char orientation, int length) {

        double random = Math.random();

        if(orientation == 'H') {
            if(length == 2) {
                if(random <= 0.25)
                    return  BitmapFactory.decodeResource(getResources(),R.drawable.bluehor2);
                else if (random <= 0.5)
                    return  BitmapFactory.decodeResource(getResources(),R.drawable.cyanhor2);
                else if(random <= 0.75)
                    return  BitmapFactory.decodeResource(getResources(),R.drawable.greenhor2);
                else
                    return  BitmapFactory.decodeResource(getResources(),R.drawable.redhor2);
            }
            else {
                if(random <= 0.33)
                    return  BitmapFactory.decodeResource(getResources(),R.drawable.brownhor3);
                else if (random <= 0.67)
                    return  BitmapFactory.decodeResource(getResources(),R.drawable.purplehor3);
                else
                    return  BitmapFactory.decodeResource(getResources(),R.drawable.turquoisehor3);
            }
        }
        else {
            if(length == 2) {
                if(random <= 0.25)
                    return  BitmapFactory.decodeResource(getResources(),R.drawable.bluevert2);
                else if (random <= 0.5)
                    return  BitmapFactory.decodeResource(getResources(),R.drawable.cyanvert2);
                else if(random <= 0.75)
                    return  BitmapFactory.decodeResource(getResources(),R.drawable.greenvert2);
                else
                    return  BitmapFactory.decodeResource(getResources(),R.drawable.redvert2);
            }
            else {
                if(random <= 0.33)
                    return  BitmapFactory.decodeResource(getResources(),R.drawable.brownvert3);
                else if (random <= 0.67)
                    return  BitmapFactory.decodeResource(getResources(),R.drawable.purplevert3);
                else
                    return  BitmapFactory.decodeResource(getResources(),R.drawable.turquoisevert3);
            }
        }
    }
}
