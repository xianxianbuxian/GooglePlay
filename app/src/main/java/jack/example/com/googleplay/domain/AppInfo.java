package jack.example.com.googleplay.domain;

import java.util.ArrayList;

/**
 * 首页应用信息封装
 * Created by jack on 2017/7/20.
 */

public class AppInfo {
    public String des;
    public String id;
    public String name;
    public String packageName;
    public String iconUrl;
    public float stars;
    public long size;
    public String downloadUrl;
    //补充字段 供应用详情页使用
    public String author;
    public String date;
    public String downloadNum;
    public String version;
    public ArrayList<Safeinfo> safe;
    public ArrayList<String> screen;

    //一个内部类是  public static 和外部类没有区别
    public static class Safeinfo {
        public String safeDes;
        public String safeDesUrl;
        public String safeUrl;

    }
}
