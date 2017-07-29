package jack.example.com.googleplay.http.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jack.example.com.googleplay.domain.AppInfo;

/**
 * 首页详情页网络访问
 * Created by jack on 2017/7/24.
 */

public class HomeDetailProtocol extends Baseprotocol<AppInfo> {
    public String packageName;

    public HomeDetailProtocol(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String getKey() {
        return "detail";
    }

    @Override
    public String getParams() {
        return "&packageName=" + packageName;
    }

    @Override
    public AppInfo parseDate(String result) {
        try {
            JSONObject jo = new JSONObject(result);
            AppInfo appInfo = new AppInfo();
            appInfo.des = jo.getString("des");
            appInfo.downloadUrl = jo.getString("downloadUrl");
            appInfo.stars = (float) jo.getDouble("stars");
            appInfo.iconUrl = jo.getString("iconUrl");
            appInfo.packageName = jo.getString("packageName");
            appInfo.id = jo.getString("id");
            appInfo.size = jo.getLong("size");
            appInfo.name = jo.getString("name");
            appInfo.author = jo.getString("author");
            appInfo.date = jo.getString("date");
            appInfo.downloadNum = jo.getString("downloadNum");
            appInfo.version = jo.getString("version");
            JSONArray safe = jo.getJSONArray("safe");
            //解析安全信息
            ArrayList<AppInfo.Safeinfo> safelist = new ArrayList<>();
            for (int i = 0; i < safe.length(); i++) {
                JSONObject jo1 = safe.getJSONObject(i);
                AppInfo.Safeinfo safeinfo = new AppInfo.Safeinfo();
                safeinfo.safeDes = jo1.getString("safeDes");
                safeinfo.safeDesUrl = jo1.getString("safeDesUrl");
                safeinfo.safeUrl=jo1.getString("safeUrl");
                safelist.add(safeinfo);
            }
            appInfo.safe = safelist;
            //解析截图信息
            ArrayList<String> screen = new ArrayList<>();
            JSONArray ja1 = jo.getJSONArray("screen");
            for (int j = 0; j < ja1.length(); j++) {
                String pic = ja1.getString(j);
                screen.add(pic);
            }
            appInfo.screen = screen;
            return appInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
