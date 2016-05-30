package cc.liyongzhi.bluetoothselector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by lee on 5/30/16.
 */
public class BluetoothStateChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.i("BluetoothStateChange", "action = " + action);

        if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String mac = device.getAddress();
            Log.i("BluetoothStateChange", "mac = " + mac);
            MedBluetooth.executeBluetoothDisconnectedCallback(mac);
        }
    }
}
