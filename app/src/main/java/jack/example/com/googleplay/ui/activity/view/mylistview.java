package jack.example.com.googleplay.ui.activity.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by jack on 2017/7/21.
 */

public class mylistview extends ListView {
    public mylistview(Context context) {
        super(context);
        ininview();
    }

    private void ininview() {
      this.setSelector(new ColorDrawable());//设置背景状态 默认选择为全透明
       this.setDivider(null);//去掉分割线
       this.setCacheColorHint(Color.TRANSPARENT);//有时候滑动listview背景会变成黑色，此方法将背景变为透明
    }

    public mylistview(Context context, AttributeSet attrs) {
        super(context, attrs);
        ininview();
    }

    public mylistview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ininview();
    }
}
