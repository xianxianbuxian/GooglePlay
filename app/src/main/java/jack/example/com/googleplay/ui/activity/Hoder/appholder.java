package jack.example.com.googleplay.ui.activity.Hoder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import jack.example.com.googleplay.R;
import jack.example.com.googleplay.Utils.BitmapHelper;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.domain.AppInfo;
import jack.example.com.googleplay.http.HttpHelper;

/**
 * 应用holdet
 * Created by jack on 2017/7/18.
 */

public class appholder extends BaseHolder<AppInfo> {
    private BitmapUtils mBitmapUitls;

    ImageView ivIcon;

    TextView tvName;

    RatingBar rbStart;

    TextView tvSize;

    TextView tvDes;

    @Override
    public View initView() {
        //加载布局
        View homeview = Uiutils.inflate(R.layout.list_item_home);
        ivIcon= (ImageView) homeview.findViewById(R.id.iv_icon);
        tvDes= (TextView) homeview.findViewById(R.id.tv_des);
        tvName= (TextView) homeview.findViewById(R.id.tv_name);
       tvSize= (TextView) homeview.findViewById(R.id.tv_size);
        rbStart= (RatingBar) homeview.findViewById(R.id.rb_start);

        //初始化控件
        //mBitmapUitls=new BitmapUtils(Uiutils.getcontext());
        mBitmapUitls= BitmapHelper.getBitmapUtils();
        return homeview;
    }


    @Override
    public void refreshview(AppInfo data) {
        tvName.setText(data.name);
        tvSize.setText(Formatter.formatFileSize(Uiutils.getcontext(), data.size));
        tvDes.setText(data.des);
        rbStart.setRating(data.stars);
        //  ivIcon.
        mBitmapUitls.display(ivIcon, HttpHelper.URL+"image?name="+data.iconUrl);
    }
}
