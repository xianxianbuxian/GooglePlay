package jack.example.com.googleplay.http.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jack.example.com.googleplay.domain.AppInfo;

/**
 * 应用请求网络 数据
 * Created by jack on 2017/7/21.
 */

public class Appprotocol extends Baseprotocol<ArrayList<AppInfo>> {
    @Override
    public String getKey() {
        return "app";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public ArrayList<AppInfo> parseDate(String result) {
        try {
            JSONArray ja=new JSONArray(result);
            ArrayList<AppInfo> list=new ArrayList<>();
            for (int i=0;i<ja.length();i++){
                AppInfo appInfo=new AppInfo();
                JSONObject jo = ja.getJSONObject(i);
                appInfo.des=jo.getString("des");
                appInfo.downloadUrl=jo.getString("downloadUrl");
                appInfo.stars= (float) jo.getDouble("stars");
                appInfo.iconUrl=jo.getString("iconUrl");
                appInfo.packageName=jo.getString("packageName");
                appInfo.id=jo.getString("id");
                appInfo.size=jo.getLong("size");
                appInfo.name=jo.getString("name");
               list.add(appInfo);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
