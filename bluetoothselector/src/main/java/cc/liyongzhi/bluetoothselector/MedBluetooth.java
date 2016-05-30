package cc.liyongzhi.bluetoothselector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.HashMap;

import cc.liyongzhi.bluetoothselector.exceptions.BluetoothNotSupportException;


/**
 * Created by lee on 5/23/16.
 */
public class MedBluetooth {

    public static final String INTENT_ACTION_BLUETOOTH_ADAPTER_CANCEL_DISCOVERY = "cc.liyongzhi.action.BLUETOOTH_ADAPTER_CANCEL_DISCOVERY";

    private static BluetoothAdapter mBluetoothAdapter;
    private static BluetoothSocket mBluetoothSocket;
    private static HashMap<String, BluetoothConnectCallback> mBluetoothConnectCallbackMap = new HashMap<>();
    private static HashMap<String, String> mMacToKey = new HashMap<>();
    protected static Handler mHandler;
    private static HashMap<String, ConnectBluetoothThread> mBluetoothMap = new HashMap<>(); //防止同一mac地址多次连接。

    public static void connectBluetooth(Context context, BluetoothConnectCallback bluetoothConnectCallback) {
        connectBluetooth(context, "", true, bluetoothConnectCallback);
    }

    public static void connectBluetooth(final Context context, String mac, Boolean showConnectBluetoothActivity, BluetoothConnectCallback bluetoothConnectCallback) {

        //确认在主线程中
        if (mHandler == null && Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("please call MedBluetooth.connect in main thread");
        } else {
            if (mHandler == null) {
                mHandler = new Handler();
            }
        }

        //如果mac地址对应的callback key已经存在
        final String key = mMacToKey.get(mac) != null ? mMacToKey.get(mac) : (int)(Math.random()*10000000) + "";

        mBluetoothConnectCallbackMap.put(key, bluetoothConnectCallback);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            executeBluetoothConnectCallback(null, null, new BluetoothNotSupportException("Can't get default bluetooth adapter"), key);
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(context, OpenBluetoothActivity.class);
            context.startActivity(intent);
            return;
        }

        if (mac == null || mac.equals("")) {
            Intent intent = new Intent(context, ChooseBluetoothActivity.class);
            intent.putExtra("callback_key", key);
            context.startActivity(intent);
        } else if (showConnectBluetoothActivity) {
            Intent intent = new Intent(context, ConnectBluetoothActivity.class);
            intent.putExtra("callback_key", key);
            intent.putExtra("bluetooth_mac_address", mac);
            context.startActivity(intent);
        } else if (!showConnectBluetoothActivity){
            ConnectBluetoothThread.startUniqueConnectThread(context, mac, new SocketConnectedCallback() {
                @Override
                public void done(BluetoothSocket socket, BluetoothDevice device, IOException e) {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        MedBluetooth.executeBluetoothConnectCallback(socket, device, e, key);
                    }
                }
            });
        }
    }

    protected static void executeBluetoothConnectCallback(BluetoothSocket socket, BluetoothDevice device, Exception e, String key) {
        mBluetoothConnectCallbackMap.get(key).internalConnected(socket, device, e);
//        mBluetoothConnectCallback.internalConnected(socket, device, e);

        if (e != null) {
            mBluetoothConnectCallbackMap.remove(key);
        }
    }

    protected static void executeBluetoothDisconnectedCallback(String mac) {

        String key = mMacToKey.get(mac);

        mBluetoothConnectCallbackMap.get(key).internalDisconnected();
        mBluetoothConnectCallbackMap.remove(key);
    }

    protected static ConnectBluetoothThread getConnectThreadByMac(String mac) {
        return mBluetoothMap.get(mac);
    }

    protected static void addConnectThreadToMap(String mac, ConnectBluetoothThread thread) {
        mBluetoothMap.put(mac, thread);
    }

    protected static void removeMacFromMap(String mac) {
        mBluetoothMap.remove(mac);
    }

}
