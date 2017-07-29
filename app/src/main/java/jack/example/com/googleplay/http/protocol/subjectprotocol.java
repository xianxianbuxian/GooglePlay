package jack.example.com.googleplay.http.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jack.example.com.googleplay.domain.subjectinfo;

/**
 * 主专题网络请求
 * Created by jack on 2017/7/21.
 */

public class subjectprotocol extends Baseprotocol<ArrayList<subjectinfo>> {
    @Override
    public String getKey() {
        return "subject";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public ArrayList<subjectinfo> parseDate(String result) {
        try {
            JSONArray ja = new JSONArray(result);

            ArrayList<subjectinfo> sublist = new ArrayList<>();
            for (int i = 0; i < ja.length(); i++) {
                subjectinfo subjectinfo = new subjectinfo();
                JSONObject jo = ja.getJSONObject(i);
                subjectinfo.des = jo.getString("des");
                subjectinfo.url = jo.getString("url");
                sublist.add(subjectinfo);
            }
            return sublist;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
