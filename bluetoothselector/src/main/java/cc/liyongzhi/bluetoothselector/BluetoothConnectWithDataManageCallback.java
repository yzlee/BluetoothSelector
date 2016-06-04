package cc.liyongzhi.bluetoothselector;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Looper;

/**
 * Created by lee on 6/3/16.
 */
public abstract class BluetoothConnectWithDataManageCallback extends BluetoothConnectCallback {

    /**
     * 处理读取到的数据的函数，后台会有一个循环读取，若没有错误产生且有数据，则每次读取会有一个buffer，以及buffer的大小bytes。
     * @param bytes 本次读取到的字节数
     * @param buffer 字节存到这个数组中
     * @param e 错误
     */
    public abstract void dataMange(int bytes, byte[] buffer, Exception e);

    public void internalDataMange(final int bytes, final byte[] buffer, final Exception e) {
        this.dataMange(bytes, buffer, e);
    }
}
