package jack.example.com.googleplay.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import jack.example.com.googleplay.R;
import jack.example.com.googleplay.Utils.BitmapHelper;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.http.HttpHelper;

/**
 * Created by jack on 2017/7/25.
 */

public class BigPicActivity extends Activity {

    private ArrayList<String> screen;
    private int where;
    private View bigview;
    private BitmapUtils mBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bigpic);
        mBitmap = BitmapHelper.getBitmapUtils();
        Intent intent = getIntent();
        screen = intent.getStringArrayListExtra("screen");
        where = intent.getIntExtra("where", 0);
        ViewPager vpbig = (ViewPager) findViewById(R.id.vp_big);
        vpbig.setAdapter(new myadaptere());
        vpbig.setCurrentItem(where);

    }



    class myadaptere extends PagerAdapter {
        @Override
        public int getCount() {
            return screen.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = null;
            imageView = new ImageView(Uiutils.getcontext());
            mBitmap.display(imageView, HttpHelper.URL + "image?name=" + screen.get(position));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BigPicActivity.this.finish();
                }
            });
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
