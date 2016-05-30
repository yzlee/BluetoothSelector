package cc.liyongzhi.bluetoothselector;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class OpenBluetoothActivity extends AppCompatActivity {

    private final int REQUEST_ENABLE_BT = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_bluetooth);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        BluetoothScreenManger.getScreenManger().pushActivity(this);

        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this,"打开蓝牙成功",Toast.LENGTH_SHORT).show();
                BluetoothScreenManger.getScreenManger().popActivity(this);
            } else {
                Toast.makeText(this,"用户取消打开蓝牙",Toast.LENGTH_SHORT).show();
                BluetoothScreenManger.getScreenManger().popActivity(this);
            }
        }

    }
}
