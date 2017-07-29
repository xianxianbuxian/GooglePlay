package jack.example.com.googleplay.ui.activity.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import jack.example.com.googleplay.R;

/**
 * 自定义控件，按比例来决定布局高度
 * Created by jack on 2017/7/22.
 */

public class RatioLayout extends FrameLayout {

    private float ratio;

    public RatioLayout(Context context) {
        super(context);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取属性值
        //attrs.getAttributeFloatValue("","ratio",-1);
        //当自定一个属性时 系统会自动生成属性id 此id通过R.styleable来引用
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        //id=属性名_字段名称（系统自动生成）
        ratio = typedArray.getFloat(R.styleable.RatioLayout_ratio, -1);
        Log.e("ration",""+ratio);
        typedArray.recycle();//回收typearry 提高性能

    }

    public RatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //1.获取到宽度
        //2.根据宽度和比例 计算出控件的高度
        //3.重新测量控件

        int widthmode = MeasureSpec.getMode(widthMeasureSpec);//获取宽度模式
        int width = MeasureSpec.getSize(widthMeasureSpec);//获取宽度值
//        MeasureSpec.AT_MOST;//至多模式，控件有多大显示多大 类似warp content
//        MeasureSpec.EXACTLY; 确定模式，类似宽高写死dip,mach parent
//        MeasureSpec.UNSPECIFIED 为指定模式，

        int hightmode = MeasureSpec.getMode(heightMeasureSpec);//获取高度模式
        //int height = MeasureSpec.getSize(heightMeasureSpec);
        //宽度确定  高度不确定 ratio 合法 才计算高度值
        if (widthmode == MeasureSpec.EXACTLY && hightmode!= MeasureSpec.EXACTLY && ratio > 0) {
            //图片宽度
            int imagewidth = width - getPaddingLeft() - getPaddingRight();
//            Log.e("  imagewidth ",""+ imagewidth);
            //图片高度=图片宽度/图片比例
            int imagehight = (int) (imagewidth / ratio + 0.5f);
//            Log.e(" imagehight ",""+imagehight );
            //测量高度
            int   height = imagehight + getPaddingTop() + getPaddingBottom();
//            Log.e(" height",""+height);
            //根据最新的高度来重新生成heightMeasureSpec（高度模式是确定模式）
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        //按照最新的高度来测量控件
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
