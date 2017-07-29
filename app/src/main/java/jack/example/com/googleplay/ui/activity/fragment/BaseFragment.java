package jack.example.com.googleplay.ui.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.ui.activity.view.LodingPage;

/**
 * Created by jack on 2017/7/11.
 */

public abstract class BaseFragment extends Fragment {

    private LodingPage mLodingPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //显示当前类的类名
//        TextView textView=new TextView(Uiutils.getcontext());
//        textView.setText(getClass().getSimpleName());
        mLodingPage = new LodingPage(Uiutils.getcontext()) {
            @Override
            public View onCreatSuccessView() {
                //此处必须调用basefragment的oncreatSuccessview 否则栈溢出
                return BaseFragment.this.onCreatSuccessView();
            }

            @Override
            public ResuState onLoad() {
                return BaseFragment.this.onLoad();
            }
        };
        return mLodingPage;
    }

    //加载成功的布局， 必须由子类来实现

    public abstract View onCreatSuccessView();

    //初始化加载网络数据 必须由子类实现
    public abstract LodingPage.ResuState onLoad();

    /**
     * 加载数据
     */
    public void loadData() {
        if (mLodingPage != null) {
            mLodingPage.LoadData();
        }
    }

    //对网络返回数据的合法性进行校验
    public LodingPage.ResuState check(Object obj) {
        if (obj != null) {
            if (obj instanceof ArrayList) {
                //判断是否是集合
                ArrayList list = (ArrayList) obj;
                if (list.isEmpty()) {
                    return LodingPage.ResuState.STATE_EMPTY;
                } else {
                    return LodingPage.ResuState.STATE_SUCCESS;
                }
            }
        }
        return LodingPage.ResuState.STATE_ERROR;
    }
}
