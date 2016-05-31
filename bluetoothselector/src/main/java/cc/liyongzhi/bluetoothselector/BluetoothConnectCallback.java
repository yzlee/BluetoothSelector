package cc.liyongzhi.bluetoothselector;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Looper;

/**
 * Created by lee on 5/25/16.
 */
public abstract class BluetoothConnectCallback {

    /**
     * 连接成功或失败后调用
     * @param socket 获得的socket
     * @param device 本次连接的设备，可存下来方便下次自动重连，就不用每次都选择了。
     * @param e 错误
     */
    public abstract void connected(BluetoothSocket socket, BluetoothDevice device, Exception e);

    /**
     * 连接断开后调用，原理为监听系统广播
     */
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
