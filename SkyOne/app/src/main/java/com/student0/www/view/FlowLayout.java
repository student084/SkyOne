package com.student0.www.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willj on 2017/2/18.
 */

public class FlowLayout extends ViewGroup {
    //直接在new 上下文时，调用一个参数的方法
    public FlowLayout(Context context) {
        this(context, null);
    }
    //定义属性时，定义两个参数的方法
    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    //书写控件并且有自定义属性时调用3个参数的方法
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //父级传来的测量值
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取传过来的值，因为在main.xml里使用了这个布局，
        // 所以这个值在main.xml里，即:fill_parent,模式为：EXACTLY
        //如果为wrap_content，那么这个值就要自己设置计算
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //warp_content
        int width = 0;
        int height = 0;

        //记录每一行的高度，宽度
        int lineWidth = 0;
        int lineHeight = 0;

        //得到内部元素的个数
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i ++){
            View child = getChildAt(i);
            //测量子view的宽高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到LayoutParams
            //子的View.getLayoutParams()得到的是父的LinearLayout.LayoutParams
            MarginLayoutParams layoutParams = (MarginLayoutParams)child.getLayoutParams();
            //子View占据的宽度
            int childWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            //子View占据的高度
            int childHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            //大于容器宽度时换行
            if(lineWidth + childWidth >sizeWidth - getPaddingLeft() - getPaddingRight()){
                //对比得到最大的宽度
                width = Math.max(width, lineWidth);
                //遇到最宽子行重置子行宽度
                lineWidth = childWidth;
                //记录行高，高度叠加
                height += lineHeight;
                lineHeight = childHeight;
            }else{
                //未换行
                //叠加行宽
                lineWidth += childWidth;
                //得到当前最大的行高度
                lineHeight = Math.max(lineHeight, childHeight);
            }
            //最后一个控件
            if(i == cCount - 1){
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }

        Log.e("TAG","sizeWidth=" + sizeWidth);
        Log.e("TAG","sizeHeight" + sizeHeight);
        //wrap_content
        setMeasuredDimension(
                //
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth:width + getPaddingRight() + getPaddingLeft(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight:height + getPaddingTop() + getPaddingBottom()//
        );

    }

    /*
    * 存放所有的View
    * 行<列<View>>
    * */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();

    /*
    * 记录每一行的高度
    * */

    private List<Integer> mLineHeight = new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //执行布局
        mAllViews.clear();
        mLineHeight.clear();

        //当前ViewCroup的宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;
        //行view
        List<View> lineViews = new ArrayList<View>();

        int cCount = getChildCount();

        for (int i = 0; i <cCount; i ++){
            View child = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams)child.getLayoutParams();

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            //如果需要换行
            if (childWidth + lineWidth + layoutParams.leftMargin + layoutParams.rightMargin > width - getPaddingLeft() - getPaddingRight()){
                //记录LineHeight
                mLineHeight.add(lineHeight);
                //记录当前行的Views
                mAllViews.add(lineViews);

                //重置行高和行宽
                lineWidth = 0;
                lineHeight = childHeight + layoutParams.topMargin + layoutParams.bottomMargin;
                //重置行view
                lineViews = new ArrayList<View>();
            }
            lineWidth += childWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight) + layoutParams.topMargin + layoutParams.bottomMargin;
            lineViews.add(child);
        }//for end

        //处理最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        //设置子View的位置

        int left = getPaddingLeft();
        int top = getPaddingTop();

        //行数
        int lineNum = mAllViews.size();

        for (int i = 0; i < lineNum; i ++){
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);
            for (int j = 0; j < lineViews.size(); j ++){
                View child = lineViews.get(j);
                //判断child的状态
                if (child.getVisibility() == View.GONE){
                    continue;
                }
                MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();

                int lc = left + layoutParams.leftMargin;
                int tc = top + layoutParams.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                //为子View进行布局
                child.layout(lc, tc, rc, bc);
                //同行增加带动其他增加
                left += child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            }// end for

            //换列行重置
            left = getPaddingLeft();
            //下一列增加带动其他改变
            top += lineHeight;
        }

    }
/*
* 与当前ViewGroup对应的LayoutParams
* */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
