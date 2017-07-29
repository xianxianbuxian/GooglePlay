package jack.example.com.googleplay.ui.activity;


import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import jack.example.com.googleplay.R;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.ui.activity.fragment.BaseFragment;
import jack.example.com.googleplay.ui.activity.fragment.FragmentFactory;
import jack.example.com.googleplay.ui.activity.view.PagerTab;

/**
 * 当项目和appcompat关联时要更改主题为theme.Appcompat
 */
public class MainActivity extends BaseActivity {


    @BindView(R.id.pager_tab)
    PagerTab pagerTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private String[] mTab_name;

    private Myadapter myadapter;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        myadapter = new Myadapter(getSupportFragmentManager());
        viewpager.setAdapter(myadapter);
        pagerTab.setViewPager(viewpager);//将指示器和viewpager绑定在一起
        pagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment = FragmentFactory.creatFragment(position);
                //开始加载数据
                fragment.loadData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initActionBar();
    }

    //FragmentPagerAdapter 是PagerAdapter的子类 当viewpager的页面是fragment的时候使用
    public class Myadapter extends FragmentPagerAdapter {

        public Myadapter(FragmentManager fm) {
            super(fm);
            //加载标题名字的数组
            mTab_name = Uiutils.getStingArray(R.array.tab_names);
        }

        //返回页签的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return mTab_name[position];
        }

        //返回当前fragment对象
        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.creatFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return mTab_name.length;
        }
    }

    //初始化ActionBar
    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);//home 可以点击
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);//显示左上角返回键
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_am);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.dl_drawer);
        //抽屉开关
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer_am,
                R.string.drawer_open, R.string.drawer_close);
        toggle.syncState();//同步状态， 将Drawerlayout和开关关联在一起
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //切换抽屉
                toggle.onOptionsItemSelected(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
