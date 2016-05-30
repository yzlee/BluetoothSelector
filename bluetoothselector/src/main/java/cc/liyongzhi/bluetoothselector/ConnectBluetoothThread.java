package cc.liyongzhi.bluetoothselector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by lee on 5/25/16.
 */
public class ConnectBluetoothThread extends Thread {

    private static ConnectBluetoothThread mConnectBluetoothThread;
    private final BluetoothSocket mSocket;
    private final BluetoothDevice mDevice;
    private BluetoothAdapter mBluetoothAdapter;
    private SocketConnectedCallback mSocketConnectedCallback;
    private Context mContext;

    private ConnectBluetoothThread(Context context, BluetoothDevice device, SocketConnectedCallback socketConnectedCallback) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothSocket tmp = null;
        mDevice = device;
        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        } catch (IOException e) {

        }
        mSocket = tmp;
        mContext = context;
        mSocketConnectedCallback = socketConnectedCallback;
    }

    private ConnectBluetoothThread(Context context, String address, SocketConnectedCallback socketConnectedCallback) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mDevice = mBluetoothAdapter.getRemoteDevice(address);
        BluetoothSocket tmp = null;
        try {
            tmp = mDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSocket = tmp;
        mContext = context;
        mSocketConnectedCallback = socketConnectedCallback;
    }

    public void run() {

        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(mContext,OpenBluetoothActivity.class);
            mContext.startActivity(intent);
            //Todo 连接成功后回调
        }

        mBluetoothAdapter.cancelDiscovery();

        try {
            if (!mSocket.isConnected()) {
                mSocket.connect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                mSocket.close();
            } catch (IOException e1) {
                mSocketConnectedCallback.internalDone(null, null, e1);
                MedBluetooth.removeMacFromMap(mDevice.getAddress());
                return;
            }
            mSocketConnectedCallback.internalDone(null, null, e);
            MedBluetooth.removeMacFromMap(mDevice.getAddress());
            return;
        }

        mSocketConnectedCallback.internalDone(mSocket, mDevice, null);
        MedBluetooth.removeMacFromMap(mDevice.getAddress());
    }

    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {

        }
    }

    public static void startUniqueConnectThread(Context context, String address, SocketConnectedCallback socketConnectedCallback) {
        ConnectBluetoothThread thread = MedBluetooth.getConnectThreadByMac(address);
        if (thread == null) {
            MedBluetooth.addConnectThreadToMap(address, new ConnectBluetoothThread(context, address, socketConnectedCallback));
            thread = MedBluetooth.getConnectThreadByMac(address);
        }

        if (!thread.isAlive()) {
            thread.start();
        } else {
            socketConnectedCallback.internalDone(null, null, new IOException("已经有在运行的实例"));
        }

    }
}
