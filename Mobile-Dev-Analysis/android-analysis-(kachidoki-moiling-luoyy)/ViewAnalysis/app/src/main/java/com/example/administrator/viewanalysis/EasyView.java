package com.example.administrator.viewanalysis;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2015/3/22 0022.
 */
public class EasyView extends View {

    private int luowuxia;
    private boolean hongyan;
    EasyView Insteat =this;

    private OnRightButtonListener RButton=null;
    private OnLeftButtonListener LButton=null;


    public EasyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.EasyView);
        luowuxia = mTypedArray.getInt(R.styleable.EasyView_luowuxia, 100);
        hongyan = mTypedArray.getBoolean(R.styleable.EasyView_hongyan, false);

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               if (event.getX()<100){
                   RButton.onclikRightButton(Insteat);
               }
                else{

                   LButton.onclikLeftButton(Insteat);
               }
                return false;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qq);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.pp), mBitmap.getWidth(), 0, null);


    }

    public void setOnRightButtonListener(OnRightButtonListener RButton) {
        this.RButton = RButton;
    }

    public void setOnLeftButtonListener(OnLeftButtonListener LButton) {
        this.LButton = LButton;
    }

    public interface OnRightButtonListener {
        public void onclikRightButton(View view);
    }

    public interface OnLeftButtonListener {
        public void onclikLeftButton(View view);
    }


}

