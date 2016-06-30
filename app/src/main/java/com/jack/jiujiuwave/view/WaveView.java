package com.jack.jiujiuwave.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import com.jack.jiujiuwave.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/6/28 0028.
 */
public class WaveView extends View {
    // 初始波纹半径
    private float mMiniRadius = 30f;
    //最大波纹半径
    private float mMaxRadius;
    //波纹持续时间
    private long mWaveDuration = 5000;
    //波纹创建时间间隔
    private long mSpeed = 1000;
    //波纹画笔
    private Paint mWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //中间图标画笔
    private Paint mCenterBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //中间图标区域
    private Rect mCenterBitmapArea = new Rect();
    //波纹颜色
    private int mWaveColor = 0x2B5F86;
    //波纹动画效果
    private Interpolator mInterpolator = new AccelerateInterpolator();
    //所有的水波纹
    private List<ValueAnimator> mAnimatorList = new ArrayList<ValueAnimator>();
    //是否开启水波纹
    private boolean mIsRuning = false;
    //是否点击了中间图标
    private boolean mIsCenterClick = false;
    //中间的图标
    private Bitmap mCenterBitmap;
    //中间的圆形图标
    private Bitmap mCenterCircleBitmap;

    public WaveView(Context context) {
        this(context,null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    private Runnable mWaveRunable = new Runnable() {
        @Override
        public void run() {
            if (mIsRuning) {
                newWaveAnimator();
                invalidate();
                postDelayed(mWaveRunable, mSpeed);
            }
        }
    };

    //开启水波纹
    public void start(){
        if (!mIsRuning){
            mIsRuning = true;
            mWaveRunable.run();
        }
    }

    //是否开启水波纹
    public boolean isStart(){
        return mIsRuning;
    }

    //关闭水波纹
    public void stop(){
        mIsRuning = false;
    }

    //设置水波纹颜色
    public void setColor(int color){
        mWaveColor = color;
    }

    //设置水波纹效果
    public void setInterpolator(Interpolator interpolator){
        mInterpolator = interpolator;
    }

    //设计水波纹持续时间
    public void setDuration(long duration){
        mWaveDuration = duration;
    }

    //设置水波纹间隔时间
    public void setSpeed(long speed){
        mSpeed = speed;
    }

    //初始波纹半径
    public float getMiniRadius(){
        return mMiniRadius;
    }

    private ValueAnimator newWaveAnimator() {
        final ValueAnimator mWaveAnimator = new ValueAnimator();
        mWaveAnimator.setFloatValues(mMiniRadius, mMaxRadius);
        mWaveAnimator.setDuration(mWaveDuration);
        mWaveAnimator.setRepeatCount(0);
        mWaveAnimator.setInterpolator(mInterpolator);
        mWaveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                (Float) animation.getAnimatedValue();
            }
        });
        mAnimatorList.add(mWaveAnimator);
        mWaveAnimator.start();
        return mWaveAnimator;
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mWavePaint.setStrokeWidth(1f);
        mWavePaint.setColor(mWaveColor);
        mWavePaint.setDither(true);
        mWavePaint.setStyle(Paint.Style.FILL);

        mCenterBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.center);
        mMiniRadius = Math.min(mCenterBitmap.getWidth(),mCenterBitmap.getHeight()) / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                //当按钮只有在图片即按钮区域内则认定为点击，其他不作点击
                mIsCenterClick = false;
                if (mCenterBitmapArea.contains((int)event.getX(),(int)event.getY())){
                    mIsCenterClick = true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (mIsCenterClick && !mIsRuning){
                    //当点击了按钮，启动水波纹
                   start();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mMaxRadius = Math.min(w, h) / 2;
        //计算中间图标区域
        mCenterBitmapArea.set((w - mCenterBitmap.getWidth()) / 2,(h - mCenterBitmap.getHeight()) / 2
                ,(w + mCenterBitmap.getWidth()) / 2,(h + mCenterBitmap.getHeight()) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas){
        Iterator<ValueAnimator> iterator = mAnimatorList.iterator();
        while (iterator.hasNext()){
            ValueAnimator valueAnimator = iterator.next();
//            Log.e("AnimatedValue",(float)valueAnimator.getAnimatedValue() + "mMaxRadius:" + mMaxRadius);
            if (!valueAnimator.getAnimatedValue().equals(mMaxRadius)){
                //设置透明度
                mWavePaint.setAlpha(getAlpha((Float) valueAnimator.getAnimatedValue()));
                //画水波纹
                canvas.drawCircle(getMeasuredWidth() / 2,getMeasuredHeight() / 2, (Float) valueAnimator.getAnimatedValue(),mWavePaint);
            }else{
                valueAnimator.cancel();
                iterator.remove();
            }
        }

        //绘制中间图标
        drawCenterBitmap(canvas);
        if (mAnimatorList.size() > 0){
            postInvalidateDelayed(10);
        }
    }

    //绘制中间图标
    private void drawCenterBitmap(Canvas canvas){
        if (mCenterCircleBitmap == null){
            mCenterCircleBitmap = createCircleImage(mCenterBitmap,mCenterBitmap.getWidth());
        }
        canvas.drawBitmap(mCenterCircleBitmap,null,mCenterBitmapArea,mCenterBitmapPaint);
    }

    //根据原图和边长绘制圆形图片
    private Bitmap createCircleImage(Bitmap source, int min)
    {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        //产生一个同样大小的画布
        Canvas canvas = new Canvas(target);
       //首先绘制圆形
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        //使用SRC_IN
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //绘制图片
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    //获取水波纹透明度
    private int getAlpha(float mRadius){
        int alpha = 1;
        if (mMaxRadius > 0){
            alpha = (int)((1 - (mRadius - mMiniRadius)/(mMaxRadius - mMiniRadius)) * 255);
        }
//        Log.e("alpha",alpha + "");
        return alpha;
    }

}
