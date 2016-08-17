package cc.liyongzhi.bluetoothselectordemo;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by lee on 4/1/16.
 */
public class BleFunction {

    private static boolean isBleCanSendData = true;

    /**
     * 向蓝牙发送信息的函数
     * @param bytes
     * @return
     */
    public synchronized static boolean SendDataToBluetooth(byte[] bytes) {
        boolean ret = false;
        OutputStream outputStream = null;

        try {
            outputStream = GlobalData.bluetoothSocket.getOutputStream();
            if (outputStream != null) {

                while (isBleCanSendData) {
                    isBleCanSendData = false;
                    outputStream.write(bytes);
                    Thread.sleep(1000);
                }


                Log.i("liyongzhi", "[BleFunction][SendDataToBluetooth] Send bytes " + bytes);

                ret = true;

            } else {

            }
        } catch (Exception e) { // TODO Auto-generated catch block
            e.printStackTrace();
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            ret = false;

        } finally {
            isBleCanSendData = true;
        }
        return ret;
    }

    public synchronized static boolean getIsBleCanSendData() {
        return isBleCanSendData;
    }

}
