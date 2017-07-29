package jack.example.com.googleplay.ui.activity.Hoder;

import android.view.View;
import android.widget.TextView;

import jack.example.com.googleplay.R;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.domain.Categoryinfo;

/**
 * 分类模块 标题holder
 * Created by jack on 2017/7/24.
 */

public class titleholder extends BaseHolder<Categoryinfo> {

    private TextView mTv_title;

    @Override
    public View initView() {
        View view = Uiutils.inflate(R.layout.list_item_title);
        mTv_title = (TextView) view.findViewById(R.id.tv_cat_title);
        return view;
    }

    @Override
    public void refreshview(Categoryinfo data) {
                mTv_title.setText(data.title);
    }
}
