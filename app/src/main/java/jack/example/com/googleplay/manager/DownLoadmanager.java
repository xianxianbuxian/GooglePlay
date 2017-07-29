package jack.example.com.googleplay.manager;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import jack.example.com.googleplay.Utils.IOUtils;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.domain.AppInfo;
import jack.example.com.googleplay.domain.DownloadInfo;
import jack.example.com.googleplay.http.HttpHelper;

import static java.lang.System.out;

/**
 * 下载管理器
 * 下载未开始 -等待下载-正在下载-下载暂停-下载失败-下载成功
 * DownLoadmanager: 被观察者 有责任通知所有观察者状态和进度发生变化
 * Created by jack on 2017/7/27.
 */

public class DownLoadmanager {
    public static final int STATE_NONE = 0;// 下载未开始
    public static final int STATE_WAITING = 1;// 等待下载
    public static final int STATE_DOWNLOAD = 2;// 正在下载
    public static final int STATE_PAUSE = 3;// 下载暂停
    public static final int STATE_ERROR = 4;// 下载失败
    public static final int STATE_SUCCESS = 5;// 下载成功
    private static DownLoadmanager mDm = new DownLoadmanager();
    //4观察者集合
    private ArrayList<DownloadObserver> mObserver = new ArrayList<>();
    //下载对象的集合
   // private HashMap<String, DownloadInfo> mDownloadinfoMap = new HashMap<>();
    //使用线程安全的ConcurrentHashMap
    private ConcurrentHashMap<String, DownloadInfo> mDownloadinfoMap = new ConcurrentHashMap<>();
    //下载任务的集合
    private ConcurrentHashMap<String, DownLoadTask> mDownLoadTask = new ConcurrentHashMap<>();

    private DownLoadmanager() {
    }

    public static DownLoadmanager getInstance() {
        return mDm;
    }

    //2注册观察者
    public void registerObserver(DownloadObserver observer) {
        if (observer != null && !mObserver.contains(observer)) {
            mObserver.add(observer);
        }
    }

    //3注销观察者
    public void unregisterObserver(DownloadObserver observer) {
        if (observer != null && mObserver.contains(observer)) {
            mObserver.remove(observer);
        }

    }

    //5.通知下载状态发生变化
    public void notifyDownloadSatateChanged(DownloadInfo info) {
        for (DownloadObserver observer : mObserver) {
            observer.onDownloadStateChang(info);
        }
    }

    //5.通知下载进度条发生变化
    public void notifyDownloadProgessChanged(DownloadInfo info) {
        for (DownloadObserver observer : mObserver) {
            observer.onDownloadProgessChang(info);
        }
    }

    //1.声明一个观察者
    public interface DownloadObserver {

        //下载状态变化
        void onDownloadStateChang(DownloadInfo info);

        //下载进度变化
         void onDownloadProgessChang(DownloadInfo info);
    }

    //开始下载
    public synchronized void download(AppInfo info) {
        //如果对象是第一次下载，需要创建一个新的DownloadInfo对象，从头下载
        //如果之前下载过，要接着下载，实现断点续传
        DownloadInfo downloadInfo = mDownloadinfoMap.get(info.id);
        if (downloadInfo == null) {
            //生成一个下载对象
            downloadInfo = DownloadInfo.copy(info);
        }
        downloadInfo.currentState = STATE_WAITING;//状态切换为等待下载
        notifyDownloadSatateChanged(downloadInfo);//通知所有观察者 状态发生变化了
        Log.e("xx", downloadInfo.name + "等待下载了");
        //将下载对象放入集合中
        mDownloadinfoMap.put(downloadInfo.id, downloadInfo);
        //初始化下载任务，并放入线程池中运行
        DownLoadTask task = new DownLoadTask(downloadInfo);
        ThreadManager.getthreadpool().execute(new DownLoadTask(downloadInfo));
        //将下载任务放入集合中
        mDownLoadTask.put(downloadInfo.id, task);

    }

