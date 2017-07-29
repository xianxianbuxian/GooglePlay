package jack.example.com.googleplay.ui.activity.Hoder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import jack.example.com.googleplay.R;
import jack.example.com.googleplay.Utils.Uiutils;

/**
 * Created by jack on 2017/7/18.
 */

public class MoreHolder extends BaseHolder<Integer> {

    //加载更多的几种状态
    //1.可以加载更多
    //2.加载更多失败
    //3.没有更多数据
    public static final int STATE_LOAD_MORE = 1;
    public static final int STATE_LOAD_ERROR = 2;
    public static final int STATE_LOAD_NONE = 3;
    private LinearLayout ll_load_more;
    private TextView ll_load_error;


    public MoreHolder(boolean isloadmore) {
        //如果有更多数据 状态为more，否则为none将此状态传给父类的data 同事刷新界面
        setData(isloadmore ? STATE_LOAD_MORE : STATE_LOAD_NONE);//setdata结束后一定会调用refreshview
    }

    @Override
    public View initView() {
        View view = Uiutils.inflate(R.layout.list_item_more);
        ll_load_more = (LinearLayout) view.findViewById(R.id.ll_load_more);
        ll_load_error = (TextView) view.findViewById(R.id.ll_load_error);
        return view;
    }

    @Override
    public void refreshview(Integer data) {
        switch (data) {
            case STATE_LOAD_MORE:
                //显示加载更多
                ll_load_more.setVisibility(View.VISIBLE);
                ll_load_error.setVisibility(View.GONE);
                break;
            case STATE_LOAD_NONE:
                //隐藏加载更多
                ll_load_more.setVisibility(View.GONE);
                ll_load_error.setVisibility(View.GONE);
                break;
            case STATE_LOAD_ERROR:
                //显示加载失败
                ll_load_more.setVisibility(View.GONE);
                ll_load_error.setVisibility(View.VISIBLE);
                break;
        }
    }
}
