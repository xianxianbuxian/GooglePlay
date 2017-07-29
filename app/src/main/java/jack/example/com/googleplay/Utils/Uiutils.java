package jack.example.com.googleplay.Utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;
import android.view.View;

import jack.example.com.googleplay.global.GooglePlayApplication;

/**
 * Created by jack on 2017/7/9.
 */

public class Uiutils {
    public static Context getcontext() {
        return GooglePlayApplication.getContext();
    }

    public static Handler gethandler() {
        return GooglePlayApplication.getHandler();
    }

    public static int getMainThreadid() {
        return GooglePlayApplication.getMainThreadID();
    }
    ///////////////////加载资源文件/////////////////////////

    //获取字符串
    public static String getString(int id) {
        return getcontext().getResources().getString(id);
    }

    //获取字符串数组
    public static String[] getStingArray(int id) {
        return getcontext().getResources().getStringArray(id);
    }

    //获取图片
    public static Drawable getdrawable(int id) {
        return getcontext().getResources().getDrawable(id);
    }

    //获取颜色
    public static int getColor(int id) {
        return getcontext().getResources().getColor(id);
    }
 //根据id获取颜色的状态选择器
    public static ColorStateList getColorStateList(int mTabTextColorResId) {
        return getcontext().getResources().getColorStateList( mTabTextColorResId);
    }
    //获取尺寸大小
    public static int getDimen(int id) {
        return getcontext().getResources().getDimensionPixelSize(id);//返回具体像素
    }

    ///////////////////dp和px转换//////////////////////
    public static int dip2px(float dip) {
        float density = getcontext().getResources().getDisplayMetrics().density;//设备密度
       return (int) (dip * density + 0.5);
    }
    public static float px2dip(int px) {
        float density = getcontext().getResources().getDisplayMetrics().density;//设备密度
        return px/ density;
    }

    ///////////////////加载布局文件//////////////////////
    public static View inflate(int id){
        return View.inflate(getcontext(),id,null);
    }
    ///////////////////判断是否运行在主线程//////////////////////
    public static boolean isRunonUiThread(){
        //获取当前线程id， 如果当前线程ID和主线程ID相同，那么就是主线程
        int myTid = Process.myTid();//当前线程ID
        if(myTid==getMainThreadid()){
            return true;
        }
            return false;
    }
    //运行在主线程的方法
    public static void runOnUiThread(Runnable r){
        if(isRunonUiThread()){
            //已经是主线程，直接运行
            r.run();
        }else {
            //如果是子线程，借助handler让其运行在主线程
            gethandler().post(r);

        }
    }
//

}
