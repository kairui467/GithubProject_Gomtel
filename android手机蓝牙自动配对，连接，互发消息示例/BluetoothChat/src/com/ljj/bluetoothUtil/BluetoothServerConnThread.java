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
 * 服务器连接线程
 */
public class BluetoothServerConnThread extends Thread {
	private Handler serviceHandler; // 用于同Service通信的Handler
	private BluetoothAdapter adapter;
	private BluetoothSocket socket; // 用于通信的Socket
	private BluetoothServerSocket serverSocket;

	/**
	 * 构造函数
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
			//Thread.sleep(1000);//休眠1秒，防止出错

			serverSocket = adapter.listenUsingRfcommWithServiceRecord("Server", BluetoothTools.PRIVATE_UUID);
			socket = serverSocket.accept();

		} catch (IOException e) {
			// 打印连接失败信息
			Log.i("gomtel---", "BluetoothServerConnThread,===" + e);
			// 发送连接失败消息
			serviceHandler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
			e.printStackTrace();
			return;
		} finally {
			try {
				if (serverSocket != null)
					serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				// 打印关闭socket失败信息
				Log.i("gomtel---", "close socket Exception,===" + e.getMessage());
			}
		}
		if (socket != null) {
			// 发送连接成功消息，消息的obj字段为连接的socket
			Message msg = serviceHandler.obtainMessage();
			msg.what = BluetoothTools.MESSAGE_CONNECT_SUCCESS;
			msg.obj = socket;
			msg.sendToTarget();
		} else {
			// 发送连接失败消息
			serviceHandler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
			Log.i("gomtel---", "socket=null");
			return;
		}
	}
}
