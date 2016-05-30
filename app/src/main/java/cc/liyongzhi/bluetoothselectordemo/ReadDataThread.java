package cc.liyongzhi.bluetoothselectordemo;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresPermission;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lee on 5/30/16.
 */
public class ReadDataThread extends Thread {

    private BluetoothSocket mBluetoothSocket;
    private InputStream mInputStream;
    private Context mContext;
    private static ReadDataThread mReadDataThread;

    private ReadDataThread(Context context) {
        mContext = context;

    }

    public static void startReadDataThread(Context context) {
        if (mReadDataThread == null) {
            mReadDataThread = new ReadDataThread(context);
            mReadDataThread.start();
        } else {
            if (!mReadDataThread.isAlive()) {
                mReadDataThread.start();
            }
        }
    }

    public void run() {

        while (GlobalData.readThreadRun) {

            try {
                mBluetoothSocket = GlobalData.bluetoothSocket;
                if (mBluetoothSocket == null) {
                    Thread.sleep(1000);
                    continue;
                }

                mInputStream = mBluetoothSocket.getInputStream();
                if (mInputStream == null) {
                    Thread.sleep(1000);
                    continue;
                }

                byte[] buffer = new byte[1024];

                while (true) {
                    int bytes = 0;

                    try {
                        bytes = mInputStream.read(buffer);
                        if (bytes > 0) {

                            byte[] datas = new byte[bytes];
                            for (int i = 0; i < bytes; i++) {
                                GlobalData.data = GlobalData.data + buffer[i] + " ";
                            }
                            Intent intent = new Intent(GlobalData.ACTION_DATA_RECEIVED);
                            mContext.sendBroadcast(intent);

                        }
                    } catch (IOException e) {
                        break;
                    }


                }



            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

            }

        }

    }

}
