package cc.liyongzhi.bluetoothselectordemo;

import android.bluetooth.BluetoothSocket;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by lee on 5/30/16.
 */
public class GlobalData {

    public static final String ACTION_DATA_RECEIVED = "cc.liyongzhi.receiveddata";
    public static BluetoothSocket bluetoothSocket;
    public static BluetoothSocket bluetoothSocket2;
    public static Boolean readThreadRun = true;
    public static String data = "";
    public static String data2 = "";




    public static LinkedBlockingQueue<byte[]> resolvedData = new LinkedBlockingQueue<>();

    public static synchronized void putPacket(byte[] packet) {
        if (packet == null) {
            return;
        }
        boolean result = resolvedData.offer(packet);
        if (!result) {
            WarningCenter.resolvedDataQueueOverflow(resolvedData, packet);
        }
    }

}
