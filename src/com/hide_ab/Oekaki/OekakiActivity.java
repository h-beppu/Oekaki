package com.hide_ab.Oekaki;

import android.app.Activity;
import android.os.Bundle;

public class OekakiActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 画面構成を適用
		setContentView(R.layout.main);
//		setContentView(new PictView(this));
	}
}