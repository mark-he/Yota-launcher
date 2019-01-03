package com.android.launcher3.markhe.yota;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class YotaDisplayManager {

    private static final YotaDisplayManager INSTANCE = new YotaDisplayManager();
    private YotaDisplayManager() {

    }

    public static YotaDisplayManager getInstance() {
        return INSTANCE;
    }

    public void openParams(Context context) {
        try {
//com.android.server.wm.EpdUpdateParamsManager
            if (YotaBridgeManager.getInstance().isMirroringStarted()) {

                Intent intent = new Intent();
                String packageName = "com.baoliyota.epdparams";
                String className = "com.baoliyota.epdparams.ParamsActivity";
                intent.setClassName(packageName, className);

                context.startActivity(intent);
                Log.e("markhe", "starting ParamsActivity");
            }
        }
        catch (Exception ex) {

        }
    }
}
