package com.example.plugpicinstall;

import android.app.Activity;
import android.widget.Toast;

public class DynamicClass {
	
	private Activity mActivity = null;
	
	public void init(Activity activity) {
		this.mActivity = activity;
	}
	
	public void showHello(String name) {
		Toast.makeText(mActivity,"your name is :"+name, Toast.LENGTH_SHORT).show();
	}
	
	public void showAddResult(int a,int b) {
		Toast.makeText(mActivity,"the result is :"+(a+b),Toast.LENGTH_SHORT).show();
	}
}
