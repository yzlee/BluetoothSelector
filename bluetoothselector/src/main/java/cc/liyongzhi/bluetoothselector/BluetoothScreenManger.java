package cc.liyongzhi.bluetoothselector;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * 界面管理器
 * Created by lee on 5/21/16.
 */
public class BluetoothScreenManger {

    private static Stack<Activity> activityStack;
    private static BluetoothScreenManger instance;

    private BluetoothScreenManger() {

    }

    /**
     * 获取管理器实例，防止重复
     * @return 管理器实例
     */
    public static BluetoothScreenManger getScreenManger() {
        if (instance == null) {
            instance = new BluetoothScreenManger();
        }
        return instance;
    }

    /**
     * 弹出栈顶
     */
    public void popActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 弹出指定activity
     * @param activity 指定activity
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 获取栈顶activity
     * @return 得到栈顶的activity
     */
    public Activity currentActivity() {
        return activityStack.size() > 0 ? activityStack.lastElement() : null;
    }

    /**
     * 将activity放入堆栈
     * @param activity 要放入的activity
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 弹出至某个activity，用于批量关闭顶层activity
     * @param cls
     */
    public void popAllActivityUntilOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    /**
     * 弹出所有activity
     */
    public void popAllActivity() {
        while (true) {
            if (currentActivity() != null) {
                popActivity(currentActivity());
            } else {
                return;
            }
        }
    }

    /**
     * 得到栈内某个activity
     * @param cls activity名字
     * @return 得到的activity
     */
    public static Activity getActivity(Class cls) {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        }
        return null;
    }

    /**
     * 退出应用程序
     * @param context context
     */
    public void AppExit(Context context) {
        popAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

    }

}
