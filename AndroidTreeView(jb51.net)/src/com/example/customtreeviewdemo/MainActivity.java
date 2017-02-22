package com.example.customtreeviewdemo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.customtreeviewdemo.bean.MyNodeBean;
import com.example.customtreeviewdemo.tree.Node;
import com.example.customtreeviewdemo.tree.TreeListViewAdapter.OnTreeNodeClickListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView treeLv;
	private Button checkSwitchBtn;
	private TextView mTv;

	private MyTreeListViewAdapter<MyNodeBean> adapter;
	private List<MyNodeBean> mDatas = new ArrayList<MyNodeBean>();
	//标记是显示Checkbox还是隐藏
	private boolean isHide = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(//屏幕长亮
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, //
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);

		initDatas();
		treeLv = (ListView) this.findViewById(R.id.tree_lv);
		checkSwitchBtn = (Button) this.findViewById(R.id.check_switch_btn);
		mTv = (TextView) findViewById(R.id.tv);

		checkSwitchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*isHide = !isHide;
				adapter.updateView(isHide);*/
				List<Node> allNodes = adapter.getAllNodes();

				StringBuffer sb = new StringBuffer();
				for (Iterator iterator = allNodes.iterator(); iterator.hasNext();) {
					Node node = (Node) iterator.next();
					sb.append(node.toString()).append("\n");
				}
				mTv.setText(sb);
			}

		});
		try {
			adapter = new MyTreeListViewAdapter<MyNodeBean>(treeLv, this, mDatas, 10, isHide);

			adapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
				@Override
				public void onClick(Node node, int position) {
					if (node.isLeaf()) {
						//Toast.makeText(getApplicationContext(), node.getName(), Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onCheckChange(Node node, int position, List<Node> checkedNodes) {
					StringBuffer sb = new StringBuffer();
					for (Node n : checkedNodes) {
						int pos = n.getId() - 1;
						sb.append(pos + 1).append("---").append(mDatas.get(pos).getName()).append(";\n");
					}

					//Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
				}
			});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		treeLv.setAdapter(adapter);
	}

	private void initDatas() {
		/*mDatas.add(new MyNodeBean(1, 0, "中国古代"));
		mDatas.add(new MyNodeBean(2, 1, "唐朝"));
		mDatas.add(new MyNodeBean(3, 1, "宋朝"));
		mDatas.add(new MyNodeBean(4, 1, "明朝"));
		mDatas.add(new MyNodeBean(5, 2, "李世民"));
		mDatas.add(new MyNodeBean(6, 2, "李白"));
		
		mDatas.add(new MyNodeBean(7, 3, "赵匡胤"));
		mDatas.add(new MyNodeBean(8, 3, "苏轼"));
		
		mDatas.add(new MyNodeBean(9, 4, "朱元璋"));
		mDatas.add(new MyNodeBean(10, 4, "唐伯虎"));
		mDatas.add(new MyNodeBean(11, 4, "文征明"));
		mDatas.add(new MyNodeBean(12, 7, "赵建立"));
		mDatas.add(new MyNodeBean(13, 8, "苏东东"));
		mDatas.add(new MyNodeBean(14, 10, "秋香"));*/

		mDatas.add(new MyNodeBean(1, 0, "关闭省电模式"));
		mDatas.add(new MyNodeBean(2, 0, "开启系统省电模式"));
		mDatas.add(new MyNodeBean(3, 0, "关闭硬件和调整色彩亮度"));
		mDatas.add(new MyNodeBean(4, 3, "开启黑白模式"));
		mDatas.add(new MyNodeBean(5, 3, "屏幕亮度降至最低"));
		mDatas.add(new MyNodeBean(6, 3, "关闭WIFI"));
		mDatas.add(new MyNodeBean(7, 3, "关闭GPS"));
		mDatas.add(new MyNodeBean(8, 3, "关闭蓝牙"));
		mDatas.add(new MyNodeBean(9, 3, "关闭移动网络"));
		mDatas.add(new MyNodeBean(10, 0, "进入省电表盘"));
	}

	private String[] checkName = { //
		"关闭省电模式", //
		"开启系统省电模式", //
		"关闭硬件和调整色彩亮度", //
		"开启黑白模式", //
		"屏幕亮度降至最低", //
		"关闭WIFI", //
		"关闭GPS", //
		"关闭蓝牙", //
		"关闭移动网络", //
		"进入省电表盘" };
}
