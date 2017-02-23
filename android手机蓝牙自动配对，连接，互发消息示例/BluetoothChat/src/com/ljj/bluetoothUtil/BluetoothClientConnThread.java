package com.ljj.bluetoothUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 蓝牙客户端连接线程
 */
public class BluetoothClientConnThread extends Thread {
	private Handler serviceHandler; // 用于向客户端Service回传消息的handler
	private BluetoothDevice serverDevice; // 服务器设备
	private BluetoothSocket socket; // 通信Socket
	private Context mContext;

	/**
	 * 构造函数
	 */
	public BluetoothClientConnThread(Handler handler, BluetoothDevice serverDevice, Context context) {
		this.serviceHandler = handler;
		this.serverDevice = serverDevice;
		mContext = context;
	}

	@Override
	public void run() {
		BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
		defaultAdapter.cancelDiscovery();
		try {
			//建立连接
			//socket = (BluetoothSocket) defaultAdapter.getClass().getMethod("createRfcommSocket", new Class[] { int.class }).invoke(defaultAdapter, 1);
			socket = serverDevice.createRfcommSocketToServiceRecord(BluetoothTools.PRIVATE_UUID);
			defaultAdapter.cancelDiscovery();
			Log.i("gomtel---", "打开连接 ，线程id： " + Thread.currentThread().getId());
			socket.connect();
		} catch (Exception ex) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.i("gomtel---", "" + ex.getMessage());
			// 发送连接失败消息
			serviceHandler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
			return;
		}

		// 发送连接成功消息，消息的obj参数为连接的socket
		Message msg = serviceHandler.obtainMessage();
		msg.what = BluetoothTools.MESSAGE_CONNECT_SUCCESS;
		msg.obj = socket;
		msg.sendToTarget();
		//connectionA2DP(defaultAdapter, serverDevice);
		connectionHeadset(defaultAdapter, serverDevice);
	}

	private void connectionA2DP(BluetoothAdapter adapter, final BluetoothDevice serverDevice) {
		adapter.getProfileProxy(mContext, new BluetoothProfile.ServiceListener() {
			@Override
			public void onServiceConnected(int profile, BluetoothProfile proxy) {
				try {
					Log.i("gomtel---", "a2dp onServiceConnected");
					Method connect = BluetoothA2dp.class.getDeclaredMethod("connect", BluetoothDevice.class);
					connect.invoke(proxy, serverDevice);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (Exception e) {
					Log.i("gomtel---", "" + e);
				}
			}

			@Override
			public void onServiceDisconnected(int profile) {
				Log.i("gomtel---", "a2dp onServiceDisconnected");
			}
		}, BluetoothProfile.A2DP);
	}

	private void connectionHeadset(BluetoothAdapter adapter, final BluetoothDevice serverDevice) {
		adapter.getProfileProxy(mContext, new BluetoothProfile.ServiceListener() {
			@Override
			public void onServiceConnected(int profile, BluetoothProfile proxy) {
				try {
					Log.i("gomtel---", "hfp onServiceConnected");
					Method connect = BluetoothHeadset.class.getDeclaredMethod("connect", BluetoothDevice.class);
					connect.invoke(proxy, serverDevice);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (Exception e) {
					Log.i("gomtel---", "" + e);
				}
			}

			@Override
			public void onServiceDisconnected(int profile) {
				Log.i("gomtel---", "hfp onServiceDisconnected");
			}
		}, BluetoothProfile.HEADSET);
	}

	public void cancel() {
		try {
			//device.createInsecureRfcommSocketToServiceRecord(uuid);
			socket.close();
			Log.i("gomtel---", "关闭连接 ，线程id： " + Thread.currentThread().getId());
		} catch (IOException e) {
			Log.i("gomtel---", "关闭异常:" + e);
		}
	}

}
