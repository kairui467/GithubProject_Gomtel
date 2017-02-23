package com.ljj.bluetoothUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class BluetoothClientService extends Service {

	// ��������Զ���豸����
	private List<BluetoothDevice> discoveredDevices = new ArrayList<BluetoothDevice>();

	// ����������
	private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	// ����ͨѶ�߳�
	private BluetoothCommunThread communThread;

	private boolean TempB = false;// �ж��Ƿ�������ȡ��������

	private BluetoothClientConnThread bluetoothClientConnThread;
	// ������Ϣ�㲥�Ľ�����
	private BroadcastReceiver controlReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothTools.ACTION_SELECTED_DEVICE.equals(action)) {
				// ѡ�������ӵķ������豸
				BluetoothDevice device = (BluetoothDevice) intent.getExtras().get(BluetoothTools.DEVICE);
				// �����豸�����߳�
				bluetoothClientConnThread = new BluetoothClientConnThread(handler, device, getApplicationContext());
				bluetoothClientConnThread.start();
			} else if (BluetoothTools.ACTION_STOP_SERVICE.equals(action)) {
				// ֹͣ��̨����
				if (communThread != null) {
					communThread.isRun = false;
				}
				stopSelf();
			} else if (BluetoothTools.ACTION_DATA_TO_SERVICE.equals(action)) {
				// ��ȡ����
				Object data = intent.getSerializableExtra(BluetoothTools.DATA);
				if (communThread != null) {
					communThread.writeObject(data);
				}
			}
		}
	};

	// ���������㲥�Ľ�����
	private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// ��ȡ�㲥��Action
			String action = intent.getAction();

			if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
				if (bluetoothAdapter.isEnabled()) {
					boolean startDiscovery = bluetoothAdapter.startDiscovery(); // ��ʼ����
					Log.i("gomtel---", "startDiscovery:" + startDiscovery);
					Toast.makeText(getApplicationContext(), "��ʼ����", Toast.LENGTH_LONG).show();
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				Log.i("gomtel---", "��ʼ����");
				// ��ʼ����
			} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// ����Զ�������豸
				// ��ȡ�豸
				BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				String address = bluetoothDevice.getAddress();
				String name = bluetoothDevice.getName();
				Log.i("gomtel---", "address:" + address + "�� name:" + name);

				//�������������ַƥ����ֻ��󣬷��͹㲥����ע���˸ù㲥��Receiver�������Ӳ���
				if (address.equals(BluetoothTools.BluetoothAddress) || address.equals(BluetoothTools.BluetoothAddress2)) {
					TempB = true;
					bluetoothAdapter.cancelDiscovery();// ȡ������
					// ���㲥���ͳ�ȥ
					Intent selectDeviceIntent = new Intent(BluetoothTools.ACTION_SELECTED_DEVICE);
					selectDeviceIntent.putExtra(BluetoothTools.DEVICE, bluetoothDevice);
					sendBroadcast(selectDeviceIntent);
				}

			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				// ���������������������ȡ�����������ͷ��͹㲥
				if (!TempB) {
					// ��δ�ҵ��豸���򷢶�δ�����豸�㲥
					Intent foundIntent = new Intent(BluetoothTools.ACTION_NOT_FOUND_SERVER);
					sendBroadcast(foundIntent);
				}
			} else if (BluetoothTools.ACTION_NOT_FOUND_SERVER.equals(action)) {
				// ������ɣ�δ�����豸��������������
				bluetoothAdapter.cancelDiscovery();// ȡ������
				handler.postDelayed(new Runnable() {
					public void run() {
						bluetoothAdapter.startDiscovery(); // ��ʼ����
					}
				}, 5000);// ����,��ֹ�Ͷ��ֻ�����
			}
		}
	};

	// ���������߳���Ϣ��Handler
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// ������Ϣ
			switch (msg.what) {
				case BluetoothTools.MESSAGE_CONNECT_ERROR:
					// ���Ӵ���
					// �������Ӵ���㲥
					Intent errorIntent = new Intent(BluetoothTools.ACTION_CONNECT_ERROR);
					sendBroadcast(errorIntent);
					break;
				case BluetoothTools.MESSAGE_CONNECT_SUCCESS:
					// ���ӳɹ�

					// ����ͨѶ�߳�
					communThread = new BluetoothCommunThread(handler, (BluetoothSocket) msg.obj);
					communThread.start();

					// �������ӳɹ��㲥
					Intent succIntent = new Intent(BluetoothTools.ACTION_CONNECT_SUCCESS);
					sendBroadcast(succIntent);
					break;
				case BluetoothTools.MESSAGE_READ_OBJECT:
					// ��ȡ������
					// �������ݹ㲥���������ݶ���
					Intent dataIntent = new Intent(BluetoothTools.ACTION_DATA_TO_GAME);
					dataIntent.putExtra(BluetoothTools.DATA, (Serializable) msg.obj);
					sendBroadcast(dataIntent);
					break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * ��ȡͨѶ�߳�
	 * 
	 * @return
	 */
	public BluetoothCommunThread getBluetoothCommunThread() {
		return communThread;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * Service����ʱ�Ļص�����
	 */
	@Override
	public void onCreate() {
		// discoveryReceiver��IntentFilter
		IntentFilter discoveryFilter = new IntentFilter();
		discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		discoveryFilter.addAction(BluetoothDevice.ACTION_FOUND);
		discoveryFilter.addAction(BluetoothTools.ACTION_NOT_FOUND_SERVER);
		discoveryFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

		// controlReceiver��IntentFilter
		IntentFilter controlFilter = new IntentFilter();
		controlFilter.addAction(BluetoothTools.ACTION_SELECTED_DEVICE);
		controlFilter.addAction(BluetoothTools.ACTION_STOP_SERVICE);
		controlFilter.addAction(BluetoothTools.ACTION_DATA_TO_SERVICE);

		// ע��BroadcastReceiver
		registerReceiver(discoveryReceiver, discoveryFilter);
		registerReceiver(controlReceiver, controlFilter);

		discoveredDevices.clear(); // ��մ���豸�ļ���
		bluetoothAdapter.enable(); // �������㲥��ACTION_STATE_CHANGED
		super.onCreate();
	}

	/**
	 * Service����ʱ�Ļص�����
	 */
	@Override
	public void onDestroy() {
		if (communThread != null)
			communThread.isRun = false;

		// �����
		unregisterReceiver(discoveryReceiver);
		
		//bluetoothClientConnThread.cancel();
		bluetoothAdapter.disable();// �ر�����
		super.onDestroy();
	}
}
