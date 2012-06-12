package com.hide_ab.Oekaki;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingActivity extends Activity {
	// アクティビティが生成されたときに呼び出されるメソッド
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		Button closeBtn = (Button)findViewById(R.id.button_close);
		closeBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				finish();
			}});

		Button redBtn = (Button)findViewById(R.id.button_red);
		redBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				finish();
			}});
		Button blackBtn = (Button)findViewById(R.id.button_black);
		blackBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				finish();
			}});
	}
}
