package lingquan.firstApp.placesearch.Utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;


public class Utils {

    public static final String GOOGLE_AUTHER_KEY = "AIzaSyAAzyWWYDYdWqPNTCLN8jsfe16MzVsfSNY";

    public static int getWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int mWidth;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {
            Point size = new Point();
            display.getSize(size);
            mWidth = size.x;
        } else {
            mWidth = display.getWidth();
        }
        return mWidth;
    }
}
