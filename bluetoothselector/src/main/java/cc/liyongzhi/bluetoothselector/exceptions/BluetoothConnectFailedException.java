package cc.liyongzhi.bluetoothselector.exceptions;

import java.io.IOException;

/**
 * Created by lee on 5/25/16.
 */
public class BluetoothConnectFailedException extends IOException {

    public BluetoothConnectFailedException(String msg) {
        super(msg);
    }
}
