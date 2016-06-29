package com.jack.jiujiuwave.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.jack.jiujiuwave.util.DisplayUtils;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public class WaveCircleView extends View {
    //圆圈画笔
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //圆圈颜色
    private int mColor = Color.BLUE;
    //圆圈半径
    private float radius = DisplayUtils.dp2px(getContext(),10);
    //在wavelayout的角度
    private int layoutX;
    //在wavelayout的半径
    private int layoutY;

    public WaveCircleView(Context context) {
        this(context,null);
    }

    public WaveCircleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint.setColor(mColor);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public int getLayoutX() {
        return layoutX;
    }

    public void setLayoutX(int layoutX) {
        this.layoutX = layoutX;
    }

    public int getLayoutY() {
        return layoutY;
    }

    public void setLayoutY(int layoutY) {
        this.layoutY = layoutY;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
    }

    //测量圆圈宽高
    private int measureSize(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = DisplayUtils.dp2px(getContext(),radius * 2);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(radius, radius, radius, mPaint);
    }

}
