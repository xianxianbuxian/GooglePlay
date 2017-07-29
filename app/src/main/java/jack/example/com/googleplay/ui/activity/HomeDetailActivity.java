package jack.example.com.googleplay.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import jack.example.com.googleplay.R;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.domain.AppInfo;
import jack.example.com.googleplay.http.protocol.HomeDetailProtocol;
import jack.example.com.googleplay.ui.activity.Hoder.DetailAppInfoHolder;
import jack.example.com.googleplay.ui.activity.Hoder.DetailDesHolder;
import jack.example.com.googleplay.ui.activity.Hoder.DetailPicsHolder;
import jack.example.com.googleplay.ui.activity.Hoder.DetailSafeHolder;
import jack.example.com.googleplay.ui.activity.Hoder.DetalDownloadHolder;
import jack.example.com.googleplay.ui.activity.view.LodingPage;

/**
 * 应用详情页
 * Created by jack on 2017/7/24.
 */

public class HomeDetailActivity extends BaseActivity {

    private LodingPage mLodingpage;
    private String packname;
    private AppInfo data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLodingpage = new LodingPage(this) {
            @Override
            public View onCreatSuccessView() {
                return HomeDetailActivity.this.onCreatSuccessView();
            }

            @Override
            public ResuState onLoad() {
                return HomeDetailActivity.this.onLoad();
            }
        };
        setContentView(mLodingpage);//直接将一个view对象设置给activity
        //获取homefragment 带过的包名
        packname = getIntent().getStringExtra("packname");
        //开始加载网络数据 进入onload 方法
        mLodingpage.LoadData();
        initActionBar();
    }

    public View onCreatSuccessView() {
        //初始化成功布局
        View view = Uiutils.inflate(R.layout.homedetail);
        //初始化应用信息模块
         FrameLayout fldetalappinfo= (FrameLayout) view.findViewById(R.id.fl_detail_appinfo);
        //动态给帧布局填充布局
        DetailAppInfoHolder appinhodel=new DetailAppInfoHolder();
        fldetalappinfo.addView(appinhodel.getmRootView());
        appinhodel.setData(data);
        //初始化安全模块
        FrameLayout fldetalsafe= (FrameLayout) view.findViewById(R.id.fl_detail_safe);
        DetailSafeHolder detailSafeHolder=new DetailSafeHolder();
        fldetalsafe.addView(detailSafeHolder.getmRootView());
        detailSafeHolder.setData(data);
           //初始化截图模块
        HorizontalScrollView hsvpic= (HorizontalScrollView) view.findViewById(R.id.hsv_detail_pic);
        DetailPicsHolder detailPicsHolder=new DetailPicsHolder();
        hsvpic.addView(detailPicsHolder.getmRootView());
        detailPicsHolder.setData(data);
        //初始化描述模块
        FrameLayout flDetailDes= (FrameLayout) view.findViewById(R.id.fl_detail_des);
        DetailDesHolder detailDesHolder=new DetailDesHolder();
        flDetailDes.addView(detailDesHolder.getmRootView());
        detailDesHolder.setData(data);
        //初始化下载模块
        FrameLayout download= (FrameLayout) view.findViewById(R.id.fl_detail_download);
        DetalDownloadHolder detalDownloadHolder=new DetalDownloadHolder();
        download.addView(detalDownloadHolder.getmRootView());
        detalDownloadHolder.setData(data);
        return view;
    }

    public LodingPage.ResuState onLoad() {
        HomeDetailProtocol protocol = new HomeDetailProtocol(packname);
        data = protocol.getData(0);
        if (data != null) {
            return LodingPage.ResuState.STATE_SUCCESS;
        } else {
            return LodingPage.ResuState.STATE_ERROR;
        }
    }
    //初始化ActionBar
    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//显示左上角返回键
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
