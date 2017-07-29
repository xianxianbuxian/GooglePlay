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
 * Created by jack on 2017/7/24.
 */

public class DetailAppInfoHolder extends BaseHolder<AppInfo> {

    private TextView name;
    private ImageView icon;
    private RatingBar start;
    private TextView size;
    private TextView downloadnum;
    private TextView datas;
    private TextView version;
    private BitmapUtils mBitmap;



    @Override
    public View initView() {
        View view = Uiutils.inflate(R.layout.layout_detail_appinfo);
        name = (TextView) view.findViewById(R.id.tv_name);
        icon = (ImageView) view.findViewById(R.id.iv_icon);
        start = (RatingBar) view.findViewById(R.id.rb_star);
        size = (TextView) view.findViewById(R.id.tv_size);
        downloadnum = (TextView) view.findViewById(R.id.tv_download_num);
        datas = (TextView) view.findViewById(R.id.tv_date);
        version = (TextView) view.findViewById(R.id.tv_version);
        mBitmap = BitmapHelper.getBitmapUtils();
        return view;
    }

    @Override
    public void refreshview(AppInfo data) {
         mBitmap.display(icon, HttpHelper.URL+"image?name="+data.iconUrl);
        name.setText(data.name);
        downloadnum.setText("下载量:"+data.downloadNum);
        version.setText("版本号："+data.version);
        datas.setText(data.date);
        size.setText(Formatter.formatFileSize(Uiutils.getcontext(),data.size));
        start.setRating(data.stars);
    }
}
