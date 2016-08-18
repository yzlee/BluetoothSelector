package cc.liyongzhi.bluetoothselectordemo;

import java.io.IOException;
import java.io.OutputStream;

/** 最底层向蓝牙发送数据的函数，非异步，如需使用请在{@link BluetoothUtils}中加个壳。
 * Created by lee on 4/1/16.
 */
public class BluetoothIO {

    private static boolean isBleCanSendData = true;

    /**
     * 向蓝牙发送信息的函数，可以设置发送间隔防止出现追尾的错误。
     * @param bytes
     * @return
     */
    public synchronized static boolean SendDataToBluetooth(byte[] bytes) {
        return SendDataToBluetooth(bytes, 1000);
    }

    public synchronized static boolean SendDataToBluetooth(byte[] bytes, int sleepMillimeters) {
        boolean ret = false;
        OutputStream outputStream = null;
        try {
            outputStream = Med.bluetoothSocket.getOutputStream();
            if (outputStream != null) {
                while (isBleCanSendData) {
                    isBleCanSendData = false;
                    outputStream.write(bytes);
                    Thread.sleep(sleepMillimeters);
                }
                ret = true;
            } else {
                //报告错误
                WarningCenter.bluetoothIOOutputStreamEmpty();
            }
        } catch (Exception e) { // TODO Auto-generated catch block
            e.printStackTrace();
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
