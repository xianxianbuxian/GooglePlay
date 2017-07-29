package jack.example.com.googleplay.ui.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import jack.example.com.googleplay.Utils.Uiutils;

/**
 * Created by jack on 2017/7/26.
 */

public class MyFlowLayout extends ViewGroup {
    private int mUsedWidth;//当前行已使用的宽度
    private int mHorizontalSpacing = Uiutils.dip2px(6);//水平间距
    private int mVerticalSpacing = Uiutils.dip2px(8);//竖直间距
    private line mLine;//当前行对象
    private static final int MAX_LINE = 100;//最大行数
    private ArrayList<line> mLineList = new ArrayList<>();//维护一个所有行集合

    public MyFlowLayout(Context context) {
        super(context);
    }

    public MyFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = l + getPaddingLeft();
        int top = t + getPaddingTop();
        //遍历所有行对象，设置每行位置
        for (int i = 0; i < mLineList.size(); i++) {
            line line = mLineList.get(i);
            line.layout(left, top);
            top += line.mMaxheight + mVerticalSpacing;//更新top值
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取有效宽度
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        //获取有效高度
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        //获取宽度模式
        int widthmode = MeasureSpec.getMode(widthMeasureSpec);
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);
        int childCount = getChildCount();//获取子控件的数量
        for (int i = 0; i < childCount; i++) {
            View childview = getChildAt(i);
            //父控件是确定模式，子控件包裹内容，否则子控件模式和额父控件一致
            int childWidthmeasurespac = MeasureSpec.makeMeasureSpec(width, widthmode == MeasureSpec.EXACTLY ?
                    MeasureSpec.AT_MOST : widthmode);
            int childHeightmeasurespac = MeasureSpec.makeMeasureSpec(height, heightmode == MeasureSpec.EXACTLY ?
                    MeasureSpec.AT_MOST : heightmode);
            //开始测量
            childview.measure(childWidthmeasurespac, childHeightmeasurespac);
            //获取子控件宽度
            int childwitdh = childview.getMeasuredWidth();
            //如果当前行对象为空 初始化一个行对象
            if (mLine == null) {
                mLine = new line();
            }
            mUsedWidth += childwitdh;//当前已使用宽度增加子控件宽度
            if (mUsedWidth < width) {
                mLine.addview(childview);//给当前行添加子控件
                //判断是否超出边界
                mUsedWidth += mHorizontalSpacing;//增加一个水平间距

                if (mUsedWidth > width) {
                    //加上边界后 是否超过边界 超过需要换行
                    if (!newline()) {
                        break;//如果创建行失败 就退出循环，不要添加
                    }
                }
            } else {
                //已经超出边界
                //1 当前没有任何元素，一旦添加当前子控件，就超出边界（子控件很长）
                if (mLine.getChildCount() == 0) {
                    mLine.addview(childview);//强制添加到当前行
                    if (!newline()) { //换行
                        break;
                    }

                } else {
                    //2当前有控件 一旦添加 超出边界
                    if (!newline()) { //换行
                        break;
                    }
                    mLine.addview(childview);
                    mUsedWidth += childwitdh + mHorizontalSpacing;//更新已使用宽度
                }

            }
            //  int childheiht = childview.getMeasuredHeight();
        }
        //保存最后一行的行对象
        if (mLine != null && mLine.getChildCount() != 0 && !mLineList.contains(mLine)) {
            mLineList.add(mLine);
        }
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);//控件整体控件
        int totalHeight = 0;//整体空间高度
        for (int i = 0; i < mLineList.size(); i++) {
            line line = mLineList.get(i);
            totalHeight += line.mMaxheight;
        }
        totalHeight += (mLineList.size() - 1) * mVerticalSpacing;//增加竖直间距
        totalHeight += getPaddingTop() + getPaddingBottom();//增加上下边距
        //根据最新的宽高来测量整体布局的大小
        setMeasuredDimension(totalWidth, totalHeight);
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //换行
    public boolean newline() {
        mLineList.add(mLine);//保存上一行数据
        if (mLineList.size() < MAX_LINE) {
            //可以继续添加
            mLine = new line();
            mUsedWidth = 0;//已使用宽度要清零
            return true;//创建成功
        }
        return false;//创建失败
    }

    //每一行对象的封装
    class line {
        private int mTotalWidth;//当前所有控件的总宽度
        public int mMaxheight;//当前控件高度 （已最高的子控件为准）
        private ArrayList<View> mChildList = new ArrayList<>();//当前行所有子控件的集合

        //添加一个子控件
        public void addview(View view) {
            mChildList.add(view);
            //总宽度添加
            mTotalWidth += view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            mMaxheight = height > mMaxheight ? height : mMaxheight;
        }

        //获取孩子的数量
        public int getChildCount() {
            return mChildList.size();
        }

        //子控件位置设置
        public void layout(int left, int top) {
            int childcount = getChildCount();
            //将剩余空分配给子控件
            int totalwidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();//屏幕有效总宽度
            //计算剩余宽度
            int surplusWidth = totalwidth - mTotalWidth - (getChildCount() - 1) * mHorizontalSpacing;
            if (surplusWidth >= 0) {
                //有剩余的空间
                int space = (int) ((float) surplusWidth / childcount + 0.5f);//平均每个控件分配的大小
                //重写测量子控件
                for (int i = 0; i < childcount; i++) {
                    View childview = mChildList.get(i);
                    int measuredWidth = childview.getMeasuredWidth();
                    int measuredHeight = childview.getMeasuredHeight();
                    measuredWidth += space;//宽度增加
                    int measureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
                    int measureSpec1 = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY);
                    //重新测量控件
                    childview.measure(measureSpec, measureSpec1);
                    //当控件比较矮时 需要居中展示 竖直方向需要向下有一定偏移
                    int topoffset = (mMaxheight - measuredHeight) / 2;
                    if (topoffset < 0) {
                        topoffset = 0;
                    }
                    //设置子控件位置
                    childview.layout(left, top + topoffset, left + measuredWidth, top + topoffset + measuredHeight);
                    left += measuredWidth + mHorizontalSpacing;//更新left值
                }
            } else {
                //这个空间很长 占了整行
                View childview = mChildList.get(0);
                childview.layout(left, top, left + childview.getMeasuredWidth(),
                        top + childview.getMeasuredHeight());
            }
        }
    }
}
