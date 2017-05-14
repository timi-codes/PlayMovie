package tarrotsystem.com.playmovie.utilities;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by DOTECH on 13/05/2017.
 */

public class ViewUtil {
    public static void showToast(String message, Context context) {
        showMessageInToast(message, context);
    }

    private static void showMessageInToast(String message, Context ctx) {
        if (ctx != null)
            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
}
