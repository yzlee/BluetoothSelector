package cc.liyongzhi.bluetoothselectordemo;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

import cc.liyongzhi.dataprocessingcenter.interf.DataProcessingWarningManager;


/** 报警中心，可能出现的问题在这里统一处理
 * Created by lee on 8/17/16.
 */

public class WarningCenter implements DataProcessingWarningManager {

    private static final String TAG = "WarningCenter";

    public static void resolvedDataQueueOverflow(LinkedBlockingQueue<byte[]> resolvedData, byte[] packet) {
        Log.i(TAG, "resolvedDataQueueOverflow: true");
    }

    public static void bluetoothIOOutputStreamEmpty() {
        Log.i(TAG, "bluetoothIOOutputStreamEmpty: true");
    }

    @Override
    public void manageOriginDataQueueWarning(LinkedBlockingQueue<Byte> originData, Exception e) {

    }

    @Override
    public void manageCuttingThreadQueueWarning(LinkedBlockingQueue<Byte> originData, LinkedBlockingQueue<byte[]> cutDataQueue, Exception e) {

    }

    @Override
    public void manageSavingDataThreadFileCreateFailedWarning(Exception e) {
        e.printStackTrace();
    }
}
