package jack.example.com.googleplay.http.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import jack.example.com.googleplay.Utils.IOUtils;
import jack.example.com.googleplay.Utils.LogUtils;
import jack.example.com.googleplay.Utils.StringUtils;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.http.HttpHelper;

/**
 * 访问网络的基类
 * Created by jack on 2017/7/20.
 */

public abstract class Baseprotocol<T> {
    public T getData(int index) {
        //先判断是否有缓存，有的话加载缓存
        String result = getCache(index);
        if (StringUtils.isEmpty(result)) {
            //如果没有缓存 或者缓存失效
            //请求服务器
            result = getDataFroServer(index);
        }
        //开始解析
        if (result != null) {
            T data = parseDate(result);
            return data;
        }
        return null;
    }

    //从网络获取数据
    //index表示的是从那个位置开始返回20条数据，用于分页
    public String getDataFroServer(int index) {
        //http://www.xxx.com/home?index=20&name=xx&age=11
        HttpHelper.HttpResult result = HttpHelper.get(HttpHelper.URL + getKey() + "?index=" +
                index + getParams());
        if (result != null) {
            String resultString = result.getString();
            LogUtils.e(resultString);
            //写缓存
            if (!StringUtils.isEmpty(resultString)) {
                setCache(index, resultString);
            }
            return resultString;
        }
        return null;
    }

    //获取网络连接的关键词  子类必须实现
    public abstract String getKey();

    //获取网络链接参数，子类必须实现
    public abstract String getParams();

    //写缓存 以url为Key 以json为value
    public void setCache(int index, String json) {
        //以url为文件名 json为文件内容 保存在本地
        File cacheDir = Uiutils.getcontext().getCacheDir();//本应用的缓存文件夹
        //生成缓存文件
        File cacheFile = new File(cacheDir, getKey() + "?index=" + index + getParams());
        FileWriter writer = null;
        try {
            writer = new FileWriter(cacheFile);
            //缓存失效截止时间
            long deadline = System.currentTimeMillis() + 30 * 60 * 1000;//半小时有效期
            writer.write(deadline + "\n");//在第一行写入缓存时间

            writer.write(json);//写入json
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }
    }

    //读缓存
    public String getCache(int index) {
        //以url为文件名 json为文件内容 保存在本地
        File cacheDir = Uiutils.getcontext().getCacheDir();//本应用的缓存文件夹
        //生成缓存文件
        File cacheFile = new File(cacheDir, getKey() + "?index=" + index + getParams());
        //判断缓存是否存在
        if (cacheFile.exists()) {
            //判断缓存是否失效
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(cacheFile));
                String s = reader.readLine();
                long deadTime = Long.parseLong(s);//截止时间
                if (System.currentTimeMillis() < deadTime) {
                    //当前时间小于 截止时间 说明缓存有效
                    //缓存有效
                    StringBuffer sb = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return sb.toString();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(reader);
            }
        }
        return null;
    }

    //解析数据
    public abstract T parseDate(String result);
}
