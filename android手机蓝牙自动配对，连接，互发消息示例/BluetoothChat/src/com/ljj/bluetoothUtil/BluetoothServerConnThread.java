package com.ljj.bluetoothUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * �����������߳�
 */
public class BluetoothServerConnThread extends Thread {
	private Handler serviceHandler; // ����ͬServiceͨ�ŵ�Handler
	private BluetoothAdapter adapter;
	private BluetoothSocket socket; // ����ͨ�ŵ�Socket
	private BluetoothServerSocket serverSocket;

	/**
	 * ���캯��
	 * 
	 * @param handler
	 */
	public BluetoothServerConnThread(Handler handler) {
		this.serviceHandler = handler;
		adapter = BluetoothAdapter.getDefaultAdapter();
	}

	@Override
	public void run() {
		try {
			//Thread.sleep(1000);//����1�룬��ֹ����

			serverSocket = adapter.listenUsingRfcommWithServiceRecord("Server", BluetoothTools.PRIVATE_UUID);
			socket = serverSocket.accept();

		} catch (IOException e) {
			// ��ӡ����ʧ����Ϣ
			Log.i("gomtel---", "BluetoothServerConnThread,===" + e);
			// ��������ʧ����Ϣ
			serviceHandler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
			e.printStackTrace();
			return;
		} finally {
			try {
				if (serverSocket != null)
					serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				// ��ӡ�ر�socketʧ����Ϣ
				Log.i("gomtel---", "close socket Exception,===" + e.getMessage());
			}
		}
		if (socket != null) {
			// �������ӳɹ���Ϣ����Ϣ��obj�ֶ�Ϊ���ӵ�socket
			Message msg = serviceHandler.obtainMessage();
			msg.what = BluetoothTools.MESSAGE_CONNECT_SUCCESS;
			msg.obj = socket;
			msg.sendToTarget();
		} else {
			// ��������ʧ����Ϣ
			serviceHandler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
			Log.i("gomtel---", "socket=null");
			return;
		}
	}
}
