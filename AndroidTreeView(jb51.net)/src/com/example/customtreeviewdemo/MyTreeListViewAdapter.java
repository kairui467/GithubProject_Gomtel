package com.example.customtreeviewdemo;

import java.util.HashMap;
import java.util.List;

import com.example.customtreeviewdemo.tree.Node;
import com.example.customtreeviewdemo.tree.TreeListViewAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyTreeListViewAdapter<T> extends TreeListViewAdapter<T> {
	HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();

	public MyTreeListViewAdapter(ListView mTree, Context context, List<T> datas, int defaultExpandLevel, boolean isHide) throws IllegalArgumentException, IllegalAccessException {
		super(mTree, context, datas, defaultExpandLevel, isHide);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView.findViewById(R.id.id_treenode_icon);
			viewHolder.label = (TextView) convertView.findViewById(R.id.id_treenode_name);
			viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.id_treeNode_check);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (node.getIcon() == -1) {
			viewHolder.icon.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.icon.setVisibility(View.VISIBLE);
			viewHolder.icon.setImageResource(node.getIcon());
		}

		if (node.isHideChecked()) {
			viewHolder.checkBox.setVisibility(View.GONE);
		} else {
			viewHolder.checkBox.setVisibility(View.VISIBLE);
			setCheckBoxBg(viewHolder.checkBox, node.isChecked());
		}
		viewHolder.label.setText(node.getName());

		//viewHolder.checkBox.setChecked(state.get(node.getId()));
		/*final int pos = position;
		viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					state.put(pos, isChecked);
				} else {
					state.remove(pos);
				}
			}
		});
		viewHolder.checkBox.setChecked(state.get(position) == null ? false : true);*/

		return convertView;
	}

	private final class ViewHolder {
		ImageView icon;
		TextView label;
		CheckBox checkBox;
	}

	/**
	 * checkbox是否显示
	 * @param cb
	 * @param isChecked
	 */
	private void setCheckBoxBg(CheckBox cb, boolean isChecked) {
		if (isChecked) {
			cb.setBackgroundResource(R.drawable.check_box_bg_check);
		} else {
			cb.setBackgroundResource(R.drawable.check_box_bg);
		}
		//cb.setChecked(isChecked);
	}
}
