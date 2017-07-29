package jack.example.com.googleplay.ui.activity.Hoder;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import jack.example.com.googleplay.R;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.domain.AppInfo;
import jack.example.com.googleplay.domain.DownloadInfo;
import jack.example.com.googleplay.manager.DownLoadmanager;
import jack.example.com.googleplay.ui.activity.view.ProgressHorizontal;

/**
 * 下载详情页
 * Created by jack on 2017/7/27.
 */

public class DetalDownloadHolder extends BaseHolder<AppInfo> implements DownLoadmanager.DownloadObserver,View.OnClickListener {

    private DownLoadmanager mDm;
    private int mCurrentState;
    private float mProgess;
    private FrameLayout flprogess;
    private Button btnDownload;
    private ProgressHorizontal ph;

    @Override
    public View initView() {
        View view = Uiutils.inflate(R.layout.layout_detail_download);
        btnDownload = (Button) view.findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(this);
        //初始化自定义进度条
        flprogess = (FrameLayout) view.findViewById(R.id.fl_progess);
        flprogess.setOnClickListener(this);
        ph = new ProgressHorizontal(Uiutils.getcontext());

        ph.setProgressBackgroundResource(R.drawable.progress_bg);//进度条背景
        ph.setProgressResource(R.drawable.progress_normal);//进度条颜色
        ph.setProgressTextColor(Color.WHITE);//进度文字颜色
        ph.setProgressTextSize(Uiutils.dip2px(20));//进度文字大小
        //宽高填充父窗体
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        //给真布局添加自定义进度条
        flprogess.addView(ph, params);
        mDm = DownLoadmanager.getInstance();
        //注册观察者 监听状态和进度的变化
        mDm.registerObserver(this);
        return view;
    }

    @Override
    public void refreshview(AppInfo data) {
        //判断当前应用是否下载过
        DownloadInfo downloadinfo = mDm.getDownloadinfo(data);
        if (downloadinfo != null) {
            //之前下载过
            //下载状态
            mCurrentState = downloadinfo.currentState;
            //下载进度
            mProgess = downloadinfo.getProgess();
            Log.e("进来了",""+mProgess);
        } else {
            //没下载过
            mCurrentState = DownLoadmanager.STATE_NONE;
            mProgess = 0;
        }
        refreshUi(mCurrentState, mProgess);
    }

    //根据当前当前状态和进度来刷新界面
    private void refreshUi(int currentState, float progess) {
        mCurrentState = currentState;
        mProgess = progess;
        switch (currentState) {
            case DownLoadmanager.STATE_NONE://未下载
                flprogess.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("下载");
                break;
            case DownLoadmanager.STATE_DOWNLOAD://正在下载
                flprogess.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
                ph.setCenterText("");

                ph.setProgress(mProgess);//设置下载经度
                break;
            case DownLoadmanager.STATE_ERROR://下载失败
                flprogess.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("下载失败");
                break;
            case DownLoadmanager.STATE_PAUSE://下载暂停
                flprogess.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
                ph.setCenterText("暂停");
                ph.setProgress(mProgess);//设置下载经度

                break;
            case DownLoadmanager.STATE_WAITING://等待下载
                flprogess.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("等待中...");
                break;
            case DownLoadmanager.STATE_SUCCESS://安装
                flprogess.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("安装");
                break;
        }
    }

    //在主线程更新UI
    private void refreshUiOnMain(final DownloadInfo info) {
        AppInfo data = getData();
        if (data.id.equals(info.id)) {
            Uiutils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshUi(info.currentState, info.getProgess());
                }
            });
        }
    }
    //状态的更新
    @Override
    public void onDownloadStateChang(DownloadInfo info) {
        //判断下载对象是否是当前应用
//        AppInfo data = getData();
//        if (data.id.equals(info.id)){
//            Log.e("状态改变",""+info.currentState);
            refreshUiOnMain(info);
     //   }
    }

    //经度的更新 子线程  多线程中更新有时差
    @Override
    public void onDownloadProgessChang(DownloadInfo info) {
        //判断下载对象是否是当前应用
//        AppInfo data = getData();
//        if (data.id.equals(info.id)){
//            Log.e("状态改变",""+info.currentState+info.currentPos);
         //   refreshUiOnMain(info.currentState, info.getProgess());
            refreshUiOnMain(info);
     //   }
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.btn_download:
           case R.id.fl_progess:
               //根据当前状态决定下一步操作
               if (mCurrentState==DownLoadmanager.STATE_NONE||
                       mCurrentState==DownLoadmanager.STATE_ERROR||
                       mCurrentState==DownLoadmanager.STATE_PAUSE){
                   mDm.download(getData());//开始下载
               }else if (mCurrentState==DownLoadmanager.STATE_DOWNLOAD||
                       mCurrentState==DownLoadmanager.STATE_WAITING){
                   mDm.pause(getData());
               }else if (mCurrentState==DownLoadmanager.STATE_SUCCESS){
                   mDm.install(getData());

               }
               break;
       }
    }
}
