package com.layout;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzheng on 2019/3/23 2:18 PM.
 * E-mail : ivring11@163.com
 **/
public class TagLayout extends ViewGroup {
    List<Rect> childrenBounds = new ArrayList<>();

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 测量自己的高度和子view的高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthUsed  = 0;
        int heightUsed = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

          /*  //拿到开发者xml的要求
            LayoutParams layoutParams = child.getLayoutParams();
            //获取当前viewGroup的模式
            int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
            int specWidthSize = MeasureSpec.getMode(heightMeasureSpec);
            //子view的模式和size、
            int childWidthMode;
            int childWidthSize;
            switch (layoutParams.width) {
                case LayoutParams.MATCH_PARENT:
                    switch (specWidthMode) {
                        //如果当前ViewGroup是具体的高度
                        case MeasureSpec.EXACTLY:
                            //最大值和
                        case MeasureSpec.AT_MOST:
//                            那么子view的模式也就确定了
                            childWidthMode = MeasureSpec.EXACTLY;
                            //当前viewGroup的size减去用掉的width，
                            childWidthSize = specWidthSize - usedWidth;

                            break;
                        case MeasureSpec.UNSPECIFIED:
                            childWidthMode = MeasureSpec.UNSPECIFIED;
                            childWidthSize = 0;
                            break;
                    }
                    break;
                case LayoutParams.WRAP_CONTENT:
                    break;
            }*/

//           ==>

            measureChildWithMargins(child, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);

            //1、测量子view
            child.measure();

            //拿到自view的尺寸，存起来
            Rect rect = childrenBounds.get(i);
            rect.set(widthUsed, heightUsed, widthUsed + child.getMeasuredWidth(), heightUsed + child.getMeasuredHeight());

            widthUsed += child.getMeasuredWidth();

        }
        //保存当前view的尺寸
        int width  = widthUsed;
        int height = 0;
        setMeasuredDimension(width, height);

    }

    /**
     * 给子view去布局
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Rect rect  = childrenBounds.get(i);
            child.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }
}
