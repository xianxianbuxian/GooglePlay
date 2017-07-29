package jack.example.com.googleplay.ui.activity.fragment;

import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import jack.example.com.googleplay.Utils.DrawableUtils;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.http.protocol.HotProtocol;
import jack.example.com.googleplay.ui.activity.view.LodingPage;
import jack.example.com.googleplay.ui.activity.view.MyFlowLayout;

/**
 * 排行
 * <p>
 * Created by jack on 2017/7/11.
 */

public class HotFragment extends BaseFragment {

    private ArrayList<String> data;
    private int pading;

    @Override
    public View onCreatSuccessView() {
        ScrollView scrollView = new ScrollView(Uiutils.getcontext());
        // FlowLayout flowLayout = new FlowLayout(Uiutils.getcontext());
        //自定义FlowLayout
        MyFlowLayout flowLayout = new MyFlowLayout(Uiutils.getcontext());
        //支持上下划
        scrollView.addView(flowLayout);
        pading = Uiutils.dip2px(15);
        flowLayout.setPadding(pading, pading, pading, pading);//设置内边距
        //   flowLayout.setHorizontalSpacing(Uiutils.dip2px(8));//水平间距
        // flowLayout.setVerticalSpacing(Uiutils.dip2px(10));//竖直间距

        for (int i = 0; i < data.size(); i++) {
            final TextView textView = new TextView(Uiutils.getcontext());
            final String keyword = data.get(i);
            textView.setText(keyword);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            textView.setTextColor(Color.WHITE);
            textView.setPadding(pading, pading, pading, pading);
            textView.setGravity(Gravity.CENTER);
            Random random = new Random();
            //随机颜色
            //r g b 0-255 30-230 提出黑色和白色
            int r = 30 + random.nextInt(200);
            int g = 30 + random.nextInt(200);
            int b = 30 + random.nextInt(200);
            int color = 0xffcecece;
//            GradientDrawable bgnormol = DrawableUtils.getGradientDrawable(Uiutils.dip2px(6), Color.rgb(r, g, b));
//
//            GradientDrawable press= DrawableUtils.getGradientDrawable(Uiutils.dip2px(6), color);
//            StateListDrawable getselet = DrawableUtils.getselet(bgnormol, press);
            StateListDrawable select = DrawableUtils.getselect(Uiutils.dip2px(6), color, Color.rgb(r, g, b));
            textView.setBackgroundDrawable(select);
            //只有设置点击事件 选择器菜起作用
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Uiutils.getcontext(), keyword, Toast.LENGTH_SHORT).show();
                }
            });
            flowLayout.addView(textView);
        }

        return scrollView;
    }

    @Override
    public LodingPage.ResuState onLoad() {
        HotProtocol protocol = new HotProtocol();
        data = protocol.getData(0);
        return check(data);
    }
}
