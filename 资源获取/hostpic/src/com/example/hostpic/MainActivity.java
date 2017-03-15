package com.example.hostpic;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	ImageView imageViewInstall  = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button buttonInstall = (Button) findViewById(R.id.install);
		imageViewInstall = (ImageView) findViewById(R.id.imageView1);
		Button buttonInstallAsset = (Button) findViewById(R.id.install_asset);
		buttonInstallAsset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try {
					AssetManager assetManager = getInstalledContext().getResources().getAssets();
					InputStream ins = assetManager.open("six.jpg");
					Bitmap bitmap = BitmapFactory.decodeStream(ins);
					imageViewInstall.setImageBitmap(bitmap);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		buttonInstall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Resources installedResource = null;
				try {
					//得到已经安装的apk的resource对象
					installedResource = getInstalledContext().getResources();
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				imageViewInstall.setImageDrawable(installedResource.getDrawable(getResourceId(installedResource,"drawable","three")));
				String app_name = installedResource.getString(getResourceId(installedResource, "string","app_name"));
				String hello_world = installedResource.getString(getResourceId(installedResource, "string","hello_world"));
				Toast.makeText(MainActivity.this,"app_name is :"+app_name+"===hello_world is :"+hello_world,Toast.LENGTH_LONG).show();
			}
		});
		
		Button buttonUninstall = (Button) findViewById(R.id.uninstall);
		buttonUninstall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String apkPath = "/storage/sdcard0/183/plugpicinstall.apk";
				String dexPath = MainActivity.this.getDir("dex",Context.MODE_PRIVATE).getAbsolutePath();
				DexClassLoader classLoader = new DexClassLoader(apkPath, dexPath, null, getClassLoader());
				try {
					//反射得到R文件的内部类drawable
					Class<?> drawable_clazz = classLoader.loadClass("com.example.plugpicinstall.R$drawable");
					//得到drawable类的所有属性
					Field[]fields = drawable_clazz.getDeclaredFields();
					for (Field field : fields) {
						field.setAccessible(true);
						if (field.getName().equals("ten")) {
							int id = field.getInt(new R.id());
							imageViewInstall.setBackground(getUnInstalledResource().getDrawable(id));
						}
					}
					//反射得到R文件的内部类string
					Class<?>string_clazz = classLoader.loadClass("com.example.plugpicinstall.R$string");
					StringBuffer sb = new StringBuffer();
					//得到string内部类的所有属性，这些属性就是我们在string.xml文件中生命的字符串资源
					Field[]fields2 = string_clazz.getDeclaredFields();
					int id = 0;
					for (Field field : fields2) {
						//得到对应的字符串资源的id值
						id = field.getInt(new R.id());
						sb.append(getUnInstalledResource().getString(id));
					}
					Toast.makeText(MainActivity.this,sb.toString(),Toast.LENGTH_SHORT).show();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		});
		
		Button button_showAddResult = (Button) findViewById(R.id.uninstall_showAddResult);
		button_showAddResult.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String apkPath = "/storage/sdcard0/183/plugpicinstall.apk";
				String dexPath = MainActivity.this.getDir("dex",Context.MODE_PRIVATE).getAbsolutePath();
				DexClassLoader classLoader = new DexClassLoader(apkPath, dexPath, null, getClassLoader());
				try {
					Class<?> clazz = classLoader.loadClass("com.example.plugpicinstall.DynamicClass");
					Object obj = clazz.newInstance();
					Method initMeghod = clazz.getDeclaredMethod("init",Activity.class);
					initMeghod.invoke(obj,MainActivity.this);
					
					//利用反射运行showAddResult方法
					Class[]params = new Class[2];
					params[0] = Integer.TYPE;
					params[1] = Integer.TYPE;
					Method showAddMethod = clazz.getDeclaredMethod("showAddResult",params);
					showAddMethod.invoke(obj, 1,33);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		
		Button button_showHello = (Button) findViewById(R.id.uninstall_showHello);
		button_showHello.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//apk文件的存放路径
				String apkPath = "/storage/sdcard0/183/plugpicinstall.apk";
				//dex文件的路径
				String dexPath = MainActivity.this.getDir("dex",Context.MODE_PRIVATE).getAbsolutePath();
				DexClassLoader classLoader = new DexClassLoader(apkPath, dexPath, null, getClassLoader());
				try {
					//加载需要的类
					Class<?> clazz = classLoader.loadClass("com.example.plugpicinstall.DynamicClass");
					Object obj = clazz.newInstance();
					//利用反射调用init方法，将context对象赋值
					Method initMeghod = clazz.getDeclaredMethod("init",Activity.class);
					initMeghod.invoke(obj,MainActivity.this);
					//利用反射执行showHello方法，传入一个string参数
					Method helloMethod = clazz.getDeclaredMethod("showHello",String.class);
					helloMethod.invoke(obj,"李磊");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
		Button button_uninstall_asset = (Button) findViewById(R.id.unstall_asset);
		button_uninstall_asset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AssetManager assetManager = getUnInstalledResource().getAssets();
				InputStream ins;
				try {
					ins = assetManager.open("five.jpg");
					Bitmap bitmap = BitmapFactory.decodeStream(ins);
					imageViewInstall.setImageBitmap(bitmap);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
		
	}
	/**
	 * 该方法用来获取已经安装的apk对应的context对象
	 * @return
	 * @throws NameNotFoundException
	 */
	private Context getInstalledContext() throws NameNotFoundException {
		return createPackageContext("com.example.plugpicinstall",Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
	}
	/**
	 * 该方法用来获取已经安装的apk中对应的resource对象
	 * @param resources
	 * @param resType
	 * @param resName
	 * @return
	 */
	private int getResourceId(Resources resources,String resType,String resName) {
		return resources.getIdentifier(resName, resType,"com.example.plugpicinstall");
	}
	
	/**
	 * 该方法用来获取未安装的apk的reosurces对象
	 * @return
	 */
	private Resources getUnInstalledResource() {
		 // 反射出资源管理器  
        try {
			Class<?> assetManager_clazz = Class  
			        .forName("android.content.res.AssetManager");
			//生成assetManager对象
			Object assetObj = assetManager_clazz.newInstance();
			//因为addAssetPath是隐藏的，所以只能通过反射来获取
			Method addAssetMethod = assetManager_clazz.getDeclaredMethod("addAssetPath",String.class);
			addAssetMethod.invoke(assetObj,"/storage/sdcard0/183/plugpicinstall.apk");
			Resources resources = getResources();
			Constructor<?>resources_constructor = Resources.class.getConstructor(assetManager_clazz,resources.getDisplayMetrics().getClass(),resources.getConfiguration().getClass());
			resources = (Resources) resources_constructor.newInstance(assetObj,resources.getDisplayMetrics(),resources.getConfiguration());
			//返回/storage/sdcard0/183/plugpicinstall.apk的resources实例
			return resources;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}  
		return null;
	}
	


}
