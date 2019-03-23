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
            }
            child.measure();
            */

//        这是就是 measureChildWithMargins()内部实现的思路

        //横向当前行已使用的宽度
        int lineWidthUsed  = 0;
        //总高度
        int allheightUsed = 0;
        //所有行最高和最宽的值 , 可以作为tagView 的宽
        int maxWidth  = 0;
        int maxHeight  = 0;
        //获取当前layoutview的mode和宽度
        int tagLayoutSpecWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int tagLayoutSpecWidthSize = MeasureSpec.getSize(widthMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            //1、方法作用：测量子view
            //2、注意使用时需要重写generateLayoutParams方法，返回一个MarginLayoutParams
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, allheightUsed);
            //1、UNSPECIFIED 的话就永远不用重新测绘childview，因为父view的宽度没有限制（处在horizonScrollView），那子view直接一直往后排就好了。
            //2、如果已使用的宽度 + 当前测量的child宽度 > 当前Layout的宽度的话，就错行，再重新量一遍当前正在量的child。
            //再量一次当前的childview是因为，上次测量如果父view宽度就不够，他就会压缩自己，这样就不准确了。
            if (tagLayoutSpecWidthMode != MeasureSpec.UNSPECIFIED && lineWidthUsed + child.getMeasuredWidth() > tagLayoutSpecWidthSize) {
                //当前行 置为头部
                lineWidthUsed = 0;
                allheightUsed += maxHeight;
                maxHeight = 0;
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, allheightUsed);
            }

            //拿到自view的尺寸，存起来
            Rect childBound;
            if (childrenBounds.size() <= i) {
                childBound = new Rect();
                childrenBounds.add(childBound);
            } else {
                childBound = childrenBounds.get(i);
            }
            //存储当前childview的位置
            childBound.set(lineWidthUsed, allheightUsed, lineWidthUsed + child.getMeasuredWidth(), allheightUsed + child.getMeasuredHeight());

            lineWidthUsed += child.getMeasuredWidth();
            //存储最宽的值
            maxWidth = Math.max(lineWidthUsed, maxWidth);
            maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
        }
        //保存当前view的尺寸
        int width  = maxWidth;
        int height = allheightUsed + maxHeight;
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

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
