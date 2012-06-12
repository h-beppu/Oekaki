package com.hide_ab.Oekaki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class PictView extends View implements OnTouchListener {
	private int type = 0;				//イベントのタイプ
	private float posx = 0.0f;			//イベントが起きたX座標
	private float posy = 0.0f;			//イベントが起きたY座標
	private Path path = null;			//パス
	private Bitmap bitmap = null;		//Viewの状態を保存するためのBitmap

	public PictView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnTouchListener(this);
	}

	public boolean onTouch(View view, MotionEvent event) {
		type = event.getAction();	//イベントのタイプ
		posx = event.getX();		//イベントが起きたX座標
		posy = event.getY();		//イベントが起きたY座標

		//イベントのタイプごとに処理を設定
		switch(type){
			case MotionEvent.ACTION_DOWN:	//最初のポイント
				//パスを初期化
				path = new Path();
				//パスの始点へ移動
				path.moveTo(posx, posy);
				break;
			case MotionEvent.ACTION_MOVE:	//途中のポイント
				//ひとつ前のポイントから、線を引く
				path.lineTo(posx, posy);
				break;
			case MotionEvent.ACTION_UP:		//最後のポイント
				//ひとつ前のポイントから線を引く
				path.lineTo(posx, posy);

				//現在のViewをbitmapに保存する
				view.setDrawingCacheEnabled(true);
				bitmap = Bitmap.createBitmap(view.getDrawingCache());
				view.setDrawingCacheEnabled(false);
		}

		//Viewを更新する
		view.invalidate();

		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		//背景を白く塗りつぶす
		canvas.drawColor(Color.WHITE);
		if(bitmap != null) {
			//保存してあるBitmapを描画する
			canvas.drawBitmap(bitmap, 0, 0, null);
		}

		Paint paint = new Paint();
		//アンチエイリアスを有効にする
		paint.setAntiAlias(true);
		//青色、透明度100
		paint.setColor(Color.argb(100, 0, 0, 255));
		//線のみ(塗りつぶさない)
		paint.setStyle(Paint.Style.STROKE);
		//線の太さ8
		paint.setStrokeWidth(8);
		//線の両端を丸くする
		paint.setStrokeCap(Paint.Cap.ROUND);
		//線のつなぎ目を丸くする
		paint.setStrokeJoin(Paint.Join.ROUND);

		if(path != null){
			//パスを描画する
			canvas.drawPath(path, paint);
		}
	}
}
