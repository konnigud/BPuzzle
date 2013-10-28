package com.example.BPuzzle;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
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
        MyShape(String input){
            type = input.charAt(1) == 'V' ? Orientation.VERTICAL : Orientation.HORIZONTAL;
            // x y length
            int tX = Character.getNumericValue(input.charAt(3));
            int tY = Character.getNumericValue(input.charAt(5));
            int tS = Character.getNumericValue(input.charAt(7));
            System.out.println("3: " + tX);
            System.out.println("5: " + tY);
            System.out.println("7: " + tS);
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
            rect = new Rect(tX*80,tY*80,right*80,bottom*80);
            //rect = new Rect((int)input.charAt(3)*80,(int)input.charAt(5)*80,(int)input.charAt(5)*80+(int)input.charAt(7)*80,(int)input.charAt(3)*80+80);
            System.out.println("rect: "+rect.toString());
            color = Color.YELLOW;
        }
        Rect rect;
        int color;
        Orientation type;
    }

    Paint mPaint = new Paint();
    Paint m_Paint = new Paint();
    ArrayList<MyShape> mShapes = new ArrayList<MyShape>();
    MyShape mMovingShape = null;
    int xOffset;
    int yOffset;
    private char[][] m_board = new char[6][6];
    private int m_cellWidth = 0;
    private int m_cellHeight = 0;
    private OnMoveEventHandler m_moveHandler = null;
    Rect m_rect = new Rect();
    ShapeDrawable m_shape = new ShapeDrawable( new OvalShape() );
   // Bitmap bm = BitmapFactory.decodeFile("drawable/game_bckgrn.png");

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_Paint.setColor( Color.WHITE );
       // m_Paint.
        m_Paint.setStyle( Paint.Style.STROKE );

        xOffset = 0;
        yOffset = 0;

        //mShapes.add(new MyShape(new Rect(80, 160, 240, 240), Color.RED, Orientation.HORIZONTAL));
        mShapes.add(new MyShape("(H 1 2 2)"));
        mShapes.add( new MyShape( "(V 0 1 3") );
        mShapes.add(new MyShape("(H 0 0 2)"));
        mShapes.add( new MyShape( "(V 3 1 3)" ));
        mShapes.add(new MyShape("(H 2 5 3)"));
        mShapes.add( new MyShape( "(V 0 4 2)") );
        mShapes.add(new MyShape("(H 4 4 2)"));
        mShapes.add( new MyShape( "(V 5 0 3)" ) );

        //(H 1 2 2), (V 0 1 3), (H 0 0 2), (V 3 1 3), (H 2 5 3), (V 0 4 2), (H 4 4 2), (V 5 0 3)
    }


    protected void onDraw( Canvas canvas ) {
       // canvas.drawBitmap(bm, 0, 0, null);
        for ( MyShape shape : mShapes ) {
            mPaint.setColor( shape.color );
            canvas.drawRect( shape.rect, mPaint );
        }

        for ( int r=5; r>=0; --r ) {
            for ( int c=0; c<6; ++c ) {
                m_rect.set( c * m_cellWidth, r * m_cellHeight,
                        c * m_cellWidth + m_cellWidth, r * m_cellHeight + m_cellHeight );

                canvas.drawRect( m_rect, m_Paint );
                m_rect.inset( (int)(m_rect.width() * 0.1), (int)(m_rect.height() * 0.1) );
                m_shape.setBounds(m_rect);

            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        m_cellWidth = xNew / 6;
        m_cellHeight = yNew / 6;
    }

    public boolean onTouchEvent( MotionEvent event ) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                Toast.makeText(super.getContext(),"x: "+(x/80)+" y: "+(y/80),Toast.LENGTH_LONG).show();
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
}
