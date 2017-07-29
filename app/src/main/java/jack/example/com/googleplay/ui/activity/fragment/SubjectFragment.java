package jack.example.com.googleplay.ui.activity.fragment;

import android.view.View;

import java.util.ArrayList;

import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.domain.subjectinfo;
import jack.example.com.googleplay.http.protocol.subjectprotocol;
import jack.example.com.googleplay.ui.activity.Hoder.BaseHolder;
import jack.example.com.googleplay.ui.activity.Hoder.subholder;
import jack.example.com.googleplay.ui.activity.adapter.mybaseadapter;
import jack.example.com.googleplay.ui.activity.view.LodingPage;
import jack.example.com.googleplay.ui.activity.view.mylistview;

/**
 * 专题
 *
 * Created by jack on 2017/7/11.
 */

public class SubjectFragment extends BaseFragment {

    private ArrayList<subjectinfo> data;

    @Override
    public View onCreatSuccessView() {
        mylistview view=new mylistview(Uiutils.getcontext());
         view.setAdapter(new Subadapter(data));
        return view;
    }

    @Override
    public LodingPage.ResuState onLoad() {
        subjectprotocol subjectprotocol = new subjectprotocol();
        data = subjectprotocol.getData(0);
        return check(data);
    }
    class Subadapter extends mybaseadapter<subjectinfo> {
        public Subadapter(ArrayList data) {
            super(data);
        }

        @Override
        public BaseHolder getholder(int position) {
            return new subholder();
        }

        @Override
        public ArrayList onLoadMore() {
            subjectprotocol subjectprotocol = new subjectprotocol();
            ArrayList<subjectinfo> moredata = subjectprotocol.getData(getlistsize());
            return moredata;
        }
    }
}
