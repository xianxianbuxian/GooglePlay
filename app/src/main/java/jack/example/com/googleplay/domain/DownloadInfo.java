package jack.example.com.googleplay.domain;

import android.os.Environment;

import java.io.File;

import jack.example.com.googleplay.manager.DownLoadmanager;

/**
 * 下载对象
 * 要加sd卡的权限
 * Created by jack on 2017/7/28.
 */

public class DownloadInfo {
    public String id;
    public String name;
    public String downloadUrl;
    public String packageName;
    public long size;

    public long currentPos;//当前下载位置
    public int currentState;//当前下载状态
    public  String path;//下载到本地文件路径
    public static final String GOOGLE_MARKET = "GOOGLE_MARKET";//sdcard的根目录 文件夹 目录
    public static final String DOWNLOAD = "dowmload";//子文件夹，存放下载文件

    //获取下载进度（0-1）
    public float getProgess() {
         if (size==0){
             return 0;
         }
        return   (float)currentPos /  size;
    }

    //获取文件的下载路径
    public String getFilPath() {
        StringBuffer sb = new StringBuffer();
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        sb.append(sdcard);
        sb.append(File.separator);//斜杠
        sb.append(GOOGLE_MARKET);
        sb.append(File.separator);
        sb.append(DOWNLOAD);
     if (creatDir(sb.toString())){
         //文件夹存在或者创建文件夹
         return sb.toString()+File.separator+name+".apk";//返回文件具体路径
     }
        return null;
    }

    private boolean creatDir(String dir) {
        File dirfile = new File(dir);
        //文件夹不存在或者不是一个文件夹
        if (!dirfile.exists() || !dirfile.isDirectory()) {
            return dirfile.mkdirs();
        }
        return true;//文件夹存在
    }
    //拷贝对象，从appinfo里拷贝出Downloadinfo
    public  static DownloadInfo copy(AppInfo info){
        DownloadInfo downloadInfo=new DownloadInfo();
        downloadInfo.id=info.id;
        downloadInfo.size=info.size;
        downloadInfo.downloadUrl=info.downloadUrl;
        downloadInfo.name=info.name;
        downloadInfo.packageName=info.packageName;
        downloadInfo.currentPos=0;
        downloadInfo.currentState= DownLoadmanager.STATE_NONE;//默认状态是未下载
        downloadInfo.path=downloadInfo.getFilPath();
         return downloadInfo;
    }
}
