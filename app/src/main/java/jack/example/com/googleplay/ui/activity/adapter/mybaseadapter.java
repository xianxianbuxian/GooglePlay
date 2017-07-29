package jack.example.com.googleplay.ui.activity.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.manager.ThreadManager;
import jack.example.com.googleplay.ui.activity.Hoder.BaseHolder;
import jack.example.com.googleplay.ui.activity.Hoder.MoreHolder;

/**
 * 对adapter的封装
 * Created by jack on 2017/7/17.
 */

public abstract class mybaseadapter<T> extends BaseAdapter {
    private static final int TYPE_NORMAL = 1;//普通布局类型
    private static final int TYPE_MORE = 0;//加载更多类型布局
    private ArrayList<T> data;

    public mybaseadapter(ArrayList<T> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size() + 1;//增加加载更多布局的数量
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder;
        if (convertView == null) {
            //当new这个对象的时候就会 加载布局 初始化控件 设置tag
            //判断是否加载更多的布局
            if (getItemViewType(position) == TYPE_MORE) {
                holder = new MoreHolder(hasmore());
            } else {
                holder = getholder(position);//子类返回具体对象
            }

        } else {
            holder = (BaseHolder) convertView.getTag();
        }
        if (getItemViewType(position) !=TYPE_MORE) {
            //刷新数据 更新界面
            holder.setData(getItem(position));
        } else {
            //加载更多布局
            //一旦加载更多布局展示出来，就开始加载更多
            //只有在有更多数据的状态下才加载更多

            MoreHolder moreholder = (MoreHolder) holder;
            if (moreholder.getData() == MoreHolder.STATE_LOAD_MORE) {
                loadMore(moreholder);
            }
        }

        return holder.getmRootView();
    }

    //子类可以重写此方法来决定是否加载更多 默认为true
    public boolean hasmore() {
        return true;
    }

    //返回布局类型个数
    @Override
    public int getViewTypeCount() {
        return 2;//返回两种参数布局，普通布局+加载更多布局
    }

    //返回当前位置应该展示那种布局
    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) {
            return TYPE_MORE;
        } else {
            return getInnerType(position);
        }

    }

    //子类可以重写此方法来更改返回布局类型
    public int getInnerType(int position) {
        return TYPE_NORMAL;
    }

    //返回当前页面的holder对象， 必须由子类实现
    public abstract BaseHolder getholder(int position);


    private boolean isLoadMore = false;//标记是否加载更多

    //加载更多数据
    public void loadMore(final MoreHolder holder) {
        if (!isLoadMore) {
            isLoadMore = true;
//            new Thread() {
//                @Override
//                public void run() {
//                    final ArrayList<T> moreData = onLoadMore();
//                    Uiutils.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (moreData != null) {
//                                //每一页有20条数据，如果返回的数据小于20条，就认为是最后一页了
//                                if (moreData.size() < 20) {
//                                    holder.setData(MoreHolder.STATE_LOAD_NONE);
//                                    Toast.makeText(Uiutils.getcontext(), "没有更多数据了",
//                                            Toast.LENGTH_SHORT).show();
//                                } else {
//                                    //有更多数据
//                                    holder.setData(MoreHolder.STATE_LOAD_MORE);
//                                }
//
//                                //将更多数据追加到当前集合中
//                                data.addAll(moreData);
//                                //刷新界面
//                                mybaseadapter.this.notifyDataSetChanged();
//                            } else {
//                                //没有数据加载失败
//                                holder.setData(MoreHolder.STATE_LOAD_ERROR);
//                            }
//                            //请求加载数据完成
//                            isLoadMore = false;
//                        }
//                    });
//
//                }
//            }.start();
            ThreadManager.getthreadpool().execute(new Runnable() {
                @Override
                public void run() {
                    final ArrayList<T> moreData = onLoadMore();
                    Uiutils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (moreData != null) {
                                //每一页有20条数据，如果返回的数据小于20条，就认为是最后一页了
                                if (moreData.size() < 20) {
                                    holder.setData(MoreHolder.STATE_LOAD_NONE);
                                    Toast.makeText(Uiutils.getcontext(), "没有更多数据了",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    //有更多数据
                                    holder.setData(MoreHolder.STATE_LOAD_MORE);
                                }

                                //将更多数据追加到当前集合中
                                data.addAll(moreData);
                                //刷新界面
                                mybaseadapter.this.notifyDataSetChanged();
                            } else {
                                //没有数据加载失败
                                holder.setData(MoreHolder.STATE_LOAD_ERROR);
                            }
                            //请求加载数据完成
                            isLoadMore = false;
                        }
                    });

                }
            });
        }


    }

    //加载更多数据，必须由子类实现
    public abstract ArrayList<T> onLoadMore();
    //返回集合的大小
    public  int getlistsize(){
        return data.size();
    }
}
