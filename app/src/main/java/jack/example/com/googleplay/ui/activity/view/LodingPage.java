package jack.example.com.googleplay.ui.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import jack.example.com.googleplay.R;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.manager.ThreadManager;

/**
 * 根据当前状态显示不同页面的自定义控件
 * <p>
 * 未加载状态
 * 加载中
 * 加载失败
 * 数据为空
 * 加载成功
 * <p>
 * Created by jack on 2017/7/12.
 */

public abstract class LodingPage extends FrameLayout {

    private static final int STATE_LOAO_UNDO = 1;//未加载
    private static final int STATE_LOAO_LOGDIN = 2;//加载中
    private static final int STATE_LOAO_ERROR = 3;//加载失败
    private static final int STATE_LOAO_EMPTY = 4;//数据为空
    private static final int STATE_LOAO_SUCCESS = 5;//加载成功
    private int mCurrentState = STATE_LOAO_UNDO;//当前的状态
    private View mLogdingPage;
    private View mErroepage;
    private View mEmptypage;
    private View mSuccessPage;
    private ResuState resuState;

    public LodingPage(Context context) {
        super(context);
        initView();
    }


    public LodingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LodingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        if (mLogdingPage == null) {
            //初始化加载中的布局
            mLogdingPage = Uiutils.inflate(R.layout.page_logding);
            addView(mLogdingPage);//将加载中的布局添加给点钱帧布局
        }

        if (mErroepage == null) {
            //初始化加载失败布局
            mErroepage = Uiutils.inflate(R.layout.page_error);
            //点击重试
          Button retry= (Button) mErroepage.findViewById(R.id.btn_retry);
            retry.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadData();
                }
            });
            addView(mErroepage);
        }
        if (mEmptypage == null) {
            //初始化数据为空的布局
            mEmptypage = Uiutils.inflate(R.layout.page_empty);
            addView(mEmptypage);
        }
        showRingpage();
    }

    /**
     * 根据当前的状态显示那个布局
     */
    private void showRingpage() {
//        if (mCurrentState == STATE_LOAO_UNDO || mCurrentState == STATE_LOAO_LOGDIN) {
//            mLogdingPage.setVisibility(View.VISIBLE);
//        } else {
//           mLogdingPage.setVisibility(View.GONE);
//        }
        mLogdingPage.setVisibility(mCurrentState == STATE_LOAO_UNDO ||
                mCurrentState == STATE_LOAO_LOGDIN ? View.VISIBLE : View.GONE);
        mErroepage.setVisibility(mCurrentState == STATE_LOAO_ERROR ? View.VISIBLE : View.GONE);
        mEmptypage.setVisibility(mCurrentState == STATE_LOAO_EMPTY ? View.VISIBLE : View.GONE);
        //当成功布局为空，并且当前状态为成功，才初始化成功的布局
        if (mSuccessPage == null && mCurrentState == STATE_LOAO_SUCCESS) {
            mSuccessPage = onCreatSuccessView();
            if (mSuccessPage != null) {
                addView(mSuccessPage);
            }
        }
        if (mSuccessPage!=null){
            mSuccessPage.setVisibility(mCurrentState==STATE_LOAO_SUCCESS?View.VISIBLE:View.GONE);
        }

    }

    //加载成功显示的布局
    public abstract View onCreatSuccessView();

    //加载网络数据   返回值表示请求网络结束后的状态
    public abstract ResuState onLoad();

    //枚举
    public enum ResuState {
        STATE_SUCCESS(STATE_LOAO_SUCCESS),
        STATE_EMPTY(STATE_LOAO_EMPTY),
        STATE_ERROR(STATE_LOAO_ERROR);
        private int state;

        private ResuState(int state) {
            this.state = state;
        }

        public int getStae() {
            return state;
        }
    }

    /**
     * 开始准备数据
     */
    public void LoadData() {
        if (mCurrentState != STATE_LOAO_LOGDIN) {
            mCurrentState = STATE_LOAO_LOGDIN;
//            new Thread() {
//                @Override
//                public void run() {
//                    resuState = onLoad();
//                    Uiutils.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (resuState !=null){
//                                //网络加载成功后 更新网络状态
//                                mCurrentState= resuState.getStae();
//                                //根据最新的网络状态加载布局
//                                showRingpage();
//                            }
//                        }
//                    });
//
//                }
//            }.start();
          ThreadManager.getthreadpool().execute(new Runnable() {
              @Override
              public void run() {
                  resuState = onLoad();
                    Uiutils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (resuState !=null){
                                //网络加载成功后 更新网络状态
                                mCurrentState= resuState.getStae();
                                //根据最新的网络状态加载布局
                                showRingpage();
                            }
                        }
                    });
              }
          });

    }
    }
}
