package com.allen.androidcustomview.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.androidcustomview.R;
import com.allen.androidcustomview.widget.CircleProgressBarView;
import com.allen.androidcustomview.widget.HorizontalProgressBar;
import com.allen.androidcustomview.widget.LoadingView;
import com.allen.androidcustomview.widget.ProductProgressBar;

public class ProgressBarActivity extends AppCompatActivity {

    ProductProgressBar productProgressBar;
    TextView textView;
    Button button;

    private TextView text1;
    private TextView text2;
    private TextView text3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregress_bar);
        productProgressBar = (ProductProgressBar) findViewById(R.id.product_progress_view);
        button = (Button) findViewById(R.id.startAnimationBtn);



        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int w_screen = displayMetrics.widthPixels;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(getInt(7.0)*w_screen/100,0,0,0);

        text1.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams layoutParams_2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams_2.setMargins(getInt(15.0)*w_screen/100,0,0,0);
        text2.setLayoutParams(layoutParams_2);


        productProgressBar.setProgress(getInt(26.0), 0).setProgressListener(new ProductProgressBar.ProgressListener() {
            @Override
            public void currentProgressListener(float currentProgress) {
                Log.e("allen", "currentProgressListener: " + currentProgress);
                if (currentProgress >= getInt(7.0)) {
                    text1.setTextColor(Color.RED);
                }
                if (currentProgress >= getInt(15.0)) {
                    text2.setTextColor(Color.RED);
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productProgressBar.setProgress(50, 100);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private int getInt(Double n) {
        double v = (n / 30.0) * 100;
        double rint = Math.rint(v);

        return (int)rint;
    }



}
