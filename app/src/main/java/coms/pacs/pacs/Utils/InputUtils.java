package coms.pacs.pacs.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import coms.pacs.pacs.App;


/**
 * Created by vange on 2017/10/9.
 * 输入法的隐藏
 * TextInputLayout相关操作
 */

public class InputUtils {
    /**
     * 隐藏输入法
     * @param editText
     */
    public static void hideKeyboard(EditText editText) {
        InputMethodManager service = (InputMethodManager) App.app.getSystemService(Context.INPUT_METHOD_SERVICE);
        service.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    /**
     * 隐藏输入法
     * @param context
     */
    public static void hideKeyboard(Activity context) {
        InputMethodManager inputMethodManager = (InputMethodManager) App.app.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                    0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 隐藏输入法
     * @param dialog
     */
    public static void hideKeyboard(Dialog dialog) {
        InputMethodManager manager= (InputMethodManager) App.app.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


}
