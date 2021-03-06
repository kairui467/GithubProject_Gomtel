package com.xbc.utils.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xbc.contacts.R;
import com.xbc.utils.activity.ContactsSortAdapter.ViewHolder;
import com.xbc.utils.activity.SideBar.OnTouchingLetterChangedListener;

public class ContactsActivity extends Activity {

	private static final String TAG = "gomtel---";
	ListView mListView;
	EditText etSearch;
	ImageView ivClearText;

	private SideBar sideBar;
	private TextView dialog;

	private List<SortModel> mAllContactsList;
	private ContactsSortAdapter adapter;
	/** 汉字转换成拼音的类 */
	private CharacterParser characterParser;

	/** 根据拼音来排列ListView里面的数据类 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_contacts);
		init();
	}

	private void init() {
		initView();
		initListener();
		loadDate();
		//loadContacts();
	}

	private void initListener() {

		/**清除输入字符**/
		ivClearText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				etSearch.setText("");
			}
		});
		etSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

			@Override
			public void afterTextChanged(Editable e) {

				String content = etSearch.getText().toString();
				boolean isEquals = "".equals(content);
				ivClearText.setVisibility(isEquals ? View.INVISIBLE : View.VISIBLE);

				if (content.length() > 0) {
					ArrayList<SortModel> fileterList = (ArrayList<SortModel>) search(content);
					adapter.updateListView(fileterList);
					//mAdapter.updateData(mContacts);
				} else {
					adapter.updateListView(mAllContactsList);
				}
				mListView.setSelection(0);
			}
		});

		//设置右侧[A-Z]快速导航栏触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					mListView.setSelection(position);
				}
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
				ViewHolder viewHolder = (ViewHolder) view.getTag();
				viewHolder.cbChecked.performClick();
				adapter.toggleChecked(position);
			}
		});

	}

	private void initView() {
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		ivClearText = (ImageView) findViewById(R.id.ivClearText);
		etSearch = (EditText) findViewById(R.id.et_search);
		mListView = (ListView) findViewById(R.id.lv_contacts);

		/** 给ListView设置adapter **/
		characterParser = CharacterParser.getInstance();
		mAllContactsList = new ArrayList<SortModel>();
		pinyinComparator = new PinyinComparator();
		Collections.sort(mAllContactsList, pinyinComparator);// 根据a-z进行排序源数据
		adapter = new ContactsSortAdapter(this, mAllContactsList);
		mListView.setAdapter(adapter);
	}

	private void loadDate() {
		for (String s : DateUtil.Timezones_zh) {
			SortModel sortModel = new SortModel(s, null, s);
			//优先使用系统sortkey取,取不到再使用工具取
			String sortLetters = getSortLetterBySortKey(s);
			if (sortLetters == null) {
				sortLetters = getSortLetter(s);
			}
			sortModel.sortLetters = sortLetters;

			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
				sortModel.sortToken = parseSortKey(s);
			else
				sortModel.sortToken = parseSortKeyLollipop(s);

			Log.i("gomtel---", sortModel.toString());
			mAllContactsList.add(sortModel);
		}

		Collections.sort(mAllContactsList, pinyinComparator);
		adapter.updateListView(mAllContactsList);
	}

	private void loadContacts() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ContentResolver resolver = getApplicationContext().getContentResolver();
					Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, new String[] {
						Phone.DISPLAY_NAME,
						Phone.NUMBER,
						"sort_key" }, null, null, "sort_key COLLATE LOCALIZED ASC");
					if (phoneCursor == null || phoneCursor.getCount() == 0) {
						Toast.makeText(getApplicationContext(), "未获得读取联系人权限 或 未获得联系人数据", Toast.LENGTH_SHORT).show();
						return;
					}
					int PHONES_NUMBER_INDEX = phoneCursor.getColumnIndex(Phone.NUMBER);
					int PHONES_DISPLAY_NAME_INDEX = phoneCursor.getColumnIndex(Phone.DISPLAY_NAME);
					int SORT_KEY_INDEX = phoneCursor.getColumnIndex("sort_key");
					if (phoneCursor.getCount() > 0) {
						mAllContactsList = new ArrayList<SortModel>();
						while (phoneCursor.moveToNext()) {
							String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
							if (TextUtils.isEmpty(phoneNumber)) continue;
							String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
							String sortKey = phoneCursor.getString(SORT_KEY_INDEX);
							SortModel sortModel = new SortModel(contactName, phoneNumber, sortKey);
							//优先使用系统sortkey取,取不到再使用工具取
							String sortLetters = getSortLetterBySortKey(sortKey);
							if (sortLetters == null) {
								sortLetters = getSortLetter(contactName);
							}
							sortModel.sortLetters = sortLetters;

							if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
								sortModel.sortToken = parseSortKey(sortKey);
							else
								sortModel.sortToken = parseSortKeyLollipop(sortKey);

							mAllContactsList.add(sortModel);
						}
					}
					phoneCursor.close();
					runOnUiThread(new Runnable() {
						public void run() {
							Collections.sort(mAllContactsList, pinyinComparator);
							adapter.updateListView(mAllContactsList);
						}
					});
				} catch (Exception e) {
					Log.e("xbc", e.getLocalizedMessage());
				}
			}
		}).start();
	}

	/**
	 * 名字转拼音,取首字母
	 * @param name
	 * @return
	 */
	private String getSortLetter(String name) {
		String letter = "#";
		if (name == null) {
			return letter;
		}
		//汉字转换成拼音
		String pinyin = characterParser.getSelling(name);
		String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);

		// 正则表达式，判断首字母是否是英文字母
		if (sortString.matches("[A-Z]")) {
			letter = sortString.toUpperCase(Locale.CHINESE);
		}
		return letter;
	}

	/**
	 * 取sort_key的首字母
	 * @param sortKey
	 * @return
	 */
	private String getSortLetterBySortKey(String sortKey) {
		if (sortKey == null || "".equals(sortKey.trim())) {
			return null;
		}
		String letter = "#";
		//汉字转换成拼音
		String sortString = sortKey.trim().substring(0, 1).toUpperCase(Locale.CHINESE);
		// 正则表达式，判断首字母是否是英文字母
		if (sortString.matches("[A-Z]")) {
			letter = sortString.toUpperCase(Locale.CHINESE);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 5.0以上需要判断汉字
			if (sortString.matches("^[\u4E00-\u9FFF]+$"))// 正则表达式，判断是否为汉字
				letter = getSortLetter(sortString.toUpperCase(Locale.CHINESE));
		}
		return letter;
	}

	/**
	 * 模糊查询
	 * @param str
	 * @return
	 */
	private List<SortModel> search(String str) {
		List<SortModel> filterList = new ArrayList<SortModel>();// 过滤后的list
		//if (str.matches("^([0-9]|[/+])*$")) {// 正则表达式 匹配号码
		if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
			String simpleStr = str.replaceAll("\\-|\\s", "");
			for (SortModel contact : mAllContactsList) {
				if (contact.number != null && contact.name != null) {
					if (contact.simpleNumber.contains(simpleStr) || contact.name.contains(str)) {
						if (!filterList.contains(contact)) {
							filterList.add(contact);
						}
					}
				}
			}
		} else {
			for (SortModel contact : mAllContactsList) {
				if (/*contact.number != null &&*/ contact.name != null) {
					//姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
					boolean isNameContains = contact.name.toLowerCase(Locale.CHINESE)
							.contains(str.toLowerCase(Locale.CHINESE));

					boolean isSortKeyContains = contact.sortKey.toLowerCase(Locale.CHINESE).replace(" ", "")
							.contains(str.toLowerCase(Locale.CHINESE));

					boolean isSimpleSpellContains = contact.sortToken.simpleSpell.toLowerCase(Locale.CHINESE)
							.contains(str.toLowerCase(Locale.CHINESE));

					boolean isWholeSpellContains = contact.sortToken.wholeSpell.toLowerCase(Locale.CHINESE)
							.contains(str.toLowerCase(Locale.CHINESE));

					if (isNameContains || isSortKeyContains || isSimpleSpellContains || isWholeSpellContains) {
						if (!filterList.contains(contact)) {
							filterList.add(contact);
						}
					}
				}
			}
		}
		return filterList;
	}

	/** 中文字符串匹配 */
	String chReg = "[\\u4E00-\\u9FA5]+";

	//String chReg="[^\\u4E00-\\u9FA5]";//除中文外的字符匹配
	/**
	 * 解析sort_key,封装简拼,全拼
	 * @param sortKey
	 * @return
	 */
	public SortToken parseSortKey(String sortKey) {
		SortToken token = new SortToken();
		if (sortKey != null && sortKey.length() > 0) {
			//其中包含的中文字符
			String[] enStrs = sortKey.replace(" ", "").split(chReg);
			for (int i = 0, length = enStrs.length; i < length; i++) {
				if (enStrs[i].length() > 0) {
					//拼接简拼
					token.simpleSpell += enStrs[i].charAt(0);
					token.wholeSpell += enStrs[i];
				}
			}
		}
		return token;
	}

	/**
	 * 解析sort_key,封装简拼,全拼。
	 * Android 5.0 以上使用
	 * @param sortKey
	 * @return
	 */
	public SortToken parseSortKeyLollipop(String sortKey) {
		String regEx = "[/s`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

		SortToken token = new SortToken();
		if (sortKey != null && sortKey.length() > 0) {
			// 清除掉所有特殊符号
			Matcher matches = Pattern.compile(regEx).matcher(sortKey);
			String result = matches.replaceAll("").trim();
			String replace = result.replace(" ", "");// 删除空格

			boolean isChinese = replace.matches(chReg);
			// 分割条件：中文不分割，英文以大写和空格分割
			String regularExpression = isChinese ? "" : "(?=[A-Z])|\\s";

			String[] enStrs = sortKey.split(regularExpression);

			for (int i = 0, length = enStrs.length; i < length; i++)
				if (enStrs[i].length() > 0) {
					//拼接简拼
					token.simpleSpell += getSortLetter(String.valueOf(enStrs[i].charAt(0)));
					token.wholeSpell += characterParser.getSelling(enStrs[i]);
				}
		}
		return token;
	}

}