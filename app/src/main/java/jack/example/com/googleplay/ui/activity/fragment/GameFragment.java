package jack.example.com.googleplay.ui.activity.fragment;

import android.view.View;
import android.widget.TextView;

import jack.example.com.googleplay.Utils.Uiutils;
import jack.example.com.googleplay.ui.activity.view.LodingPage;

/**
 *
 *
 * Created by jack on 2017/7/11.
 */

public class GameFragment extends BaseFragment {
    @Override
    public View onCreatSuccessView() {
        TextView textView=new TextView(Uiutils.getcontext());
        textView.setText("GanmFragment");
        return textView;
    }

    @Override
    public LodingPage.ResuState onLoad() {
        return LodingPage.ResuState.STATE_SUCCESS;
    }
}
