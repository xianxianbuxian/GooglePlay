package jack.example.com.googleplay.ui.activity.Hoder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import jack.example.com.googleplay.R;
import jack.example.com.googleplay.Utils.BitmapHelper;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.domain.AppInfo;
import jack.example.com.googleplay.http.HttpHelper;

/**
 * 应用详情页  安全模块
 * Created by jack on 2017/7/25.
 */

public class DetailSafeHolder extends BaseHolder<AppInfo> {
    private ImageView[] mSafeIcons;//安全标识图片
    private ImageView[] mDesIcons;//安全描述图片
    private ImageView arrow;//
    private TextView[] mSafeDes;//安全描述文字
    private LinearLayout[] mSafeDesBar;//安全描述条目（图片加文字）
    private BitmapUtils mBitmap;
    private RelativeLayout rlDesRoot;
    private LinearLayout llsafefRoot;
    private int mDesheight;
    private LinearLayout.LayoutParams params;

    @Override
    public View initView() {
        View view = Uiutils.inflate(R.layout.layout_detail_safeinfo);
        mSafeIcons = new ImageView[4];
        mSafeIcons[0] = (ImageView) view.findViewById(R.id.iv_safe1);
        mSafeIcons[1] = (ImageView) view.findViewById(R.id.iv_safe2);
        mSafeIcons[2] = (ImageView) view.findViewById(R.id.iv_safe3);
        mSafeIcons[3] = (ImageView) view.findViewById(R.id.iv_safe4);
        mDesIcons = new ImageView[4];
        mDesIcons[0] = (ImageView) view.findViewById(R.id.iv_des1);
        mDesIcons[1] = (ImageView) view.findViewById(R.id.iv_des2);
        mDesIcons[2] = (ImageView) view.findViewById(R.id.iv_des3);
        mDesIcons[3] = (ImageView) view.findViewById(R.id.iv_des4);
        arrow = (ImageView) view.findViewById(R.id.iv_arrow);
        mSafeDes = new TextView[4];
        mSafeDes[0] = (TextView) view.findViewById(R.id.tv_des1);
        mSafeDes[1] = (TextView) view.findViewById(R.id.tv_des2);
        mSafeDes[2] = (TextView) view.findViewById(R.id.tv_des3);
        mSafeDes[3] = (TextView) view.findViewById(R.id.tv_des4);
        mSafeDesBar = new LinearLayout[4];
        mSafeDesBar[0] = (LinearLayout) view.findViewById(R.id.ll_des1);
        mSafeDesBar[1] = (LinearLayout) view.findViewById(R.id.ll_des2);
        mSafeDesBar[2] = (LinearLayout) view.findViewById(R.id.ll_des3);
        mSafeDesBar[3] = (LinearLayout) view.findViewById(R.id.ll_des4);
        rlDesRoot = (RelativeLayout) view.findViewById(R.id.rl_des_root);
        llsafefRoot = (LinearLayout) view.findViewById(R.id.ll_des_root);

        rlDesRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(isopen);
            }
        });
        mBitmap = BitmapHelper.getBitmapUtils();

        return view;
    }

    private boolean isopen = false; //标记完全模式 开关状态

    //打开或者关闭安全描述信息
    private void toggle(boolean isopens) {
        ValueAnimator animator = null;
        //属性动画
        if (isopens) {
            //关闭
            isopen = false;
            animator = ValueAnimator.ofInt(mDesheight, 0);
        } else {
            //开启
            isopen = true;
            animator = ValueAnimator.ofInt(0, mDesheight);
        }
        //动画更新的监听
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            //启动动画后悔不断的回调次方法 来获得最新的高度值
            public void onAnimationUpdate(ValueAnimator animation) {
                //获取最新的高度值
                Integer height = (Integer) animation.getAnimatedValue();
                //重新修改布局的高度
                params.height = height;
                llsafefRoot.setLayoutParams(params);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
             //动画结束时间更换右侧小箭头
                if(isopen){
                    arrow.setImageResource(R.drawable.arrow_up);
                }else {
                    arrow.setImageResource(R.drawable.arrow_down);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(200);//动画时间
        animator.start();//开启属性动画
    }

    @Override
    public void refreshview(AppInfo data) {
        ArrayList<AppInfo.Safeinfo> safe = data.safe;
        for (int i = 0; i < 4; i++) {
            if (i < safe.size()) {
                //安全标识图片
                AppInfo.Safeinfo safeinfo = safe.get(i);
                mBitmap.display(mSafeIcons[i], HttpHelper.URL + "image?name=" + safeinfo.safeUrl);
                //安全描述文字
                mSafeDes[i].setText(safeinfo.safeDes);
                //安全描述图片
                mBitmap.display(mDesIcons[i], HttpHelper.URL + "image?name=" + safeinfo.safeDesUrl);
            } else {
                //剩下不应该显示的图片
                mSafeIcons[i].setVisibility(View.GONE);
                //隐藏多余的条目
                mSafeDesBar[i].setVisibility(View.GONE);
            }
        }
        //获取安全描述的完整高度
        //不传任何值 底层自己去测量
        llsafefRoot.measure(0, 0);
        //测量后的真实高度
        mDesheight = llsafefRoot.getMeasuredHeight();
        //修改安全描述的高度为0，达到隐藏效果
        params = (LinearLayout.LayoutParams) llsafefRoot.getLayoutParams();
        params.height = 0;
        llsafefRoot.setLayoutParams(params);
    }
}
