package jack.example.com.googleplay.ui.activity.Hoder;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import jack.example.com.googleplay.R;
import jack.example.com.googleplay.Utils.BitmapHelper;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.domain.AppInfo;
import jack.example.com.googleplay.http.HttpHelper;
import jack.example.com.googleplay.ui.activity.BigPicActivity;

/**
 * 应用详情页 截图
 * Created by jack on 2017/7/25.
 */

public class DetailPicsHolder extends BaseHolder<AppInfo> {
    private ImageView[] ivpic;
    private BitmapUtils mBitmap;

    @Override
    public View initView() {
        View view = Uiutils.inflate(R.layout.layout_detail_picinfo);
        ivpic = new ImageView[5];
        ivpic[0] = (ImageView) view.findViewById(R.id.iv_pic1);
        ivpic[1] = (ImageView) view.findViewById(R.id.iv_pic2);
        ivpic[2] = (ImageView) view.findViewById(R.id.iv_pic3);
        ivpic[3] = (ImageView) view.findViewById(R.id.iv_pic4);
        ivpic[4] = (ImageView) view.findViewById(R.id.iv_pic5);
        mBitmap = BitmapHelper.getBitmapUtils();
        return view;
    }

    @Override
    public void refreshview(final AppInfo data) {
        final ArrayList<String> screen = data.screen;
        for (int i=0;i<5;i++){
            if (i<screen.size()){
                 mBitmap.display(ivpic[i], HttpHelper.URL+"image?name="+screen.get(i));
                Log.e("aaa",HttpHelper.URL+"image?name="+screen.get(i));
                final int finalI = i;
                ivpic[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent( Uiutils.getcontext(), BigPicActivity.class);
                        intent.putExtra("screen",screen);
                        intent.putExtra("where", finalI);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Uiutils.getcontext().startActivity(intent);
                    }
                });
            }else {
                      ivpic[i].setVisibility(View.GONE);
            }
        }
    }
}