    //下载任务的对象
    class DownLoadTask implements Runnable {
        DownloadInfo info;

        public DownLoadTask(DownloadInfo info) {
            this.info = info;
        }

        @Override
        public void run() {
            //状态改为正在下载
            info.currentState = STATE_DOWNLOAD;
            notifyDownloadSatateChanged(info);
            Log.e("xx", info.name + "开始下载了");
            File file = new File(info.path);
            HttpHelper.HttpResult httpResult;
            if (!file.exists() || file.length() != info.currentPos || info.currentPos == 0) {
                //从头下载
                //删除无效文件
                file.delete();//文件如果不存在也可以删  只不过没效果
                info.currentPos = 0;//当前位置至为0
                httpResult = HttpHelper.download(HttpHelper.URL + "download?name=" + info.downloadUrl);
            } else {
                //断点续传
                //range 表示请求服务器从文件的那个位置开始返回数据
                httpResult = HttpHelper.download(HttpHelper.URL + "download?name=" + info.downloadUrl + "&range" + file.length());
            }
            InputStream in=null;
            FileOutputStream fos=null;
            if (httpResult!=null&&( in=httpResult.getInputStream())!=null){
                try {
                  fos=new FileOutputStream(file,true);//在原来文件上追加数据
                    int len=0;
                    byte[] buffer=new byte[1024*10];
                    //只有状态是正在下载才轮询 解决下载过程中 中途暂停的问题
                   while ((len=in.read(buffer))!=-1&&info.currentState==STATE_DOWNLOAD){
                       fos.write(buffer,0,len);
                       fos.flush();//把剩余数据刷入本地
                       //更新下载进度
                       info.currentPos+=len;
                       notifyDownloadProgessChanged(info);
                   }

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    IOUtils.close(in);
                    IOUtils.close(out);
                }

                //文件下载结束
                if (file.length()==info.size){
                    //文件完整 下载成功
                    info.currentState=STATE_SUCCESS;
                    notifyDownloadSatateChanged(info);
                }else if (info.currentState==STATE_PAUSE){
                    //中途暂停
                    notifyDownloadSatateChanged(info);


                }else {
                    //下载失败
                    file.delete();//删除无效文件
                    info.currentState=STATE_ERROR;
                    info.currentPos=0;
                    notifyDownloadSatateChanged(info);

                }
            }else {
                //网络异常
                file.delete();//删除无效文件
                info.currentState=STATE_ERROR;
                info.currentPos=0;
                notifyDownloadSatateChanged(info);

            }
            //从集合中移除下载任务
            mDownLoadTask.remove(info.id);
        }
    }

    //下载暂停
    public synchronized void pause(AppInfo info) {
        //取出下载对象
        DownloadInfo downloadInfo = mDownloadinfoMap.get(info.id);
        if (downloadInfo != null) {
            //只有在正在下载和等待下载是才暂停
            if (downloadInfo.currentState == STATE_DOWNLOAD ||
                    downloadInfo.currentState == STATE_WAITING) {
                //将下载状态切换成暂停
                downloadInfo.currentState = STATE_PAUSE;
                notifyDownloadSatateChanged(downloadInfo);
                DownLoadTask task = mDownLoadTask.get(downloadInfo.id);
                if (task != null) {
                    //移除下载任务 如果任务还没开始正在等待可以通过此方法移除
                    //如果任务已经开始 需要在run方法里面进行中断
                    ThreadManager.getthreadpool().cancel(task);
                }

            }
        }
    }

    //开始安装
    public synchronized void install(AppInfo info) {

        DownloadInfo downloadInfo = mDownloadinfoMap.get(info.id);
        if (downloadInfo != null) {
            // 跳到系统的安装页面进行安装
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + downloadInfo.path),
                    "application/vnd.android.package-archive");
            Uiutils.getcontext().startActivity(intent);
        }
    }
   //根据应用信息返回一个下载对象
    public DownloadInfo getDownloadinfo(AppInfo info){
       return mDownloadinfoMap.get(info.id);
    }
}
