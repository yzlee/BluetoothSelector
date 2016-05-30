package cc.liyongzhi.bluetoothselector;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Looper;

/**
 * Created by lee on 5/25/16.
 */
public abstract class BluetoothConnectCallback {

    public abstract void connected(BluetoothSocket socket, BluetoothDevice device, Exception e);
    public abstract void disconnected();

    public void internalConnected(final BluetoothSocket socket, final BluetoothDevice device, final Exception e) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            if (!MedBluetooth.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    connected(socket, device, e);
                }
            })) {

            }
        } else {
            this.connected(socket, device, e);
        }
    }

    public void internalDisconnected() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            if (!MedBluetooth.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    disconnected();
                }
            })) {

            }
        } else {
            this.disconnected();
        }
    }
}
