package jack.example.com.googleplay.ui.activity.fragment;

import android.view.View;

import java.util.ArrayList;

import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.domain.Categoryinfo;
import jack.example.com.googleplay.http.protocol.CategroryProtocol;
import jack.example.com.googleplay.ui.activity.Hoder.BaseHolder;
import jack.example.com.googleplay.ui.activity.Hoder.categoryholder;
import jack.example.com.googleplay.ui.activity.Hoder.titleholder;
import jack.example.com.googleplay.ui.activity.adapter.mybaseadapter;
import jack.example.com.googleplay.ui.activity.view.LodingPage;
import jack.example.com.googleplay.ui.activity.view.mylistview;

/**
 * 分类
 * <p>
 * Created by jack on 2017/7/11.
 */

public class CategoryFragment extends BaseFragment {

    private ArrayList<Categoryinfo> data;

    @Override
    public View onCreatSuccessView() {
        mylistview view = new mylistview(Uiutils.getcontext());
        view.setAdapter(new myadapter(data));
        return view;
    }

    @Override
    public LodingPage.ResuState onLoad() {
        CategroryProtocol protocol = new CategroryProtocol();
        data = protocol.getData(0);
        return check(data);
    }

    class myadapter extends mybaseadapter<Categoryinfo> {
        public myadapter(ArrayList<Categoryinfo> data) {
            super(data);
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;//在基础上加一
        }

        @Override
        public int getInnerType(int positoin) {
            //判断是否是标题类型 还是普通分类类型
            Categoryinfo info = data.get(positoin);
            if (info.istitle) {
                //是标题 返回标题类型
                return super.getInnerType(positoin) + 1;//原来基础上加一
            } else {
                //返回普通类型
                return super.getInnerType(positoin);

            }

        }

        @Override
        public BaseHolder getholder(int positoin) {
            //判断是标题类型还是不同类型  返回不同的holder
            Categoryinfo info = data.get(positoin);
            if (info.istitle) {
                //是标题
                return new titleholder();
            } else {
                //不是标题
                return new categoryholder();
            }

        }

        @Override
        public boolean hasmore() {
            return false;//没有更多数据 需要隐藏加载更多的布局
        }

        @Override
        public ArrayList<Categoryinfo> onLoadMore() {
            return null;
        }
    }
}
