package jack.example.com.googleplay.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

/**
 * 自定义application，进行全局初始化
 * Created by jack on 2017/7/9.
 */

public class GooglePlayApplication extends Application {

    private  static Context  context;
    private static Handler handler;
    private static int mainThreadID;

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public  static  int getMainThreadID() {
        return mainThreadID;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        handler = new Handler();
        //线程ID  PID 进程ID  这里是主线程id
        mainThreadID = Process.myTid();

    }
}
