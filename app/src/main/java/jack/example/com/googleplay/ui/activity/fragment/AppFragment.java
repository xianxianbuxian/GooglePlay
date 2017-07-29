package jack.example.com.googleplay.ui.activity.fragment;

import android.view.View;

import java.util.ArrayList;

import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.domain.AppInfo;
import jack.example.com.googleplay.http.protocol.Appprotocol;
import jack.example.com.googleplay.ui.activity.Hoder.BaseHolder;
import jack.example.com.googleplay.ui.activity.Hoder.appholder;
import jack.example.com.googleplay.ui.activity.adapter.mybaseadapter;
import jack.example.com.googleplay.ui.activity.view.LodingPage;
import jack.example.com.googleplay.ui.activity.view.mylistview;

/**
 * 首页
 * <p>
 * Created by jack on 2017/7/11.
 */

public class AppFragment extends BaseFragment {
    private ArrayList<AppInfo> data;

    @Override
    public View onCreatSuccessView() {
        mylistview view = new mylistview(Uiutils.getcontext());
        view.setAdapter(new appAdater(data));
        return view;
    }

    @Override
    public LodingPage.ResuState onLoad() {
        Appprotocol appprotocol = new Appprotocol();
        data = appprotocol.getData(0);
        return check(data);
    }

    class appAdater extends mybaseadapter<AppInfo> {

        public appAdater(ArrayList<AppInfo> data) {
            super(data);
        }

        @Override
        public BaseHolder getholder(int position) {
            return new appholder();
        }

        @Override
        public ArrayList<AppInfo> onLoadMore() {
            Appprotocol appprotocol = new Appprotocol();
            ArrayList<AppInfo> moredata = appprotocol.getData(getlistsize());
            return moredata ;
        }
    }
}
