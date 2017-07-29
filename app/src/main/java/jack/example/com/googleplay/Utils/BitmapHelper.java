package jack.example.com.googleplay.Utils;

import com.lidroid.xutils.BitmapUtils;

/**
 * Created by jack on 2017/7/21.
 */

public class BitmapHelper {
    private static BitmapUtils mBitmapUtilis=null;
    //单例懒汉模式
    public  static BitmapUtils getBitmapUtils(){
       if (mBitmapUtilis==null){
           synchronized (BitmapHelper.class){
               if (mBitmapUtilis==null){
                   mBitmapUtilis=new BitmapUtils(Uiutils.getcontext());
               }
           }

       }
       return mBitmapUtilis;
    }
}
