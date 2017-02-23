package com.ljj.bluetoothchat;

import java.util.Date;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ljj.bluetoothUtil.BluetoothServerService;
import com.ljj.bluetoothUtil.BluetoothTools;
import com.ljj.bluetoothUtil.TransmitBean;

public class ServerActivity extends Activity {
	private TextView serverStateTextView;
	private EditText msgEditText;
	private EditText sendMsgEditText;
	private Button sendBtn;
	
	private static final int DICOVERY_DURATION = 300;

	@Override
	protected void onStart() {
		// ������̨service
		Intent startService = new Intent(ServerActivity.this,
				BluetoothServerService.class);
		startService(startService);

		// ע��BoradcasrReceiver
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothTools.ACTION_DATA_TO_GAME);
		intentFilter.addAction(BluetoothTools.ACTION_CONNECT_SUCCESS);
		intentFilter.addAction(BluetoothTools.ACTION_CONNECT_ERROR);
		registerReceiver(broadcastReceiver, intentFilter);
		super.onStart();

		// ����������
		bluetoothAdapter.enable(); // ������
		// �����������ֹ��ܣ�300�룩
		Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DICOVERY_DURATION);
		startActivityForResult(discoveryIntent, 0x10);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server);
		serverStateTextView = (TextView) findViewById(R.id.serverStateTxt);
		serverStateTextView.setText("�ȴ�����...");

		msgEditText = (EditText) findViewById(R.id.serverEditText);

		sendMsgEditText = (EditText) findViewById(R.id.serverSendEditText);

		sendBtn = (Button) findViewById(R.id.serverSendMsgBtn);
		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("".equals(sendMsgEditText.getText().toString().trim())) {
					Toast.makeText(ServerActivity.this, "���벻��Ϊ��",
							Toast.LENGTH_SHORT).show();
				} else {
					// ������Ϣ
					TransmitBean data = new TransmitBean();
					data.setMsg(sendMsgEditText.getText().toString());
					Intent sendDataIntent = new Intent(
							BluetoothTools.ACTION_DATA_TO_SERVICE);
					sendDataIntent.putExtra(BluetoothTools.DATA, data);
					sendBroadcast(sendDataIntent);
				}
			}
		});

		sendBtn.setEnabled(false);
	}

	@Override
	protected void onStop() {
		// �رպ�̨Service
		Intent startService = new Intent(BluetoothTools.ACTION_STOP_SERVICE);
		sendBroadcast(startService);
		unregisterReceiver(broadcastReceiver);
		super.onStop();
	}

	// ����������
	private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (DICOVERY_DURATION == resultCode)
			sendBroadcast(new Intent(BluetoothTools.ACTION_REQUEST_DISCOVERABLE_OK));
	}

	// �㲥������
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();

			if (BluetoothTools.ACTION_DATA_TO_GAME.equals(action)) {
				// ��������
				TransmitBean data = (TransmitBean) intent.getExtras().getSerializable(BluetoothTools.DATA);
				String msg = "from remote " + new Date().toLocaleString() + " :\r\n" + data.getMsg() + "\r\n";
				msgEditText.append(msg);

			} else if (BluetoothTools.ACTION_CONNECT_SUCCESS.equals(action)) {
				// ���ӳɹ�
				serverStateTextView.setText("���ӳɹ�");
				sendBtn.setEnabled(true);
			}else if(BluetoothTools.ACTION_CONNECT_ERROR.equals(action)){
				//����ʧ��
				serverStateTextView.setText("����ʧ��");
			}

		}
	};
}
