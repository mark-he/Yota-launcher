package com.android.launcher3.markhe.utils;


import android.content.Context;
import android.hardware.display.DisplayManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DualDisplayManager {
    private final static DualDisplayManager INSTANCE = new DualDisplayManager();
    private DualDisplayManager() {

    }

    public static DualDisplayManager getInstance() {
        return INSTANCE;
    }

    public Display getActiveDisplay(Context context) {

        DisplayManager mDisplayManager;//屏幕管理类
        Display[]  displays;//屏幕数组
        mDisplayManager = (DisplayManager)context.getSystemService(Context.DISPLAY_SERVICE);
        displays = mDisplayManager.getDisplays();

        for (Display d : displays) {
            if (d.getState() == Display.STATE_ON) {
                return d;
            }
        }
        return null;
    }

    public WindowManager getActiveWindowManager(Context context) {

        Context c = context.createDisplayContext(getActiveDisplay(context));
        return (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
    }

    public DisplayMetrics getActiveDisplayMetric(Context context) {
        Display display = this.getActiveDisplay(context);
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);

        return dm;
    }
}
