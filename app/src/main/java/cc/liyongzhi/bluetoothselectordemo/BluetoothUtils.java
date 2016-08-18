package cc.liyongzhi.bluetoothselectordemo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lee on 4/1/16.
 * 通过此类操作蓝牙数据的发送等。不直接使用{@link BluetoothIO}
 */
public class BluetoothUtils {

    /**
     * 向采集盒发送开始指令，基础逻辑均在此实现，比如判断是否需要发送，若输入参数为true则为强制发送。
     * @return 是否发送成功
     */
    public static synchronized void SendStartToBluetoothInBackground() {
        SendStartToBluetoothInBackground(false, new BooleanCallBack() {
            @Override
            public void onCallBack(Boolean b) {

            }
        });
    }

    public static synchronized void SendStartToBluetoothInBackground(final BooleanCallBack booleanCallBack) {
        SendStartToBluetoothInBackground(false,booleanCallBack);
    }

    public static synchronized void SendStartToBluetoothInBackground(final Boolean isNecessary, final BooleanCallBack booleanCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String time= new SimpleDateFormat("$yyyy$MM$dd$HH$mm$ss", Locale.SIMPLIFIED_CHINESE).format(new Date());
                if (isNecessary) {
//                    booleanCallBack.onCallBack(BluetoothIO.SendDataToBluetooth((BluetoothCommand.SETTIME+time+"#").getBytes()));
                    booleanCallBack.onCallBack(BluetoothIO.SendDataToBluetooth((BluetoothCommand.START).getBytes()));
                } else {
                    //判断是否需要发送
                    //蓝牙已经连接、没有收到数据、已经开单、蓝牙发送间隔小于一秒
                    if (BluetoothIO.getIsBleCanSendData()) {
//                        booleanCallBack.onCallBack(BluetoothIO.SendDataToBluetooth((BluetoothCommand.SETTIME + time + "#").getBytes()));
                        booleanCallBack.onCallBack(BluetoothIO.SendDataToBluetooth((BluetoothCommand.START).getBytes()));
                    } else {
                        booleanCallBack.onCallBack(false);
                    }
                }

            }
        }).start();

    }


    public static synchronized void SendTimeToBluetoothInBackground(final Boolean isNecessary, final BooleanCallBack booleanCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String time= new SimpleDateFormat("$yyyy$MM$dd$HH$mm$ss", Locale.SIMPLIFIED_CHINESE).format(new Date());
                if (isNecessary) {
                    booleanCallBack.onCallBack(BluetoothIO.SendDataToBluetooth((BluetoothCommand.SETTIME+time+"#").getBytes()));
                } else {
                    //判断是否需要发送
                    //蓝牙已经连接、没有开始记录数据、已经开单、蓝牙发送间隔小于一秒
                    if (BluetoothIO.getIsBleCanSendData()) {
                        booleanCallBack.onCallBack(BluetoothIO.SendDataToBluetooth((BluetoothCommand.SETTIME + time + "#").getBytes()));
                    } else {
                        booleanCallBack.onCallBack(false);
                    }
                }
            }
        }).start();

    }

    public static synchronized void SendPatientIdToBluetoothInBackground(final Boolean isNecessary, final BooleanCallBack booleanCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String patientId = Med.getPatient_id();
                String patientId = "test";
                if (isNecessary) {
                    booleanCallBack.onCallBack(BluetoothIO.SendDataToBluetooth((BluetoothCommand.SETID+patientId+"#").getBytes()));
                } else {
                    //判断是否需要发送
                    //蓝牙已经连接、没有开始记录数据、已经开单、蓝牙发送间隔小于一秒
                    if (BluetoothIO.getIsBleCanSendData()) {
                        booleanCallBack.onCallBack(BluetoothIO.SendDataToBluetooth((BluetoothCommand.SETID+patientId+"#").getBytes()));
                    } else {
                        booleanCallBack.onCallBack(false);
                    }
                }
            }
        }).start();
    }


    /**
     * 向采集盒发送结束指令，基础逻辑均在此实现，比如判断是否需要发送，若输入参数为true则为强制发送。
     * @return 是否发送成功
     */
    public static synchronized void SendStopToBluetoothInBackground() {
        SendStopToBluetoothInBackground(false, new BooleanCallBack() {
            @Override
            public void onCallBack(Boolean b) {

            }
        });
    }

    public static synchronized void SendStopToBluetoothInBackground(final BooleanCallBack booleanCallBack) {
        SendStartToBluetoothInBackground(false,booleanCallBack);
    }

    public static synchronized void SendStopToBluetoothInBackground(final Boolean isNecessary, final BooleanCallBack booleanCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isNecessary) {
                    booleanCallBack.onCallBack(BluetoothIO.SendDataToBluetooth(BluetoothCommand.STOP.getBytes()));
                } else {
                    //判断是否已经结束单子、判断蓝牙是否在连接状态、蓝牙发送间隔小于一秒、还能收到数据
                    if (BluetoothIO.getIsBleCanSendData()) {
                        booleanCallBack.onCallBack(BluetoothIO.SendDataToBluetooth(BluetoothCommand.STOP.getBytes()));
                    } else {
                        booleanCallBack.onCallBack(false);
                    }
                }
            }
        }).start();
    }



}
