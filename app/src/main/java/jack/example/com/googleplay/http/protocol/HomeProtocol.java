package jack.example.com.googleplay.http.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jack.example.com.googleplay.domain.AppInfo;

/**
 * 首页网络数据解析
 * Created by jack on 2017/7/20.
 */

public class HomeProtocol extends Baseprotocol<ArrayList<AppInfo>>{

    private ArrayList<String> pics;

    @Override
    public String getKey() {
        return "home";
    }

    @Override
    public String getParams() {
        return "";//如果没有参数就传空串，不要穿null
    }

    @Override
    public ArrayList<AppInfo> parseDate(String result) {
        //gson jsonobject
        //使用Jsonobject解析方式：如果遇到{}，就是jsonobjec;如果遇到[],即使jsonaaray
        try {
            JSONObject jo=new JSONObject(result);
            //解析应用列表信息
            JSONArray ja = jo.getJSONArray("list");
           ArrayList<AppInfo> appInfolist=new ArrayList<>();
            for (int i=0;i<ja.length();i++){
                AppInfo appInfo=new AppInfo();
                JSONObject jo1 = ja.getJSONObject(i);
                appInfo.des=jo1.getString("des");
                appInfo.downloadUrl=jo1.getString("downloadUrl");
                appInfo.stars= (float) jo1.getDouble("stars");
                appInfo.iconUrl=jo1.getString("iconUrl");
                appInfo.packageName=jo1.getString("packageName");
                appInfo.id=jo1.getString("id");
                appInfo.size=jo1.getLong("size");
                appInfo.name=jo1.getString("name");
                appInfolist.add(appInfo);
            }
            //初始化轮播条的数据
            JSONArray ja1 = jo.getJSONArray("picture");
            pics = new ArrayList<>();
            for (int i=0;i<ja1.length();i++){
                String pic= ja1.getString(i);
                pics.add(pic);
            }
            return appInfolist;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    public  ArrayList<String> getPicList(){
        return pics;
    }
}
