package jack.example.com.googleplay.http.protocol;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * 排行访问网络
 * Created by jack on 2017/7/23.
 */

public class HotProtocol extends Baseprotocol<ArrayList<String>> {
    @Override
    public String getKey() {
        return "hot";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public ArrayList<String> parseDate(String result) {
        try {
            JSONArray ja=new JSONArray(result);
            ArrayList<String> list=new ArrayList<>();
            for (int i=0;i<ja.length();i++){
                String keyword = ja.getString(i);
                list.add(keyword);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
