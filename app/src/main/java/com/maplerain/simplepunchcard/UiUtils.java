package com.maplerain.simplepunchcard;

import android.app.Activity;
import android.view.View;

/**
 * Created by vivian on 1/5/17.
 *
 * Helper class for Android app development.
 */

public class UiUtils {

    /* findView() utility method to find a view using its id
            source taken from http://www.vogella.com/tutorials/Android/article.html

       Usage example:
            Button button = UiUtils.findView(this, R.id.button);
     */

    public static <T extends View> T findView(View root, int id) {
        return (T) root.findViewById(id);
    }
    public static <T extends View> T findView(Activity activity, int id) {
        return (T) activity.getWindow().getDecorView().getRootView().findViewById(id);
    }

}
