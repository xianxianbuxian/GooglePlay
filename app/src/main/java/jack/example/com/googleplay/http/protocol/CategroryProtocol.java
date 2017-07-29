package jack.example.com.googleplay.http.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jack.example.com.googleplay.domain.Categoryinfo;

/**
 * 分类模块请求网络
 * Created by jack on 2017/7/23.
 */

public class CategroryProtocol extends Baseprotocol<ArrayList<Categoryinfo>> {
    @Override
    public String getKey() {
        return "category";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public ArrayList<Categoryinfo> parseDate(String result) {
        try {
            JSONArray ja = new JSONArray(result);
            ArrayList<Categoryinfo> catlist = new ArrayList<>();
            for (int i = 0; i < ja.length(); i++) {//遍历大分类，2次
                JSONObject jo = ja.getJSONObject(i);
                //判断是否有title字段
                if (jo.has("title")) {
                    //初始化标题对象
                    Categoryinfo titleinfo = new Categoryinfo();
                    titleinfo.title = jo.getString("title");
                    titleinfo.istitle = true;
                    catlist.add(titleinfo);
                }
                if (jo.has("infos")) {
                    JSONArray ja1 = jo.getJSONArray("infos");
                    for(int j=0;j<ja1.length();j++){
                        JSONObject jo1 = ja1.getJSONObject(j);
                        Categoryinfo infos=new Categoryinfo();
                       infos.name1= jo1.getString("name1");
                        infos.name2= jo1.getString("name2");
                        infos.name3= jo1.getString("name3");
                        infos.url1= jo1.getString("url1");
                        infos.url2= jo1.getString("url2");
                        infos.url3= jo1.getString("url3");
                        infos.istitle=false;
                        catlist.add(infos);
                    }
                }
            }
            return catlist;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
