package com.hide_ab.Oekaki;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingActivity extends Activity implements SensorEventListener {
	//センサーマネージャー
	private SensorManager mSensorManager;

	private float[] currentOrientationValues  = {0.0f, 0.0f, 0.0f};
	private float[] currentAccelerationValues = {0.0f, 0.0f, 0.0f};
	private static final float SHAKE = 13.0f;//22.0f;

	private int color;
	private int size;
	private Paint paint = null;			// 描画用
	private Bitmap bmp = null;
	private Canvas bmpCanvas;
	private final int BRUSH_SIZE = 4;

	// アクティビティが生成されたときに呼び出されるメソッド
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		try {
			// 呼び出し元からパラメータ取得
			Intent intent = getIntent();
			color = intent.getIntExtra("Color", Color.BLACK);
			size  = intent.getIntExtra("Size", BRUSH_SIZE);
		} catch (Exception e) {
			color = Color.BLACK;
			size  = BRUSH_SIZE;
		}

		// 描画用の準備
		paint = new Paint();
		// アンチエイリアスを有効にする
		paint.setAntiAlias(true);
		// 黒色、透明度100
		paint.setColor(color);
		// 線の太さ
		paint.setStrokeWidth(size);
		// 線のみ(塗りつぶさない)
		paint.setStyle(Paint.Style.STROKE);
		// 線の両端を丸くする
		paint.setStrokeCap(Paint.Cap.ROUND);
		// 線のつなぎ目を丸くする
		paint.setStrokeJoin(Paint.Join.ROUND);

		bmp = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
		bmpCanvas = new Canvas(bmp);

		// ペンサンプルの更新
		previewSample();

		// 閉じる
		Button closeBtn = (Button)findViewById(R.id.button_close);
		closeBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("Color", color);
				intent.putExtra("Size", size);
				setResult(RESULT_OK, intent);
				finish();
			}});

		// クリックイベントリスナ
		OnClickListener ivPaletteListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				int color = (Integer) v.getTag();
				setColor(color);
				previewSample();
			}
		};
		OnClickListener ivPensizeListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				int size = (Integer) v.getTag();
				setSize(size);
				previewSample();
			}
		};

		// イベントリスナの登録
		findViewById(R.id.IvPaletteBlack).setOnClickListener(ivPaletteListener);
		findViewById(R.id.IvPaletteWhite).setOnClickListener(ivPaletteListener);
		findViewById(R.id.IvPaletteGray).setOnClickListener(ivPaletteListener);
		findViewById(R.id.IvPaletteRed).setOnClickListener(ivPaletteListener);
		findViewById(R.id.IvPaletteGreen).setOnClickListener(ivPaletteListener);
		findViewById(R.id.IvPaletteBlue).setOnClickListener(ivPaletteListener);
		findViewById(R.id.IvPaletteYellow).setOnClickListener(ivPaletteListener);
		findViewById(R.id.IvPaletteCyan).setOnClickListener(ivPaletteListener);
		findViewById(R.id.IvPaletteMagenta).setOnClickListener(ivPaletteListener);
		findViewById(R.id.IvPensize1).setOnClickListener(ivPensizeListener);
		findViewById(R.id.IvPensize2).setOnClickListener(ivPensizeListener);
		findViewById(R.id.IvPensize4).setOnClickListener(ivPensizeListener);
		findViewById(R.id.IvPensize16).setOnClickListener(ivPensizeListener);
		findViewById(R.id.IvPensize32).setOnClickListener(ivPensizeListener);

		// 色をタグとして登録
		findViewById(R.id.IvPaletteBlack).setTag(Color.BLACK);
		findViewById(R.id.IvPaletteWhite).setTag(Color.WHITE);
		findViewById(R.id.IvPaletteGray).setTag(Color.GRAY);
		findViewById(R.id.IvPaletteRed).setTag(Color.RED);
		findViewById(R.id.IvPaletteGreen).setTag(Color.GREEN);
		findViewById(R.id.IvPaletteBlue).setTag(Color.BLUE);
		findViewById(R.id.IvPaletteYellow).setTag(Color.YELLOW);
		findViewById(R.id.IvPaletteCyan).setTag(Color.CYAN);
		findViewById(R.id.IvPaletteMagenta).setTag(Color.MAGENTA);
		// サイズをタグとして登録
		findViewById(R.id.IvPensize1).setTag(1);
		findViewById(R.id.IvPensize2).setTag(2);
		findViewById(R.id.IvPensize4).setTag(4);
		findViewById(R.id.IvPensize16).setTag(16);
		findViewById(R.id.IvPensize32).setTag(32);

		//センサーサービス取得
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

		//センサーマネージャのリスナ登録破棄
    	mSensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	/**
	* センサーイベント
	*/
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			// 傾き(ハイカット)
			currentOrientationValues[0] = event.values[0] * 0.1f + currentOrientationValues[0] * (1.0f - 0.1f);
			currentOrientationValues[1] = event.values[1] * 0.1f + currentOrientationValues[1] * (1.0f - 0.1f);
			currentOrientationValues[2] = event.values[2] * 0.1f + currentOrientationValues[2] * (1.0f - 0.1f);
			// 加速度(ローカット)
			currentAccelerationValues[0] = event.values[0] - currentOrientationValues[0];
			currentAccelerationValues[1] = event.values[1] - currentOrientationValues[1];
			currentAccelerationValues[2] = event.values[2] - currentOrientationValues[2];
			// 振ってる？ 絶対値(あるいは2乗の平方根)の合計がいくつ以上か？
			// 実装例
			float targetValue = 
				Math.abs(currentAccelerationValues[0]) + 
				Math.abs(currentAccelerationValues[1]) +
				Math.abs(currentAccelerationValues[2]);
			if(targetValue > SHAKE) {
				Intent intent = new Intent();
				intent.putExtra("Color", color);
				intent.putExtra("Size", size);
				setResult(RESULT_OK, intent);
				finish();
			}
		}
	}

	// ペン色設定
	public void setColor(int color) {
		this.color = color;
	}
	// ペンサイズ設定
	public void setSize(int size) {
		this.size = size;
	}

	// ペンサンプルの更新
	private void previewSample() {
		paint.setColor(color);
		paint.setStrokeWidth(size);
		bmpCanvas.drawColor(Color.WHITE);
		bmpCanvas.drawPoint(50, 50, paint);

		ImageView ivPenSample = (ImageView)findViewById(R.id.IvPenSample);
		if(ivPenSample != null) {
			ivPenSample.setImageBitmap(bmp);
		}
	}
}