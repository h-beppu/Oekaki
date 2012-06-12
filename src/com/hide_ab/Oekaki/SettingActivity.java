package com.hide_ab.Oekaki;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class SettingActivity extends Activity {
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