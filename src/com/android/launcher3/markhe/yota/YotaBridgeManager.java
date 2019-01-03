package com.android.launcher3.markhe.yota;

import android.content.Context;
import android.os.IBinder;
import android.util.Log;

import com.yotadevices.sdk.EpdCallbacks;
import com.yotadevices.sdk.EpdManager;

import java.lang.reflect.Method;

public class YotaBridgeManager {

    private static final YotaBridgeManager INSTANCE = new YotaBridgeManager();
    private BridgeCallback bridgeCallback;
    private EpdCallbacks callbacks = new EpdCallbacks() {
        public void onEpdLocked() {
            if (null != bridgeCallback) {
                bridgeCallback.onEpdLocked();
            }
        }

        public void onEpdUnlocked() {
            if (null != bridgeCallback) {
                bridgeCallback.onEpdUnlocked();
            }
        }

        public void onMirroringStarted() {
            if (null != bridgeCallback) {
                bridgeCallback.onMirroringStarted();
            }
        }

        public void onMirroringStopped() {
            if (null != bridgeCallback) {
                bridgeCallback.onMirroringStopped();
            }
        }
    };

    private YotaBridgeManager() {

    }

    public static YotaBridgeManager getInstance() {
        return INSTANCE;
    }

    public void addCallback(Context context, BridgeCallback callback) {
        if (null == bridgeCallback) {
            bridgeCallback = callback;
            EpdManager.getInstance().registerEpdCallbacks(context, callbacks);
        }
    }

    public boolean toggleMirroring(Context context) {
        try {
            if (isEpdInteractive()) {
                EpdManager.getInstance().stopMirroring();
                return false;
            }
            else {
                EpdManager.getInstance().startMirroring();
                return true;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void printMethods(Class remote) {
        try {
            Log.e("markhe", "===========" + remote.getName());
            Method[] methods = remote.getDeclaredMethods();
            for (Method m : methods) {
                Log.e("markhe", m.getName());
                for (Class p: m.getParameterTypes())  {
                    Log.e("markhe",  "param: " + p.getName());
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Object loadIMirroringManager() throws Exception {
        Class<?> clz = Class.forName("com.yotadevices.mirroring.IMirroringManager$Stub");
        Method asInterface = clz.getDeclaredMethod("asInterface", IBinder.class);
        Object remote = asInterface.invoke(null, checkService("mirroring"));

        return remote;
    }

    private Object loadEpdKeyguard() throws Exception {
        Class<?> clz = Class.forName("com.yotadevices.keyguard.IEpdKeyguardManager$Stub");
        Method asInterface = clz.getDeclaredMethod("asInterface", IBinder.class);
        Object remote = asInterface.invoke(null, checkService("epd_keyguard"));

        return remote;
    }

    private Object loadPowerManager() throws Exception {
        Class<?> clz = Class.forName("android.os.IPowerManager$Stub");
        Method asInterface = clz.getDeclaredMethod("asInterface", IBinder.class);
        Object remote = asInterface.invoke(null, checkService("power"));

        return remote;
    }

    public Boolean isMirroringStarted() throws Exception {
        Object remote = loadIMirroringManager();
        Method remoteMethod = remote.getClass().getDeclaredMethod("isMirroringStarted");
        return (Boolean)remoteMethod.invoke(remote);
    }

    public void switchToEpd() throws Exception {
        Object remote = loadEpdKeyguard();
        Method remoteMethod = remote.getClass().getDeclaredMethod("switchToEpd");
        remoteMethod.invoke(remote);
    }

    public void switchToFront() throws Exception {
        Object remote = loadEpdKeyguard();
        Method remoteMethod = remote.getClass().getDeclaredMethod("switchToFront");
        remoteMethod.invoke(remote);
    }

    public Boolean isEpdInteractive() throws Exception {
        Object remote = loadEpdKeyguard();
        Method remoteMethod = remote.getClass().getDeclaredMethod("isEpdInteractive");
        return (Boolean) remoteMethod.invoke(remote);
    }

    public void goToSleep() throws Exception {
        Object remote = loadPowerManager();
        Method remoteMethod = remote.getClass().getDeclaredMethod("goToSleep");
        remoteMethod.invoke(remote);
    }

    public void waitUp() throws Exception {
        Object remote = loadPowerManager();
        Method remoteMethod = remote.getClass().getDeclaredMethod("waitUp");
        remoteMethod.invoke(remote);
    }

    public Boolean isEpdLocked() throws Exception {
        Object remote = loadEpdKeyguard();
        Method remoteMethod = remote.getClass().getDeclaredMethod("isEpdKeyguardEnabled");
        return (Boolean) remoteMethod.invoke(remote);
    }

    public void unlockFront() throws Exception {
        Object remote = loadEpdKeyguard();
        Method remoteMethod = remote.getClass().getDeclaredMethod("hideDefaultKeyguard");
        remoteMethod.invoke(remote, Boolean.TRUE);
    }

    public void unlockEdp() throws Exception {
        Object remote = loadEpdKeyguard();
        Method remoteMethod = remote.getClass().getDeclaredMethod("hideEpdKeyguard");
        remoteMethod.invoke(remote, Boolean.TRUE);
    }

    public void lockFront() throws Exception {
        Object remote = loadEpdKeyguard();
        Method remoteMethod = remote.getClass().getDeclaredMethod("showDefaultKeyguard");
        remoteMethod.invoke(remote, Boolean.TRUE);
    }

    public void lockEdp() throws Exception {
        Object remote = loadEpdKeyguard();
        Method remoteMethod = remote.getClass().getDeclaredMethod("showEpdKeyguard");
        remoteMethod.invoke(remote, Boolean.TRUE);
    }

    public void startMirroring() throws Exception {
        Object remote = loadIMirroringManager();
        Method remoteMethod = remote.getClass().getDeclaredMethod("startMirroring");
        remoteMethod.invoke(remote);
    }

    public void stopMirroring() throws Exception {
        Object remote = loadIMirroringManager();
        Method remoteMethod = remote.getClass().getDeclaredMethod("stopMirroring");
        remoteMethod.invoke(remote);
    }

    private IBinder checkService(String name){
        IBinder iBinder = null;
        try{
            Class<?> clz = Class.forName("android.os.ServiceManager");

            Method checkService = clz.getMethod("getService", String.class);
            checkService.setAccessible(true);
            iBinder = (IBinder)checkService.invoke(null, name);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return iBinder;
    }

    public interface BridgeCallback {
        void onEpdLocked();

        void onEpdUnlocked();

        void onMirroringStarted();

        void onMirroringStopped();
    }
}
