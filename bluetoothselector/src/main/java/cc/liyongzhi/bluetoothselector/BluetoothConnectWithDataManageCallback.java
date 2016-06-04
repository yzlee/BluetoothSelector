package cc.liyongzhi.bluetoothselector;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Looper;

/**
 * Created by lee on 6/3/16.
 */
public abstract class BluetoothConnectWithDataManageCallback extends BluetoothConnectCallback {

    public abstract void dataMange(int bytes, byte[] buffer, Exception e);

    public void internalDataMange(final int bytes, final byte[] buffer, final Exception e) {
        this.dataMange(bytes, buffer, e);
    }
}
