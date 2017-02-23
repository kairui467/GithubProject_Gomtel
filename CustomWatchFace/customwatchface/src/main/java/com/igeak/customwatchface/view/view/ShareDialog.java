package com.igeak.customwatchface.view.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igeak.customwatchface.R;


/**
 * Created by star on 15-9-17.
 */
public class ShareDialog extends Dialog {

    LayoutInflater inflater;
    private Integer[] shareIcon = new Integer[]{R.drawable.weibo, R.drawable.weixin, R.drawable
            .weixin1, R.drawable.weibo, R.drawable.weixin, R.drawable
            .weixin1};
    private Integer[] shareString = new Integer[]{R.string.weibo, R.string.weixin, R.string
            .weixin_friend, R.string.weibo, R.string.weixin, R.string
            .weixin_friend};
    private Activity activity;
    private View shareView;

    public ShareDialog(Activity activity, View shareView) {
        super(activity, R.style.MyDialog);
        this.shareView = shareView;
        Context context = activity.getApplicationContext();
        this.activity = activity;
        inflater = LayoutInflater.from(context);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置

        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setWindowAnimations(R.style.MyDialogAnimation); // 添加动画
    }

    private ShareAdapter adapter;
    private IItemClickEvent iEvent;

    public interface IItemClickEvent {
        public void dismiss();

        public void choose(int position, String path);
    }

    public void setIEvent(IItemClickEvent iEvent) {
        this.iEvent = iEvent;
    }

    public void showShareDialog(String[] list) {


        LinearLayout root = (LinearLayout) inflater.inflate(R.layout.share_bottom_view, null);

        GridView gvShare = (GridView) root.findViewById(R.id.gvShare);
        adapter = new ShareAdapter(shareIcon, shareString);
        gvShare.setAdapter(adapter);
        gvShare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                adapter.setSelection(position);
                adapter.notifyDataSetChanged();

                dismiss();
            }
        });


        Button btnCancel = (Button) root.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        this.setContentView(root);
        this.show();
    }


    public void dismiss() {
        super.dismiss();
        if (iEvent != null) {
            iEvent.dismiss();
        }
    }


    private class ShareAdapter extends BaseAdapter {

        private String[] list;
        private Integer[] listString;
        private Integer[] listIcon;

        private int clickTmp = -1;

        public void setSelection(int position) {
            this.clickTmp = position;
        }

        public ShareAdapter(Integer[] listIcon, Integer[] listString) {
            this.listIcon = listIcon;
            this.listString = listString;
        }

        @Override
        public int getCount() {
            return listIcon.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.share_bottom_list_item, null);
                viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (position == clickTmp) {
                convertView.setBackgroundColor(activity.getResources().getColor(R.color
                        .share_item_click_color));
            } else {
                convertView.setBackgroundColor(activity.getResources().getColor(android.R.color
                        .transparent));
            }
            viewHolder.tvTitle.setText(getStringByPosition(position));
            viewHolder.ivIcon.setImageResource(getImageByPosition(position));
            return convertView;
        }

        public int getImageByPosition(int position) {
            return listIcon[position];
        }

        public int getStringByPosition(int position) {
            return listString[position];
        }

        private class ViewHolder {
            private ImageView ivIcon;
            private TextView tvTitle;
        }
    }


}
