package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 语音通话的声波控件
 * Created by Mr.LongFace on 2017/9/16.
 */
public class SoundWavesView extends View {
    private int mMini; // 最短值
    private int mMax; // 最大值
    private int mLineWidth; // 每条声波的宽度
    private int mSoundNum = 5; // 声波的数量
    private int mSpac; // 每条声波的中点
    private int mWidth, mHeight; // 控件宽高
    private boolean isRun = false;
    private Paint mPaint;
    private RectF mRectF;
    private List<SoundLine> mSoundList = new ArrayList<>();
    private Handler mHandler = new Handler();
    private Runnable mInvalidateRun = new Runnable() {
        @Override
        public void run() {
            postInvalidate();
        }
    };

    public SoundWavesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#000000"));
        mPaint.setStyle(Paint.Style.FILL);
        mRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initParam();
    }

    private void initParam() {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mMini = (int) (mHeight * 0.3f);
        mMax = mHeight;
        initLines();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mSoundNum; i++) {
            SoundLine sound = mSoundList.get(i);
            mRectF.left = sound.left;
            mRectF.right = sound.right;
            mRectF.top = sound.top;
            mRectF.bottom = sound.bottom;
            canvas.drawRoundRect(mRectF, mLineWidth / 2, mLineWidth / 2, mPaint);
        }
        if (isRun) {
            mHandler.postDelayed(mInvalidateRun, 10);
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (isRun) {
            if (visibility == VISIBLE) {
                if (mWidth == 0) {
                    initParam();
                }
                if (mSoundList != null && mSoundList.size() >0){
                    for (SoundLine soundLine : mSoundList) {
                        soundLine.start();
                    }
                }
            } else {
                if (mSoundList != null && mSoundList.size() >0){
                    for (SoundLine soundLine : mSoundList) {
                        soundLine.stop();
                    }
                }
            }
        }
    }

    public void start() {
        if (!isRun) {
            isRun = true;
            for (SoundLine sound : mSoundList) {
                sound.start();
            }
            postInvalidate();
        }
    }

    public void stop() {
        if (isRun) {
            isRun = false;
            for (SoundLine sound : mSoundList) {
                sound.stop();
            }
        }
    }

    private void initLines() {
        mLineWidth = (int) (mWidth / mSoundNum * 0.7f);
        mSpac = mWidth / (mSoundNum - 1);
        mSoundList.clear();
        chaos();
    }

    /**
     * 生成凌乱的
     */
    private void chaos() {
        for (int i = 0; i < mSoundNum; i++) {
            int left = i * mSpac - mLineWidth / 2;
            int right = i * mSpac + mLineWidth / 2;
            SoundLine s = new SoundLine(left, right, 0, mHeight);
            s.setMode(SoundLine.SPEED_RAN);
            s.setBorder(mMini, mMax);
            mSoundList.add(s);
        }
    }

    /**
     * 生成波浪的
     */
    private void wave() {
// TODO 防止UI抽风
    }

    /**
     * 生成有序的
     */
    private void order() {
// TODO 防止UI抽风
    }
}