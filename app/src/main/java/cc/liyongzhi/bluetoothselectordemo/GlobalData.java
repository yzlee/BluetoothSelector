package cc.liyongzhi.bluetoothselectordemo;

import android.bluetooth.BluetoothSocket;

/**
 * Created by lee on 5/30/16.
 */
public class GlobalData {

    public static final String ACTION_DATA_RECEIVED = "cc.liyongzhi.receiveddata";
    public static BluetoothSocket bluetoothSocket;
    public static Boolean readThreadRun = true;
    public static String data = "";

}
