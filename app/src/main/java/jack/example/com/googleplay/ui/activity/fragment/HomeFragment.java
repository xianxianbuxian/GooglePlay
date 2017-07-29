package jack.example.com.googleplay.ui.activity.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.domain.AppInfo;
import jack.example.com.googleplay.http.protocol.HomeProtocol;
import jack.example.com.googleplay.ui.activity.Hoder.BaseHolder;
import jack.example.com.googleplay.ui.activity.Hoder.HomeHeadHolder;
import jack.example.com.googleplay.ui.activity.Hoder.homeholder;
import jack.example.com.googleplay.ui.activity.HomeDetailActivity;
import jack.example.com.googleplay.ui.activity.adapter.mybaseadapter;
import jack.example.com.googleplay.ui.activity.view.LodingPage;
import jack.example.com.googleplay.ui.activity.view.mylistview;

/**
 * 首页
 * <p>
 * Created by jack on 2017/7/11.
 */

public class HomeFragment extends BaseFragment {

    //private ArrayList<String> list;
    private ArrayList<AppInfo> data;
    private ArrayList<String> picList;

    @Override
    //如果加载数据成功 调用此方法 在主线程运行  只有成功走此方法
    public View onCreatSuccessView() {
//        TextView textView =new TextView(Uiutils.getcontext());
//        textView.setText("加载成功咯");
        mylistview view = new mylistview(Uiutils.getcontext());

        //给listview头布局加轮播条
        HomeHeadHolder headHolder = new HomeHeadHolder();
        view.addHeaderView(headHolder.getmRootView());//先添加头布局再添加adapter
        if (picList != null) {
            //设置轮播条数据
            headHolder.setData(picList);
        }
        view.setAdapter(new homeadapter(data));
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //减去头布局
                AppInfo appInfo = data.get(position-1);
                if (appInfo!=null){
                    Intent intent=new Intent(Uiutils.getcontext(), HomeDetailActivity.class);
                    intent.putExtra("packname",appInfo.packageName);
                    startActivity(intent);
                }

            }
        });
        return view;
    }

    //运行在子线程 可以直接执行耗时网络操作
    @Override
    public LodingPage.ResuState onLoad() {

        //请求网络
//        list = new ArrayList<>();
//        for (int i = 0; i < 50; i++) {
//            list.add("测试数据" + i);
//        }
        HomeProtocol protocol = new HomeProtocol();
        //加载第一个数据
        data = protocol.getData(0);

        picList = protocol.getPicList();
        return check(data);
    }


    class homeadapter extends mybaseadapter<AppInfo> {
        public homeadapter(ArrayList<AppInfo> data) {
            super(data);
        }

        @Override
        public BaseHolder<AppInfo> getholder(int positon) {
            return new homeholder();
        }

        @Override
        public ArrayList<AppInfo> onLoadMore() {
//            ArrayList<String> moredata=new ArrayList<>();
//            for (int i=0;i<50;i++){
//                moredata.add("测试数据1"+i);
//            }
            //   SystemClock.sleep(2000);
            HomeProtocol protocol = new HomeProtocol();
            //下一页数据的位置 等于当前集合的大小
            ArrayList<AppInfo> data = protocol.getData(getlistsize());
            return data;
        }


        //        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            viewhoder hoder;
//            if (convertView == null) {
//                hoder = new viewhoder();
//                convertView = Uiutils.inflate(R.layout.list_item_home);
//                hoder.tvcontent = (TextView) convertView.findViewById(R.id.tv_content);
//                convertView.setTag(hoder);
//            } else {
//                hoder = (viewhoder) convertView.getTag();
//            }
//
//            hoder.tvcontent.setText(getItem(position));
//            return convertView;
//        }
    }

    static class viewhoder {
        TextView tvcontent;
    }
}
