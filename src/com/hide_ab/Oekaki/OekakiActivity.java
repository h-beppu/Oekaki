package com.hide_ab.Oekaki;

import android.app.Activity;
import android.os.Bundle;

public class OekakiActivity extends Activity {
	// アクティビティが生成されたときに呼び出されるメソッド
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(new PictView(this));
		setContentView(R.layout.main);
	}
}