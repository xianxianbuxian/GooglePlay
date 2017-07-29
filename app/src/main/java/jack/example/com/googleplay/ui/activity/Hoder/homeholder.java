package jack.example.com.googleplay.ui.activity.Hoder;

import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import jack.example.com.googleplay.R;
import jack.example.com.googleplay.Utils.BitmapHelper;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.domain.AppInfo;
import jack.example.com.googleplay.domain.DownloadInfo;
import jack.example.com.googleplay.http.HttpHelper;
import jack.example.com.googleplay.manager.DownLoadmanager;
import jack.example.com.googleplay.ui.activity.view.ProgressArc;

/**
 * 首页holdet
 * Created by jack on 2017/7/18.
 */

public class homeholder extends BaseHolder<AppInfo> implements View.OnClickListener, DownLoadmanager.DownloadObserver {
    private BitmapUtils mBitmapUitls;
    private DownLoadmanager mDm;
    ImageView ivIcon;

    TextView tvName;

    RatingBar rbStart;

    TextView tvSize;

    TextView tvDes;
    private ProgressArc pbProgress;
    private FrameLayout flprogess;
    private int mCurrentState;
    private float mProgress;
    private  TextView tvDownload;
    @Override
    public View initView() {
        //加载布局
        View homeview = Uiutils.inflate(R.layout.list_item_home);
        ivIcon= (ImageView) homeview.findViewById(R.id.iv_icon);
        tvDes= (TextView) homeview.findViewById(R.id.tv_des);
        tvName= (TextView) homeview.findViewById(R.id.tv_name);
       tvSize= (TextView) homeview.findViewById(R.id.tv_size);
        rbStart= (RatingBar) homeview.findViewById(R.id.rb_start);
        tvDownload=(TextView) homeview.findViewById(R.id.tv_download);
        //初始化控件
        //mBitmapUitls=new BitmapUtils(Uiutils.getcontext());
        mBitmapUitls= BitmapHelper.getBitmapUtils();
        //初始化进度条
        flprogess = (FrameLayout) homeview.findViewById(R.id.fl_progress);
        pbProgress = new ProgressArc(Uiutils.getcontext());
        //初始化圆形进度条

        //设置圆形进度条直径
        pbProgress.setArcDiameter(Uiutils.dip2px(26));
        //设置进度条颜色
        pbProgress.setProgressColor(Uiutils.getColor(R.color.progress));
        //设置进度条宽高布局参数
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                Uiutils.dip2px(27), Uiutils.dip2px(27));
        flprogess.addView(pbProgress, params);

        flprogess.setOnClickListener(this);
        mDm = DownLoadmanager.getInstance();
        //注册观察者 监听状态和进度的变化
        mDm.registerObserver(this);
        return homeview;
    }


    @Override
    public void refreshview(AppInfo data) {
        tvName.setText(data.name);
        tvSize.setText(Formatter.formatFileSize(Uiutils.getcontext(), data.size));
        tvDes.setText(data.des);
        rbStart.setRating(data.stars);
        //  ivIcon.
        mBitmapUitls.display(ivIcon, HttpHelper.URL+"image?name="+data.iconUrl);
        //判断当前应用是否下载过
        DownloadInfo downloadinfo = mDm.getDownloadinfo(data);
        if (downloadinfo != null) {
            //之前下载过
            //下载状态
            mCurrentState = downloadinfo.currentState;
            //下载进度
            mProgress = downloadinfo.getProgess();
            Log.e("进来了",""+ mProgress);
        } else {
            //没下载过
            mCurrentState = DownLoadmanager.STATE_NONE;
            mProgress = 0;
        }
        refreshUI( mProgress,mCurrentState,getData().id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_progress:
                // 根据当前状态来决定相关操作
                if (mCurrentState == DownLoadmanager.STATE_NONE
                        || mCurrentState == DownLoadmanager.STATE_PAUSE
                        || mCurrentState == DownLoadmanager.STATE_ERROR) {
                    // 开始下载
                mDm.download(getData());
                } else if (mCurrentState ==  DownLoadmanager.STATE_DOWNLOAD
                        || mCurrentState == DownLoadmanager.STATE_WAITING) {
                    // 暂停下载
                mDm.pause(getData());
                } else if (mCurrentState == DownLoadmanager.STATE_SUCCESS) {
                    // 开始安装
                mDm.install(getData());
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void onDownloadStateChang(DownloadInfo info) {
        refreshUiOnMain(info);
    }

    @Override
    public void onDownloadProgessChang(DownloadInfo info) {
        refreshUiOnMain(info);
    }
    //在主线程更新UI
    private void refreshUiOnMain(final DownloadInfo info) {
        AppInfo data = getData();
        if (data.id.equals(info.id)) {
            Uiutils.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    refreshUI(info.getProgess(), info.currentState,info.id);
                }
            });
        }
    }
    /**
     * 刷新界面
     *
     * @param progress
     * @param state
     */
    private void refreshUI(float progress, int state,String id) {
        //由于listvie的重用机制 要刷新之前 确保是同一个应用
        if (!getData().id.equals(id)){
            return;
        }
        mCurrentState = state;
        mProgress = progress;
        switch (state) {
            case DownLoadmanager.STATE_NONE:
                //自定义控件的背景
                pbProgress.setBackgroundResource(R.drawable.ic_download);
                //没有进度
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText(Uiutils.getString(R.string.app_state_download));
                break;
            case DownLoadmanager.STATE_WAITING:
                pbProgress.setBackgroundResource(R.drawable.ic_download);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
                tvDownload.setText(Uiutils.getString(R.string.app_state_waiting));
                break;
            case DownLoadmanager.STATE_DOWNLOAD:
                pbProgress.setBackgroundResource(R.drawable.ic_pause);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
                pbProgress.setProgress(progress, true);
                tvDownload.setText((int) (progress * 100) + "%");
                break;
            case DownLoadmanager.STATE_PAUSE:
                pbProgress.setBackgroundResource(R.drawable.ic_resume);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
              //  flprogess.setVisibility(View.VISIBLE);
                break;
            case DownLoadmanager.STATE_ERROR:
                pbProgress.setBackgroundResource(R.drawable.ic_redownload);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText(Uiutils.getString(R.string.app_state_error));
                break;
            case DownLoadmanager.STATE_SUCCESS:
                pbProgress.setBackgroundResource(R.drawable.ic_install);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText(Uiutils.getString(R.string.app_state_downloaded));
                break;

            default:
                break;
        }
    }

}
