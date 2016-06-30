package com.jack.jiujiuwave.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jack.jiujiuwave.R;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public class WaveLayout extends ViewGroup {
    //宽度
    private int mWidth;
    //高度
    private int mHeight;
    //波纹view
    private WaveView waveView;

    public WaveLayout(Context context) {
        this(context,null);
    }

    public WaveLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mWidth = mHeight = Math.min(mWidth, mHeight);
        //测量每个children
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        waveView = (WaveView) findViewById(R.id.wave_view);
    }

    private int measureSize(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 500;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        float miniCircleRadius = 0;
        //设置水波纹位置
        waveView.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        miniCircleRadius = waveView.getMiniRadius();

        int childCount = getChildCount();
        //设置circleview
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            //如果不是Circleview跳过
            if (!(child instanceof WaveCircleView)) {
                continue;
            }

            WaveCircleView waveCircleView = (WaveCircleView) child;
            if (waveCircleView.getLayoutX() == 0 && waveCircleView.getLayoutY() == 0) {
                //随机获取角度
                double angle = Math.random() * 360;
                //随机获取半径，最小半径不能小于WaveView的最小波纹半径
                double radians = miniCircleRadius + waveCircleView.getMeasuredWidth() / 2 + Math.random() * (mWidth / 2 - miniCircleRadius - waveCircleView.getMeasuredWidth() / 2);
                //获取WaveCircleView的x坐标
                double x = getMeasuredWidth() / 2 + Math.sin(Math.toRadians(angle)) * radians;
                //获取WaveCircleView的y坐标
                double y = getMeasuredHeight() / 2 + Math.cos(Math.toRadians(angle)) * radians;
                waveCircleView.setLayoutX((int) x);
                waveCircleView.setLayoutY((int) y);

            }
            waveCircleView.layout(waveCircleView.getLayoutX(),waveCircleView.getLayoutY(),waveCircleView.getLayoutX() + waveCircleView.getMeasuredWidth(),waveCircleView.getLayoutY() + waveCircleView.getMeasuredHeight());
        }
    }

    //增加CircleVie
    public void addCircleView(WaveCircleView waveCircleView){
        if (waveView == null || !waveView.isStart()){
            return;
        }
        addView(waveCircleView);
    }
}
