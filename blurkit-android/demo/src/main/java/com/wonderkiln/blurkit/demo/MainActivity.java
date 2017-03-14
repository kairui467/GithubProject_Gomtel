package com.wonderkiln.blurkit.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wonderkiln.blurkit.BlurLayout;

public class MainActivity extends AppCompatActivity {

    private BlurLayout blurLayout;
    private float movement = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blurLayout = (BlurLayout) findViewById(R.id.blurLayout);

        blurLayout.animate().translationY(movement).setDuration(1500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                movement = movement > 0 ? -50 : 50;
                blurLayout.animate().translationY(movement).setDuration(1500).setListener(this).start();
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        blurLayout.startBlur();
        blurLayout.lockView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        blurLayout.pauseBlur();
    }
}
