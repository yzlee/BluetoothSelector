package cc.liyongzhi.bluetoothselector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;

import cc.liyongzhi.bluetoothselector.exceptions.BluetoothNotSupportException;


/**
 * Created by lee on 5/23/16.
 */
public class MedBluetooth {

    protected static final String INTENT_ACTION_BLUETOOTH_ADAPTER_CANCEL_DISCOVERY = "cc.liyongzhi.action.BLUETOOTH_ADAPTER_CANCEL_DISCOVERY";

    public static final String INTENT_BLUETOOTH_DISCONNECTED = "cc.liyongzhi.action.BLUETOOTH_DISCONNECTED";
    public static final String INTENT_BLUETOOTH_CONNECTED = "cc.liyongzhi.action.BLUETOOTH_CONNECTED";
    public static final String EXTRA_BLUETOOTH_MAC = "mac";

    private static BluetoothAdapter mBluetoothAdapter;
    private static BluetoothSocket mBluetoothSocket;
    private static HashMap<String, BluetoothConnectCallback> mBluetoothConnectCallbackMap = new HashMap<>();
    private static HashMap<String, BluetoothReadDataThread> mBluetoothReadDataThreadMap = new HashMap<>();
    private static HashMap<String, String> mMacToKey = new HashMap<>();
    protected static Handler mHandler;
    private static Context mContext;
    private static HashMap<String, ConnectBluetoothThread> mBluetoothMap = new HashMap<>(); //防止同一mac地址多次连接。

    private static final String TAG = "MedBluetooth";

    /**
     * @param context                  上下文
     * @param bluetoothConnectCallback 连接建立和取消连接后调用的回调函数
     */
    public static void connectBluetooth(Context context, BluetoothConnectCallback bluetoothConnectCallback) {
        connectBluetooth(context, "", true, bluetoothConnectCallback);
    }

    /**
     * @param context                      上下文
     * @param mac                          如果以前有保存蓝牙mac地址，则可以直接输入
     * @param showConnectBluetoothActivity 是否显示等待界面，若后台有自动重连请设置为false，不然每次连接都转圈圈。。。
     * @param bluetoothConnectCallback     连接建立和取消连接后调用的回调函数
     */
    public static void connectBluetooth(final Context context, String mac, Boolean showConnectBluetoothActivity, BluetoothConnectCallback bluetoothConnectCallback) {

        //确认在主线程中
        if (mHandler == null && Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("please call MedBluetooth.connect in main thread");
        } else {
            if (mHandler == null) {
                mHandler = new Handler();
            }
        }

        mContext = context;

        Log.d(TAG, "connectBluetooth: before put in map bluetoothConnectCallback instanceof BluetoothConnectWithDataManageCallback == " + (bluetoothConnectCallback instanceof BluetoothConnectWithDataManageCallback));


        //如果mac地址对应的callback key已经存在
        final String key = mMacToKey.get(mac) != null ? mMacToKey.get(mac) : (int) (Math.random() * 10000000) + "";

        //todo mac 为空
        Log.i("BluetoothStateChange", "final mac = " + mac);
        Log.i("BluetoothStateChange", "final key = " + mMacToKey.get(mac));

        mBluetoothConnectCallbackMap.put(key, bluetoothConnectCallback);

        Log.d(TAG, "connectBluetooth: after put in map bluetoothConnectCallback instanceof BluetoothConnectWithDataManageCallback == " + (mBluetoothConnectCallbackMap.get(key) instanceof BluetoothConnectWithDataManageCallback) + " callbackid = " + mBluetoothConnectCallbackMap.get(key).toString());


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
        } else if (!showConnectBluetoothActivity) {
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

        if (mMacToKey.get(device.getAddress()) == null) {
            mMacToKey.put(device.getAddress(), key);
        }

        Log.d(TAG, "executeBluetoothConnectCallback: mBluetoothConnectCallbackMap.get(key) instanceof BluetoothConnectWithDataManageCallback == " + (mBluetoothConnectCallbackMap.get(key) instanceof BluetoothConnectWithDataManageCallback) + " callbackid = " + mBluetoothConnectCallbackMap.get(key).toString());


        if (e == null && mBluetoothConnectCallbackMap.get(key) instanceof BluetoothConnectWithDataManageCallback) {
            Log.d(TAG, "executeBluetoothConnectCallback: mBluetoothConnectCallbackMap.get(key) instanceof BluetoothConnectWithDataManageCallback and e == " + e);
            BluetoothReadDataThread thread = new BluetoothReadDataThread(socket, (BluetoothConnectWithDataManageCallback) mBluetoothConnectCallbackMap.get(key));
            BluetoothReadDataThread oldThread = mBluetoothReadDataThreadMap.get(key);
            if (oldThread == null || oldThread.getState() == Thread.State.TERMINATED) {
                Log.d(TAG, "executeBluetoothConnectCallback: oldThread == null");
                mBluetoothReadDataThreadMap.put(key, thread);
                thread.start();
            } else {
                Log.d(TAG, "executeBluetoothConnectCallback: oldThread != null");
                oldThread.interrupt();
                mBluetoothReadDataThreadMap.put(key, thread);
                thread.start();
            }
            Log.d(TAG, "executeBluetoothConnectCallback: thread == " + thread.toString());
        } else {
            Log.d(TAG, "executeBluetoothConnectCallback: mBluetoothConnectCallbackMap.get(key) instanceof BluetoothConnectWithDataManageCallback == " + (mBluetoothConnectCallbackMap.get(key) instanceof BluetoothConnectWithDataManageCallback));
        }


        mBluetoothConnectCallbackMap.get(key).internalConnected(socket, device, e);
//        mBluetoothConnectCallback.internalConnected(socket, device, e);

        Log.i("BluetoothStateChange", "connect mac = " + device.getAddress());
        Log.i("BluetoothStateChange", "connect key = " + mMacToKey.get(device.getAddress()));


        if (e != null) {
            mBluetoothConnectCallbackMap.remove(key);
            Log.i("BluetoothStateChange", "e != null mac = " + device.getAddress());
            Log.i("BluetoothStateChange", "e != null key = " + mMacToKey.get(device.getAddress()));
        } else {
            Intent intent = new Intent(INTENT_BLUETOOTH_CONNECTED);
            intent.putExtra(EXTRA_BLUETOOTH_MAC, device.getAddress());
            mContext.sendBroadcast(intent);
        }
    }

    protected static void executeBluetoothDisconnectedCallback(String mac) {

        String key = mMacToKey.get(mac);
        Log.i("BluetoothStateChange", "disconnect mac = " + mac);
        Log.i("BluetoothStateChange", "disconnect key = " + mMacToKey.get(mac));
        if (mBluetoothConnectCallbackMap.get(key) != null) {
            BluetoothReadDataThread thread = mBluetoothReadDataThreadMap.get(key);
            if (thread != null) {
                Log.d(TAG, "executeBluetoothDisconnectedCallback: thread == " + thread.toString());
                thread.stopThread();
                thread.interrupt();
            }
            //send broadcast to user
            Intent intent = new Intent(INTENT_BLUETOOTH_DISCONNECTED);
            intent.putExtra(EXTRA_BLUETOOTH_MAC, mac);
            mContext.sendBroadcast(intent);

            mBluetoothConnectCallbackMap.get(key).internalDisconnected();
            mBluetoothConnectCallbackMap.remove(key);
        }
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
