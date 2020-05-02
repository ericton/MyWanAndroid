package com.ericton.wanandroid.inject;

import android.app.Activity;
import android.view.View;

import com.ericton.wanandroid.base.BaseActivity;
import com.ericton.wanandroid.utils.LogUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by eric.
 * Date: 2020-05-02
 */
public class AutoClickImp {
    public static void inject(final Activity activity) {
        Class<?> clazz = activity.getClass();
        while (clazz != BaseActivity.class) {
            clazz = clazz.getSuperclass();
        }
        Method[] methods = clazz.getDeclaredMethods();
        for (final Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof AutoClick) {
                    int[] ids = ((AutoClick) annotation).id();
                    for (int id : ids) {
                        View v = activity.findViewById(id);
                        if (v != null) {
//                            View.OnClickListener listener = new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    try {
//                                        method.setAccessible(true);
//                                        method.invoke(activity);
//                                    } catch (Exception e) {
//                                        LogUtil.i(e.toString());
//                                    }
//                                }
//                            };

                            View.OnClickListener listener =
                                    (View.OnClickListener) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                                        @Override
                                        public Object invoke(Object proxy, Method m,
                                                             Object[] args) throws Throwable {
                                            try {
                                                if (m.getName().equals("onClick")) {
                                                    method.setAccessible(true);
                                                    return method.invoke(activity);
                                                }
                                            } catch (Exception e) {
                                                LogUtil.i(e.toString());
                                            }
                                            return null;
                                        }
                                    });
                            v.setOnClickListener(listener);
                        }
                    }
                }
            }
        }
    }
}
