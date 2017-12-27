package com.allen.androidcustomview.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.androidcustomview.R;
import com.allen.androidcustomview.widget.ProductProgressBar;

/**
 * Created by gyx on 2017/12/27.
 */
public class MyProgressBar extends RelativeLayout {

	private ProductProgressBar progressView;
	private TextView text1;
	private TextView text2;
	private TextView text3;
	public MyProgressBar(Context context) {
		super(context);
	}

	public MyProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		init();
	}

	private void init() {
		View inflate = LayoutInflater.from(getContext()).inflate(R.layout.view_my_progressbar, this);



		progressView = (ProductProgressBar) inflate.findViewById(R.id.progress_view);
		text1 = (TextView) inflate.findViewById(R.id.text1);
		text2 = (TextView) inflate.findViewById(R.id.text2);
		text3 = (TextView) inflate.findViewById(R.id.text3);
		inflate.invalidate();

	}
}
