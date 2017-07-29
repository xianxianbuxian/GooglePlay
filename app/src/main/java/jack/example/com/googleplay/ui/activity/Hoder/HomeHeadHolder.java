package jack.example.com.googleplay.ui.activity.Hoder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import jack.example.com.googleplay.R;
import jack.example.com.googleplay.Utils.BitmapHelper;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.http.HttpHelper;

/**
 * 首页轮播条holder
 * Created by jack on 2017/7/24.
 */

public class HomeHeadHolder extends BaseHolder<ArrayList<String>> {
    private ArrayList<String> data;
    private ViewPager pager;
    private LinearLayout llcontainer;
   private int mPreviouspos;//上个圆点位置
    @Override
    public View initView() {

        //创建相对布局
        RelativeLayout rlroot = new RelativeLayout(Uiutils.getcontext());
        //初始化布局参数，根布局上层控件是listview,所有使用的是listview的layoutparam
        ListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                Uiutils.dip2px(180));
        rlroot.setLayoutParams(params);

        //viewpager
        pager = new ViewPager(Uiutils.getcontext());
        RelativeLayout.LayoutParams rlparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        rlroot.addView(pager, rlparams);//吧viewpager添加给相对布局

        //初始化指示器
        llcontainer = new LinearLayout(Uiutils.getcontext());
        llcontainer.setOrientation(LinearLayout.HORIZONTAL);//水平方向
        RelativeLayout.LayoutParams rlcontainerparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置内边距
        int pading = Uiutils.dip2px(10);
        llcontainer.setPadding(pading, pading, pading, pading);
        //添加规则，设置展示位置
        rlcontainerparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//底部对齐
        rlcontainerparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);//右边对齐


        //添加布局
        rlroot.addView(llcontainer, rlcontainerparams);
        return rlroot;
    }

    @Override
    public void refreshview(final ArrayList<String> data) {
        this.data = data;
        //填空viewpager的数据
        pager.setAdapter(new Homeheadadaptere());
        pager.setCurrentItem(data.size() * 10000);

        //初始化指示器
        for (int i = 0; i < data.size(); i++) {
            ImageView point = new ImageView(Uiutils.getcontext());
          LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,
                  LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i==0){
                //第一个默认选中
                point.setImageResource(R.drawable.indicator_selected);
            }else {
                point.setImageResource(R.drawable.indicator_normal);
                params.leftMargin=Uiutils.dip2px(8);//设置左边距
            }
            point.setLayoutParams(params);
            llcontainer.addView(point);
        }

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position=position%data.size();
                //当前点选中
               ImageView view= (ImageView) llcontainer.getChildAt(position);
                view.setImageResource(R.drawable.indicator_selected);
                //上个点变成不选中
                ImageView preview= (ImageView) llcontainer.getChildAt(mPreviouspos);
                preview.setImageResource(R.drawable.indicator_normal);
                mPreviouspos=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        HomeHeaderTask task = new HomeHeaderTask();
        task.start();


    }

    class HomeHeaderTask implements Runnable {
        public void start() {
            //移除消息 再次进入时 上一个消息可能没有移除 影响到现在的消息 避免消息重复
            Uiutils.gethandler().removeCallbacksAndMessages(null);
            Uiutils.gethandler().postDelayed(this, 3000);
        }

        @Override
        public void run() {
            int currentItem = pager.getCurrentItem();
            currentItem++;
            pager.setCurrentItem(currentItem);
            //继续发送延迟3秒消息 实现内循环
            Uiutils.gethandler().postDelayed(this, 3000);
        }
    }

    class Homeheadadaptere extends PagerAdapter {

        private final BitmapUtils bitmapUtils;

        public Homeheadadaptere() {
            bitmapUtils = BitmapHelper.getBitmapUtils();
        }

        @Override
        public int getCount() {
            //  return data.size();
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % data.size();
            String url = data.get(position);
            ImageView view = new ImageView(Uiutils.getcontext());
            bitmapUtils.display(view, HttpHelper.URL + "image?name=" + url);
            container.addView(view);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
