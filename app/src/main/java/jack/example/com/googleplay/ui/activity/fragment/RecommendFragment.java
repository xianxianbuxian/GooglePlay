package jack.example.com.googleplay.ui.activity.fragment;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.http.protocol.RecommendProtocol;
import jack.example.com.googleplay.ui.activity.view.LodingPage;
import jack.example.com.googleplay.ui.activity.view.fly.ShakeListener;
import jack.example.com.googleplay.ui.activity.view.fly.StellarMap;

/**
 * 推荐
 * <p>
 * Created by jack on 2017/7/11.
 */

public class RecommendFragment extends BaseFragment {

    private ArrayList<String> data;

    @Override
    public View onCreatSuccessView() {
        final StellarMap stellar = new StellarMap(Uiutils.getcontext());
        stellar.setAdapter(new Rcommendadapter());
        //随机的方式 将控件划分9行6列的格子 然后再格子随机展示
        stellar.setRegularity(6, 9);
        //更好适配
        int pading = Uiutils.dip2px(10);
        //设置内边距
        stellar.setInnerPadding(pading, pading, pading, pading);
        //设置默认界面 第一组数据
        stellar.setGroup(0, true);
        ShakeListener listener=new ShakeListener(Uiutils.getcontext());
        listener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                stellar.zoomIn();//跳到下一页数据
            }
        });
        return stellar;
    }

    @Override
    public LodingPage.ResuState onLoad() {
        RecommendProtocol protocol = new RecommendProtocol();
        data = protocol.getData(0);
        return check(data);
    }

    class Rcommendadapter implements StellarMap.Adapter {
        //返回组的个数
        @Override
        public int getGroupCount() {
            return 2;
        }

        //返回某一组的成员个数
        @Override
        public int getCount(int group) {
            int count = data.size() / getGroupCount();
            if (group == getGroupCount() - 1) {
                //最后一页 将除不尽的，余数增加在最后一页保证数据完整
                count += data.size() % getGroupCount();
            }
            return count;
        }

        //初始化布局
        @Override
        public View getView(int group, int position, View convertView) {
            //因为每组position都会从0开始计数 所以需要将前面组的个数追加起来才是 当前加载数据的脚标
            position += (group) * getCount(group - 1);
            final String keyword = data.get(position);
            TextView view = new TextView(Uiutils.getcontext());
            view.setText(keyword);
            //随机大小
            Random random = new Random();
            final int size = 25 + random.nextInt(9);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            //随机颜色
            //r g b 0-255 30-230 提出黑色和白色
            int r = 30 + random.nextInt(200);
            int g = 30 + random.nextInt(200);
            int b = 30 + random.nextInt(200);
            view.setTextColor(Color.rgb(r,g,b));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Uiutils.getcontext(),keyword,Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }

        //返回下一组的id
        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            if (isZoomIn) {
                //上下划 上一页
                if (group > 0) {
                    group--;
                } else {
                    //跳到最后一页
                    group = getGroupCount() - 1;
                }
            } else {
                //向上划 下一页
                if (group < getGroupCount() - 1) {
                    group++;
                } else {
                    group = 0;
                }
            }
            return group;
        }
    }
}
