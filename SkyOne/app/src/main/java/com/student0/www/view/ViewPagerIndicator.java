package com.student0.www.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by willj on 2017/2/23.
 */

public class ViewPagerIndicator extends LinearLayout {
    private static final int COUNT_DEFAULT_TAB = 4;
    private static final int COLOR_TEXT_NORMAL = 0x77FFFFFF;
    private static final int COLOR_TEXT_HIGHLIGHT = 0XFFFFFFFF;

    //画笔获取
    private Paint mPaint;
    private Path mPath;

    private int mTriangleWidth;
    private int mTriangleHeight;
    private static final float RADIO_TRIANGLE_WIDTH = 1/6F;

    //移动的X成员变量
    private int mInitTranslationX;
    private int mTranslationX;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        //start -初始化画笔
        mPaint = new Paint();
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        //设置尖锐部分的圆滑点
        mPaint.setPathEffect(new CornerPathEffect(3));//end -画笔初始化

        //然后在dispatchDraw里绘制

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();//防止对画布造成影响

        //平移,
        canvas.translate(mInitTranslationX + mTranslationX, 0);
        //System.out.println("---------------------"+ getHeight()+"----------------"+ getTop());
        //绘制
        canvas.drawPath(mPath, mPaint);
        canvas.restore();//防止对画布造成影响
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mTriangleWidth = (int) (w/3 * RADIO_TRIANGLE_WIDTH);
        mInitTranslationX = w/3/2 - mTriangleWidth/2;

        initTriangle();
    }
//初始化三角形
    private void initTriangle() {
        mTriangleHeight = mTriangleWidth/3;

        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth,0);
        mPath.lineTo(mTriangleWidth/2, mTriangleHeight);
        mPath.close();
    }
    //指示器跟随手指进行滚动,改变子控件颜色

    public void scroll(int position, float positionOffset) {
        int tabWidth = getWidth()/3;
        mTranslationX = (int) (position  * tabWidth + positionOffset * tabWidth);
        View view = getChildAt(position);
        if (view instanceof TextView){
            resetTextViewColor();
            ((TextView)view).setTextColor(COLOR_TEXT_HIGHLIGHT);
        }
        //repainting
        invalidate();
    }
    //重置子控件颜色
  private void resetTextViewColor(){
        for(int i = 0; i < getChildCount(); i ++){
            View view = getChildAt(i);
            if (view instanceof TextView){
                ((TextView)view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }

}
