package com.example.myapplication;

import android.animation.ValueAnimator;

import java.util.Random;

/**
* 语音音频波纹的单个音波属性
* Created by Mr.LongFace on 2017/9/16.
*/
public class SoundLine implements ValueAnimator.AnimatorUpdateListener{
// 低 中 高 随机 4挡
public static final int SPEED_LOW = 500;
public static final int SPEED_MID = 200;
public static final int SPEED_HEI = 0;
public static final int SPEED_RAN = 0;
private Random mRandom;
private ValueAnimator mAnim;
public int left , right , top , bottom;
private int min , max;
public SoundLine(int left , int right , int top , int bottom){
this.left = left;
this.right = right;
this.top = top;
this.bottom = bottom;
mRandom = new Random();
initAnim();
}
private void initAnim() {
mAnim = ValueAnimator.ofFloat(0.0f , 1.0f);
setMode(SPEED_MID);
mAnim.setRepeatCount(-1);
mAnim.setRepeatMode(ValueAnimator.REVERSE);
mAnim.addUpdateListener(this);
}
public void setMode(int mode){
if (mode == SPEED_RAN) {
mode = mRandom.nextInt(400);
}
mAnim.setDuration(300 + mode);
}
public void start(){
if (mAnim.isRunning()){
mAnim.end();
}
mAnim.start();
}
@Override
public void onAnimationUpdate(ValueAnimator valueAnimator) {
float f = (float) valueAnimator.getAnimatedValue();
top = (int) (f * (max - min) / 2);
bottom = max - top;
}
public void setBorder(int min, int max) {
this.min = min;
this.max = max;
}
public void stop() {
mAnim.end();
mAnim.cancel();
}
}