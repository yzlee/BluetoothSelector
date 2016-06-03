package cc.liyongzhi.bluetoothselector;

import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lee on 6/3/16.
 */
public class BluetoothReadDataThread extends Thread {

    private BluetoothSocket mSocket;
    private BluetoothConnectWithDataManageCallback mConnectCallback;

    public BluetoothReadDataThread(BluetoothSocket socket, BluetoothConnectWithDataManageCallback connectCallback) {
        mSocket = socket;
        mConnectCallback = connectCallback;
    }

    public void run() {

        try {
            if (mSocket == null) {
                mConnectCallback.internalDataMange(0, null, new Exception("Socket 为空"));
                return;
            }

            InputStream inputStream = mSocket.getInputStream();

            if (inputStream == null) {
                mConnectCallback.internalDataMange(0, null, new Exception("InputStream 创建失败"));
                return;
            }

            byte[] buffer = new byte[1024];

            while (true) {
                int bytes = 0;

                try {
                    bytes = inputStream.read(buffer);
                    if (bytes > 0) {

                        mConnectCallback.internalDataMange(bytes, buffer, null);

                    }
                } catch (IOException e) {
                    mConnectCallback.internalDataMange(bytes, buffer, new Exception("处理数据出错！"));
                }
            }

        } catch (Exception e) {
            mConnectCallback.internalDataMange(0, null, new Exception("处理数据线程出错！"));
        }

    }


}
