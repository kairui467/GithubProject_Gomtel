package com.igeak.customwatchface.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.igeak.customwatchface.R;
import com.igeak.customwatchface.presenter.IPresenter;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseActivity extends AppCompatActivity {

    Toolbar toolbar = null;
    TextView toolbarTitle = null;
    LayoutInflater inflater;
    FrameLayout flContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_base);
        inflater = LayoutInflater.from(getApplicationContext());
        flContainer = (FrameLayout) findViewById(R.id.flContainer);

    }

    /**
     * add view to contanier...
     *
     * @param layout
     */
    public void addViewToContainerById(int layout) {
        View view = inflater.inflate(layout, null);
        addViewToContainer(view);
    }

    /**
     * add view to container
     *
     * @param v
     */
    public void addViewToContainer(View v) {
        flContainer.addView(v);
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        View v = findViewById(R.id.my_toolbar);
        if (v != null) {
            toolbar = (Toolbar) v;
            setSupportActionBar(toolbar);
            toolbarTitle = (TextView) v.findViewById(R.id.toolbar_title);
            if (toolbarTitle != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.selector_btn_back);
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }


    }

    protected void setFun1(CharSequence fun1text, View.OnClickListener listener) {
        TextView v = (TextView) findViewById(R.id.toolbar_fun1);
        v.setOnClickListener(listener);
        v.setText(fun1text);
    }

    protected void setFun2(CharSequence fun1text, View.OnClickListener listener) {
        TextView v = (TextView) findViewById(R.id.toolbar_fun2);
        v.setOnClickListener(listener);
        v.setText(fun1text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            //finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission
                .WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
