package cc.liyongzhi.bluetoothselectordemo;

import android.util.Log;

import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;


/** 报警中心，可能出现的问题在这里统一处理
 * Created by lee on 8/17/16.
 */

public class WarningCenter {

    private static final String TAG = "WarningCenter";

    public static void resolvedDataQueueOverflow(LinkedBlockingQueue<byte[]> resolvedData, byte[] packet) {
        Log.i(TAG, "resolvedDataQueueOverflow: true");
    }

    public static void bluetoothIOOutputStreamEmpty() {
        Log.i(TAG, "bluetoothIOOutputStreamEmpty: true");
    }

}
