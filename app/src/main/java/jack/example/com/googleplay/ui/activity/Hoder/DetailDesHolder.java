package jack.example.com.googleplay.ui.activity.Hoder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import jack.example.com.googleplay.R;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.domain.AppInfo;

import static android.R.id.toggle;

/**
 * Created by jack on 2017/7/25.
 */

public class DetailDesHolder extends BaseHolder<AppInfo> {

    private TextView tvdes;
    private TextView tvauthor;
    private ImageView ivarrow;
    private LinearLayout.LayoutParams params;

    @Override
    public View initView() {
        View view = Uiutils.inflate(R.layout.layout_detail_desinfo);
        tvdes = (TextView) view.findViewById(R.id.tv_detail_des);
        tvauthor = (TextView) view.findViewById(R.id.tv_detail_author);
        ivarrow = (ImageView) view.findViewById(R.id.iv_arrow);
        RelativeLayout rltoggle = (RelativeLayout) view.findViewById(R.id.rl_detail_toggle);
        rltoggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }


    @Override
    public void refreshview(AppInfo data) {
        tvdes.setText(data.des);
        tvauthor.setText(data.author);
        //放在消息队列里运行，解决只有三行描述时也是7行高度的bug
        tvdes.post(new Runnable() {
            @Override
            public void run() {
                //默认展示7行高度值
                int shorheight = getshorheight();
                params = (LinearLayout.LayoutParams) tvdes.getLayoutParams();
                params.height = shorheight;
                tvdes.setLayoutParams(params);
            }
        });

    }
    private boolean isopen = false;

    private void toggle() {
        int shorheight = getshorheight();
        int longheight = getlongheight();
            ValueAnimator animator=null;
            if (isopen) {
                isopen = false;
                if (longheight>shorheight){
                animator = ValueAnimator.ofInt(longheight, shorheight);}
            } else {

                isopen = true;
                if (longheight>shorheight){
                animator = ValueAnimator.ofInt(shorheight, longheight);}
            }

        if (animator!=null){
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer height = (Integer) animation.getAnimatedValue();
                    params.height=height;
                    tvdes.setLayoutParams(params);
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (isopen){
                      ivarrow.setImageResource(R.drawable.arrow_up);
                    }else {
                        ivarrow.setImageResource(R.drawable.arrow_down);
                    }
                    //scrollview 要滑动到最底部
                    final ScrollView scrollView = getScrollView();
                    //view对象也能post 一个runnable 对象
                    //为了运行更加安全和稳定 可以将活动到底部的方法放到消息队列里面执行
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.setDuration(200);
            animator.start();
        }

        }



    /**
     * 获取7行textview的高度
     */
    public int getshorheight() {
        //模拟一个textview，设置最大行数为7行，计算该模拟textview的高度
        //从而展示tvdes展示7行时应该多高
        //宽度
        int width = tvdes.getMeasuredWidth();

        TextView textView = new TextView(Uiutils.getcontext());
        textView.setText(getData().des);//设置文字
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);//文字大小一致
        textView.setMaxLines(7);//最大行数为7行

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width,
                View.MeasureSpec.EXACTLY);//宽不变 确定值 matchparent
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000,
                View.MeasureSpec.AT_MOST);//高度包裹内容 参1 当包裹内容时 表示尺寸的最大值 暂写2000也可以是屏幕高度
        //开始测量
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();//返回测量后的高度

    }

    /**
     * 获取完整textview的高度
     */
    public int getlongheight() {
        //模拟一个textview，设置最大行数为7行，计算该模拟textview的高度
        //从而展示tvdes展示7行时应该多高
        //宽度
        int width = tvdes.getMeasuredWidth();

        TextView textView = new TextView(Uiutils.getcontext());
        textView.setText(getData().des);//设置文字
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);//文字大小一致
        //textView.setMaxLines(7);//最大行数为7行

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width,
                View.MeasureSpec.EXACTLY);//宽不变 确定值 matchparent
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000,
                View.MeasureSpec.AT_MOST);//高度包裹内容 参1 当包裹内容时 表示尺寸的最大值 暂写2000也可以是屏幕高度
        //开始测量
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();//返回测量后的高度

    }
    //获取Scrollview 一层一层网上找 直到找到scrollview;注意一定要保证
    // 父控件或者祖宗控件有scrollview 否则就是死循环
    private ScrollView getScrollView(){
        ViewParent parent = tvdes.getParent();
        while(!(parent instanceof ScrollView)) {
              parent= parent.getParent();
        }
        return (ScrollView)parent;
    }


}
