package cc.liyongzhi.bluetoothselector;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

public class ConnectBluetoothActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private Context mContext;
    private String mKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_bluetooth);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        BluetoothScreenManger.getScreenManger().pushActivity(this);
        mContext = this;

        initView();

        String mac = getIntent().getStringExtra("bluetooth_mac_address");
        mKey = getIntent().getStringExtra("callback_key");

        if (mac == null || mac.equals("")) {
            Intent intent = new Intent(this, ChooseBluetoothActivity.class);
            startActivity(intent);
            finish();
        }

        ConnectBluetoothThread.startUniqueConnectThread(this, mac, new SocketConnectedCallback() {
            @Override
            public void done(BluetoothSocket socket, BluetoothDevice device, IOException e) {
                if (e != null) {
                    Toast.makeText(mContext, "连接失败，请重新连接", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    BluetoothScreenManger.getScreenManger().popActivity((Activity) mContext);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "连接成功", Toast.LENGTH_SHORT).show();
                    MedBluetooth.executeBluetoothConnectCallback(socket, device, e, mKey);
                    BluetoothScreenManger.getScreenManger().popAllActivity();
                }
            }
        });
    }



    private void initView() {
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        if (mProgressBar != null) {
            mProgressBar.setIndeterminate(true);
        }
    }


}
