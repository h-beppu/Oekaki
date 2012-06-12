package com.hide_ab.Oekaki;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class OekakiActivity extends Activity implements SensorEventListener {
	//�Z���T�[�}�l�[�W���[
	private SensorManager mSensorManager;

	private float[] currentOrientationValues  = {0.0f, 0.0f, 0.0f};
	private float[] currentAccelerationValues = {0.0f, 0.0f, 0.0f};
	private static final float SHAKE = 13.0f;//22.0f;

	private PictView pview = null;
	// ���j���[�A�C�e��ID
	private static final int
		MENU_ITEM0 = 0,
		MENU_ITEM1 = 1,
		MENU_ITEM2 = 2;

	String[] str_modes = {"����", "�܂�", "������"};
	int[] modes = {PictView.MODE_LINE, PictView.MODE_CIRCLE, PictView.MODE_RECT};
	int result_mode;

	int now_color, now_size;

	// �A�N�e�B�r�e�B���������ꂽ�Ƃ��ɌĂяo����郁�\�b�h
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//�Z���T�[�T�[�r�X�擾
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	}

	@Override
	protected void onResume() {
		super.onResume();

		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if(sensors.size() > 0) {
			Sensor sensor = sensors.get(0);
			boolean mRegisteredSensor = mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		//�Z���T�[�}�l�[�W���̃��X�i�o�^�j��
    	mSensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	/**
	* �Z���T�[�C�x���g
	*/
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//			String str =
//			String.valueOf(event.values[0]) + ", " +
//			String.valueOf(event.values[1]) + ", " +
//			String.valueOf(event.values[2]);
//			TextView tv = (TextView)findViewById(R.id.text_sensor);
//			tv.setText(str);

/*
			if(Math.abs(event.values[0]) > 5) {
				Intent intent = new Intent(OekakiActivity.this, SettingActivity.class);
				intent.putExtra("Color", now_color);
				intent.putExtra("Size",  now_size);
				startActivityForResult(intent, 0);
			}
*/
			// �X��(�n�C�J�b�g)
			currentOrientationValues[0] = event.values[0] * 0.1f + currentOrientationValues[0] * (1.0f - 0.1f);
			currentOrientationValues[1] = event.values[1] * 0.1f + currentOrientationValues[1] * (1.0f - 0.1f);
			currentOrientationValues[2] = event.values[2] * 0.1f + currentOrientationValues[2] * (1.0f - 0.1f);
			// �����x(���[�J�b�g)
			currentAccelerationValues[0] = event.values[0] - currentOrientationValues[0];
			currentAccelerationValues[1] = event.values[1] - currentOrientationValues[1];
			currentAccelerationValues[2] = event.values[2] - currentOrientationValues[2];
			// �U���Ă�H ��Βl(���邢��2��̕�����)�̍��v�������ȏォ�H
			// ������
			float targetValue = 
				Math.abs(currentAccelerationValues[0]) + 
				Math.abs(currentAccelerationValues[1]) +
				Math.abs(currentAccelerationValues[2]);
			if(targetValue > SHAKE) {
				Intent intent = new Intent(OekakiActivity.this, SettingActivity.class);
				intent.putExtra("Color", now_color);
				intent.putExtra("Size",  now_size);
				startActivityForResult(intent, 0);
			}

			String str = String.valueOf(targetValue);
			TextView tv = (TextView)findViewById(R.id.text_sensor);
			tv.setText(str);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem item0 = menu.add(Menu.NONE, MENU_ITEM0, Menu.NONE, "����");
		item0.setIcon(android.R.drawable.ic_menu_delete);
		MenuItem item1 = menu.add(Menu.NONE, MENU_ITEM1, Menu.NONE, "������");
		item1.setIcon(android.R.drawable.ic_menu_edit);
		MenuItem item2 = menu.add(Menu.NONE, MENU_ITEM2, Menu.NONE, "���ǂ�");
		item2.setIcon(android.R.drawable.ic_menu_revert);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(this.pview == null) {
			this.pview = (PictView) findViewById(R.id.vw_canvas);
		}

		switch(item.getItemId()) {
			case MENU_ITEM0:
				this.pview.Clear();
				break;
			case MENU_ITEM1:
				// Single Choice Dialog
				new AlertDialog.Builder(this)
					.setIcon(R.drawable.icon)
					.setTitle("�������������ł�")
					.setSingleChoiceItems(str_modes, result_mode,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								result_mode = which;
							}
						})
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						/* OK�{�^�����N���b�N�������̏��� */
						public void onClick(DialogInterface dialog, int whichButton) {
							int mode = modes[result_mode];
							OekakiActivity.this.pview.SetMode(mode);
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						/* Cancel �{�^�����N���b�N�������̏��� */
						public void onClick(DialogInterface dialog, int whichButton) {
//							new AlertDialog.Builder(OekakiActivity.this)
//								.setTitle("Canceled")
//								.show();
						}
					})
					.show();
				break;
			case MENU_ITEM2:
				this.pview.Undo();
				break;
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//		if(requestCode == REQUEST_CODE) {
//			Log.d(TAG, "requestCode = " + requestCode);
//		}

//		if(resultCode == RESULT_OK) {
//			Log.d(TAG, "resultCode = " + resultCode);
//		}

		if(intent != null) {
			if(this.pview == null) {
				this.pview = (PictView)findViewById(R.id.vw_canvas);
			}

			now_color = intent.getIntExtra("Color", now_color);
			now_size  = intent.getIntExtra("Size",  now_size);
			this.pview.SetColor(now_color);
			this.pview.SetSize(now_size);
		}
	}
}