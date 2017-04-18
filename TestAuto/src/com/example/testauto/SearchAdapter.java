package com.example.testauto;

import java.util.*;
import android.content.Context;
import android.util.Log;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class SearchAdapter<T> extends BaseAdapter implements Filterable {
	private List<T> mObjects;

	private List<Set<String>> pinyinList;//֧�ֶ�����,����:{{z,c},{j},{z},{q,x}}�ļ���  

	private final Object mLock = new Object();

	private int mResource;

	private int mFieldId = 0;

	private Context mContext;

	private ArrayList<T> mOriginalValues;
	private ArrayFilter mFilter;

	private LayoutInflater mInflater;

	public static final int ALL = -1;//ȫ��  
	private int maxMatch = 10;//�����ʾ���ٸ�����ѡ��  
	//֧�ֶ�����  

	public SearchAdapter(Context context, int textViewResourceId, T[] objects, int maxMatch) {
		// TODO Auto-generated constructor stub  
		init(context, textViewResourceId, 0, Arrays.asList(objects));
		this.pinyinList = getHanziSpellList(objects);
		this.maxMatch = maxMatch;
	}

	public SearchAdapter(Context context, int textViewResourceId, List<T> objects, int maxMatch) {
		// TODO Auto-generated constructor stub  
		init(context, textViewResourceId, 0, objects);
		this.pinyinList = getHanziSpellList(objects);
		this.maxMatch = maxMatch;
	}

	private void init(Context context, int resource, int textViewResourceId, List<T> objects) {
		mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mResource = resource;
		mObjects = objects;
		mFieldId = textViewResourceId;
	}

	/** 
	 * ��ú���ƴ������ĸ�б� 
	 */
	private List<Set<String>> getHanziSpellList(T[] hanzi) {
		List<Set<String>> listSet = new ArrayList<Set<String>>();
		PinYin4j pinyin = new PinYin4j();
		for (int i = 0; i < hanzi.length; i++) {
			listSet.add(pinyin.getPinyin(hanzi[i].toString()));
		}
		return listSet;
	}

	/** 
	 * ��ú���ƴ������ĸ�б� 
	 */
	private List<Set<String>> getHanziSpellList(List<T> hanzi) {
		List<Set<String>> listSet = new ArrayList<Set<String>>();
		PinYin4j pinyin = new PinYin4j();
		for (int i = 0; i < hanzi.size(); i++) {
			listSet.add(pinyin.getPinyin(hanzi.get(i).toString()));
		}
		return listSet;
	}

	public int getCount() {
		return mObjects.size();
	}

	public T getItem(int position) {
		return mObjects.get(position);
	}

	public int getPosition(T item) {
		return mObjects.indexOf(item);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mResource);
	}

	private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
		View view;
		TextView text;

		if (convertView == null) {
			view = mInflater.inflate(resource, parent, false);
		} else {
			view = convertView;
		}

		try {
			if (mFieldId == 0) {
				text = (TextView) view;
			} else {
				text = (TextView) view.findViewById(mFieldId);
			}
		} catch (ClassCastException e) {
			Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
			throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", e);
		}

		text.setText(getItem(position).toString());

		return view;
	}

	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				synchronized (mLock) {
					mOriginalValues = new ArrayList<T>(mObjects);//  
				}
			}

			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
					//                  ArrayList<T> list = new ArrayList<T>();//��  
					ArrayList<T> list = new ArrayList<T>(mOriginalValues);//List<T>  
					results.values = list;
					results.count = list.size();
				}
			} else {
				String prefixString = prefix.toString().toLowerCase();

				final ArrayList<T> hanzi = mOriginalValues;//����String  
				final int count = hanzi.size();

				final Set<T> newValues = new HashSet<T>(count);//֧�ֶ�����,���ظ�  

				for (int i = 0; i < count; i++) {
					final T value = hanzi.get(i);//����String  
					final String valueText = value.toString().toLowerCase();//����String
					final Set<String> pinyinSet = pinyinList.get(i);//֧�ֶ�����,����:{z,c}
					Iterator iterator = pinyinSet.iterator();//֧�ֶ�����
					while (iterator.hasNext()) {//֧�ֶ�����  
						final String pinyin = iterator.next().toString().toLowerCase();//ȡ�����������һ����ĸ  

						if (pinyin.indexOf(prefixString) != -1) {//����ƥ��  
							newValues.add(value);
						} else if (valueText.indexOf(prefixString) != -1) {//����Ǻ�����ֱ������  
							newValues.add(value);
						}
					}
					if (maxMatch > 0) {//����������  
						if (newValues.size() > maxMatch - 1) {//��Ҫ̫��  
							break;
						}
					}

				}
				List<T> list = Set2List(newValues);//ת��List  
				results.values = list;
				results.count = list.size();
			}
			return results;
		}

		protected void publishResults(CharSequence constraint, FilterResults results) {

			mObjects = (List<T>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}

	//List Set �໥ת��  
	public <T extends Object> Set<T> List2Set(List<T> tList) {
		Set<T> tSet = new HashSet<T>(tList);
		//TODO ����ʵ�ֿ�����ת���ɲ�ͬ��Set�����ࡣ     
		return tSet;
	}

	public <T extends Object> List<T> Set2List(Set<T> oSet) {
		List<T> tList = new ArrayList<T>(oSet);
		// TODO ��Ҫ���õ���ʱ������д���죬������Ҫ����List�Ķ�Ӧ���ࡣ     
		return tList;
	}
}