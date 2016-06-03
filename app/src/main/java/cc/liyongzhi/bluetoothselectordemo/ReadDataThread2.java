package cc.liyongzhi.bluetoothselectordemo;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lee on 5/30/16.
 */
public class ReadDataThread2 extends Thread {

    private BluetoothSocket mBluetoothSocket;
    private InputStream mInputStream;
    private Context mContext;
    private static ReadDataThread2 mReadDataThread;

    private ReadDataThread2(Context context) {
        mContext = context;

    }

    public static void startReadDataThread(Context context) {
        if (mReadDataThread == null) {
            mReadDataThread = new ReadDataThread2(context);
            mReadDataThread.start();
        } else {

            if (!mReadDataThread.isAlive() && mReadDataThread.getState() != State.RUNNABLE) {
                try {
                    mReadDataThread.start();
                } catch (Exception e) {

                }

            }
        }
    }

    public void run() {

            try {
                mBluetoothSocket = GlobalData.bluetoothSocket2;
                if (mBluetoothSocket == null) {
                    Thread.sleep(1000);
                    return;
                }

                mInputStream = mBluetoothSocket.getInputStream();
                if (mInputStream == null) {
                    Thread.sleep(1000);
                    return;
                }

                byte[] buffer = new byte[1024];

                Log.i("liyongzhi", "get in to while(true)");
                while (true) {
                    int bytes = 0;

                    try {
                        bytes = mInputStream.read(buffer);
                        if (bytes > 0) {

                            for (int i = 0; i < bytes; i++) {
                                GlobalData.data2 = GlobalData.data2 + buffer[i] + " ";
                                if (GlobalData.data2.length() > 200) {
                                    GlobalData.data2 = "";
                                }
                            }
                            Intent intent = new Intent(GlobalData.ACTION_DATA_RECEIVED);
                            mContext.sendBroadcast(intent);
                            Log.i("liyongzhi", "after sendBroadcast and GlobalData.data = " + GlobalData.data2);

                        }
                    } catch (IOException e) {
                        Thread.sleep(1000);
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
