package com.example.myapplication;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
    static String token;
    volatile static int status=0;
    static CountDownLatch countDownLatch=new CountDownLatch(1);
    public static void main(String[] args){
        new RefreshThread("thread1").start();
        new RefreshThread("thread2").start();
        new RefreshThread("thread3").start();
    }
    static class RefreshThread extends Thread{
        public RefreshThread(String name){
            super(name);
        }

        @Override
        public void run() {
            try {
                super.run();
                if (status == 1) {
                    countDownLatch.await();
                }
                if(status==2) {
                    System.out.println(getName()+":当前token："+token);
                }else{
                    refresh();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        private void refresh(){
            System.out.println(getName()+":开始刷新token");
            try {
                status=1;
                sleep(3000);
                status=2;
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(getName()+":结束刷新token");
            token=getName();
        }
    }
}
/**
 1、未设置修改文案 100%【杨铭嘉】
 2、 分享弹窗体验，分享完成关闭提示 100%【杨铭嘉】
 3、A158正式环境修改 100%【杨铭嘉】
 4、首页数据去重重写 100%【杨铭嘉】
 5、无车页面去绑定文案修改 100%【杨铭嘉】
 6、首页fragment 白屏 bug处理 100%【杨铭嘉】

 */
