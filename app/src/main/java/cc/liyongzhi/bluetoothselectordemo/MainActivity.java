package cc.liyongzhi.bluetoothselectordemo;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cc.liyongzhi.bluetoothselector.BluetoothConnectWithDataManageCallback;
import cc.liyongzhi.bluetoothselector.MedBluetooth;
import cc.liyongzhi.dataprocessingcenter.DataProcessingCenter;
import cc.liyongzhi.dataprocessingcenter.DataProcessingSetting;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private TextView mTextView;
    private TextView mTextView2;
    private Button mButton;
    private Button mButton2;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Med.ACTION_DATA_RECEIVED)) {
                refreshData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        IntentFilter filter = new IntentFilter(Med.ACTION_DATA_RECEIVED);
        registerReceiver(mReceiver,filter);

        DataProcessingSetting setting = new DataProcessingSetting(this, new WarningCenter(), "123", "刘凯");
        final DataProcessingCenter dataProcessingCenter = DataProcessingCenter.getInstance(setting);
        dataProcessingCenter.startSaving();

        mButton = (Button) findViewById(R.id.btn_connect_device);
        mButton2 = (Button) findViewById(R.id.btn_connect_device2);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedBluetooth.connectBluetooth(mContext, new BluetoothConnectWithDataManageCallback() {
                    @Override
                    public void dataMange(int bytes, byte[] buffer, Exception e) {
                        if (e != null) {

                        } else {
                            DataParser dataParser = new DataParser();
                            for (int i = 0; i < bytes; i++) {
                                dataProcessingCenter.putOriginData(buffer[i]);
                                Med.data = Med.data + buffer[i] + " ";
                                if (Med.data.length() > 100) {
                                    Med.data = "";
                                }
                            }
                            Intent intent = new Intent(Med.ACTION_DATA_RECEIVED);
                            mContext.sendBroadcast(intent);
                        }
                    }

                    @Override
                    public void connected(BluetoothSocket socket, BluetoothDevice device, Exception e) {
                        if (e != null) {

                        } else{
                            Med.bluetoothSocket = socket;
                            BluetoothUtils.SendStartToBluetoothInBackground();
                            BluetoothUtils.SendPatientIdToBluetoothInBackground(true, new BooleanCallBack() {
                                @Override
                                public void onCallBack(Boolean b) {

                                }
                            });
                            mButton.setText("已连接1");
                        }
                    }

                    @Override
                    public void disconnected() {
                        Toast.makeText(mContext, "Bluetooth Disconnected", Toast.LENGTH_SHORT).show();
                        mButton.setText("重新连接1");
                    }
                });
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedBluetooth.connectBluetooth(mContext, new BluetoothConnectWithDataManageCallback() {
                    @Override
                    public void dataMange(int bytes, byte[] buffer, Exception e) {

                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            for (int i = 0; i < bytes; i++) {
                                Med.data2 = Med.data2 + buffer[i] + " ";
                                if (Med.data2.length() > 300) {
                                    Med.data2 = "";
                                }
                            }
                            Intent intent = new Intent(Med.ACTION_DATA_RECEIVED);
                            mContext.sendBroadcast(intent);
                            Log.i("liyongzhi", "after sendBroadcast and Med.data = " + Med.data2);
                        }
                    }

                    @Override
                    public void connected(BluetoothSocket socket, BluetoothDevice device, Exception e) {
                        if (e != null) {

                        } else{
                            Med.bluetoothSocket2 = socket;
                            mButton2.setText("已连接2");
                        }
                    }

                    @Override
                    public void disconnected() {
                        Toast.makeText(mContext, "Bluetooth2 Disconnected", Toast.LENGTH_SHORT).show();
                        mButton2.setText("重新连接2");
                    }
                });
            }
        });

        mTextView = (TextView) findViewById(R.id.tv_monitor);
        mTextView2 = (TextView) findViewById(R.id.tv_monitor2);
    }

    public void refreshData() {
        mTextView.setText(Med.data);
        mTextView2.setText(Med.data2);
    }
}
