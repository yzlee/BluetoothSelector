package cc.liyongzhi.bluetoothselector;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Looper;

import java.io.IOException;

/**
 * Created by lee on 5/25/16.
 */
public abstract class SocketConnectedCallback {

    public abstract void done(BluetoothSocket socket, BluetoothDevice device, IOException e);

    public void internalDone(final BluetoothSocket socket, final BluetoothDevice device, final IOException e) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            if (!MedBluetooth.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    done(socket, device, e);
                }
            })) {

            }
        } else {
            this.done(socket, device, e);
        }
    }
}
