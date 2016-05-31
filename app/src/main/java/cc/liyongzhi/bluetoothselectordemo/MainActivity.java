package cc.liyongzhi.bluetoothselectordemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import javax.microedition.khronos.opengles.GL;

import cc.liyongzhi.bluetoothselector.BluetoothConnectCallback;
import cc.liyongzhi.bluetoothselector.MedBluetooth;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private TextView mTextView;
    private Button mButton;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(GlobalData.ACTION_DATA_RECEIVED)) {
                refreshData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        IntentFilter filter = new IntentFilter(GlobalData.ACTION_DATA_RECEIVED);
        registerReceiver(mReceiver,filter);

        mButton = (Button) findViewById(R.id.btn_connect_device);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedBluetooth.connectBluetooth(mContext, new BluetoothConnectCallback() {
                    @Override
                    public void connected(BluetoothSocket socket, BluetoothDevice device, Exception e) {
                        if (e != null) {

                        } else{
                            GlobalData.bluetoothSocket = socket;
                            ReadDataThread.startReadDataThread(mContext);
                            mButton.setText("已连接");
                        }
                    }

                    @Override
                    public void disconnected() {
                        Toast.makeText(mContext, "Bluetooth Disconnected", Toast.LENGTH_SHORT).show();
                        mButton.setText("重新连接");
                    }
                });
            }
        });

        mTextView = (TextView) findViewById(R.id.tv_monitor);

    }

    public void refreshData() {
        mTextView.setText(GlobalData.data);
    }
}
