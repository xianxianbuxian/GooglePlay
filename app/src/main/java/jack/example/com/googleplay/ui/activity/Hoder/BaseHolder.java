package jack.example.com.googleplay.ui.activity.Hoder;

import android.view.View;

/**
 * Created by jack on 2017/7/18.
 */

public abstract class BaseHolder<T> {
    private final View mRootView; //一个item的根布局
    private T data;

    //当new这个对象的时候就会 加载布局 初始化控件 设置tag
    public BaseHolder() {
        mRootView = initView();
        mRootView.setTag(this);
    }

    //初始化控件和布局
    public abstract View initView();
    //返回一个item的布局对象

    public View getmRootView() {
        return mRootView;
    }

    //根据数据刷新界面
    public abstract void refreshview(T data);

    //设置数据 当前item的数据
    public void setData(T data) {
        this.data = data;
        refreshview(data);
    }

    //返回数据 当前item的数据
    public T getData() {
        return data;
    }
}
