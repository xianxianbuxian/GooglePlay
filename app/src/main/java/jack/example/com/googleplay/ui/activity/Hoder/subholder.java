package jack.example.com.googleplay.ui.activity.Hoder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import jack.example.com.googleplay.R;
import jack.example.com.googleplay.Utils.BitmapHelper;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.domain.subjectinfo;
import jack.example.com.googleplay.http.HttpHelper;

/**
 * 专题holder
 * Created by jack on 2017/7/21.
 */

public class subholder extends BaseHolder<subjectinfo> {

    private ImageView iv_pic;
    private TextView tv_titel;
    private BitmapUtils mBitmap;

    @Override
    public View initView() {
        View view = Uiutils.inflate(R.layout.list_item_sub);
        iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
        tv_titel = (TextView) view.findViewById(R.id.tv_title);
        mBitmap = BitmapHelper.getBitmapUtils();
        return view;
    }

    @Override
    public void refreshview(subjectinfo data) {
        tv_titel.setText(data.des);
        mBitmap.display(iv_pic, HttpHelper.URL + "image?name=" + data.url);
    }
}
