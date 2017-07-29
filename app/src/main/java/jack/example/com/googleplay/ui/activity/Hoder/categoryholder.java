package jack.example.com.googleplay.ui.activity.Hoder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;

import jack.example.com.googleplay.R;
import jack.example.com.googleplay.Utils.BitmapHelper;
import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.domain.Categoryinfo;
import jack.example.com.googleplay.http.HttpHelper;

/**
 * Created by jack on 2017/7/24.
 */

public class categoryholder extends BaseHolder<Categoryinfo> implements View.OnClickListener {

    private ImageView iv_icon1;
    private ImageView iv_icon2;
    private ImageView iv_icon3;
    private TextView tv_name1;
    private TextView tv_name2;
    private TextView tv_name3;
    private BitmapUtils mBitmapUitls;
    private LinearLayout llgrid1;
    private LinearLayout llgrid2;
    private LinearLayout llgrid3;

    @Override
    public View initView() {
        View view = Uiutils.inflate(R.layout.list_item_category);
        iv_icon1 = (ImageView) view.findViewById(R.id.iv_icon1);
        iv_icon2 = (ImageView) view.findViewById(R.id.iv_icon2);

        iv_icon3 = (ImageView) view.findViewById(R.id.iv_icon3);
        tv_name1 = (TextView) view.findViewById(R.id.tv_name1);
        tv_name2 = (TextView) view.findViewById(R.id.tv_name2);
        tv_name3 = (TextView) view.findViewById(R.id.tv_name3);
        llgrid1 = (LinearLayout) view.findViewById(R.id.ll_grid1);
        llgrid2 = (LinearLayout) view.findViewById(R.id.ll_grid2);

        llgrid3 = (LinearLayout) view.findViewById(R.id.ll_grid3);
        llgrid1.setOnClickListener(this);
        llgrid2.setOnClickListener(this);
        llgrid3.setOnClickListener(this);
        mBitmapUitls = BitmapHelper.getBitmapUtils();
        return view;
    }

    @Override
    public void refreshview(Categoryinfo data) {
        tv_name1.setText(data.name1);
        tv_name2.setText(data.name2);

        tv_name3.setText(data.name3);

        mBitmapUitls.display(iv_icon1, HttpHelper.URL + "image?name=" + data.url1);
        mBitmapUitls.display(iv_icon2, HttpHelper.URL + "image?name=" + data.url2);
        mBitmapUitls.display(iv_icon3, HttpHelper.URL + "image?name=" + data.url3);
    }

    @Override
    public void onClick(View v) {
        Categoryinfo data = getData();
        switch (v.getId()) {
            case R.id.ll_grid1:
                Toast.makeText(Uiutils.getcontext(), data.name1, Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_grid2:
                Toast.makeText(Uiutils.getcontext(), data.name2, Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_grid3:
                Toast.makeText(Uiutils.getcontext(), data.name3, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
