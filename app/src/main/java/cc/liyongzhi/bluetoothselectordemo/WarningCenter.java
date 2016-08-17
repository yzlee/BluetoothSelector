package cc.liyongzhi.bluetoothselectordemo;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by lee on 8/17/16.
 */

public class WarningCenter {

    private static final String TAG = "WarningCenter";

    public static void resolvedDataQueueOverflow(LinkedBlockingQueue<byte[]> resolvedData, byte[] packet) {
        Log.i(TAG, "resolvedDataQueueOverflow: true");
    }

}
