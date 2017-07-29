package jack.example.com.googleplay.Utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by jack on 2017/7/23.
 */

public class DrawableUtils {
    //获取一个shape对象
    public static GradientDrawable getGradientDrawable(int radius, int rgb){
        //xml中定义的shape标签 对应此类
        GradientDrawable shape=new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);//矩形
        shape.setCornerRadius(radius);//圆角半径
        shape.setColor(rgb);//颜色
        return shape;
    }
    //获取状态选择器
    public  static StateListDrawable getselet(Drawable nomol, Drawable press){
        StateListDrawable select=new StateListDrawable();
        select.addState(new int[]{android.R.attr.state_pressed},press);//按下图片
        select.addState(new int[]{},nomol);
        return select;
    }
    public  static StateListDrawable getselect (int radius, int press,int nomol){
        GradientDrawable bgnormol = getGradientDrawable(radius, nomol);
        GradientDrawable bgpress = getGradientDrawable(radius, press);
        StateListDrawable select = getselet(bgnormol, bgpress);
        return select;
    }
}
