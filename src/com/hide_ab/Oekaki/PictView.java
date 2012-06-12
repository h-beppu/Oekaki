package com.hide_ab.Oekaki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class PictView extends  SurfaceView implements SurfaceHolder.Callback, Runnable, OnTouchListener {
	private int type = 0;				// イベントのタイプ
	private float posx = 0.0f;			// イベントが起きたX座標
	private float posy = 0.0f;			// イベントが起きたY座標
	private final int REPEAT_INTERVAL = 2;
	private final int BRUSH_SIZE = 4;

	private Path path = null;			// パス
	private Paint paint = null;			// 描画用

	private Thread thread = null;		// スレッドクラス
	private static final String LOG = "MainSurfaceView";

	private Bitmap bmp = null;

	public PictView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Touchに対するイベントハンドラを登録
		setOnTouchListener(this);

		// SurfaceView描画に用いるコールバックを登録
		getHolder().addCallback(this);

		// 描画用の準備
		paint = new Paint();
		//アンチエイリアスを有効にする
		paint.setAntiAlias(true);
		//青色、透明度100
		paint.setColor(Color.WHITE);
//		paint.setColor(Color.argb(100, 0, 0, 255));
		//線のみ(塗りつぶさない)
		paint.setStyle(Paint.Style.STROKE);
		//線の太さ8
		paint.setStrokeWidth(BRUSH_SIZE);
		//線の両端を丸くする
		paint.setStrokeCap(Paint.Cap.ROUND);
		//線のつなぎ目を丸くする
		paint.setStrokeJoin(Paint.Join.ROUND);

		Log.d(LOG, "start");
	}

	public boolean onTouch(View view, MotionEvent event) {
		type = event.getAction();	//イベントのタイプ
		posx = event.getX();		//イベントが起きたX座標
		posy = event.getY();		//イベントが起きたY座標
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.d(LOG, "change");
		// TODO 今回は何もしない。
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(LOG, "create");

		// Canvasの確保(1stバッファ)
		Canvas canvas = holder.lockCanvas();
		canvas.drawColor(Color.BLUE);
		// Canvasの解放
		holder.unlockCanvasAndPost(canvas);

		// Canvasの確保(2ndバッファ)
		canvas = holder.lockCanvas();
		canvas.drawColor(Color.BLUE);
		// Canvasの解放
		holder.unlockCanvasAndPost(canvas);

/*
		int w = getWidth();
		int h = getHeight();
		bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bmp);
		canvas.drawColor(Color.BLUE);
*/

		// スレッド開始
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread = null;
	}

	@Override
	public void run() {
		// Runnableインターフェースをimplementsしているので、runメソッドを実装する
		// これは、Threadクラスのコンストラクタに渡すために用いる。
		while(thread != null) {
/*
			if(bmp == null) {
				continue;
			}
*/

			try {
				thread.sleep(REPEAT_INTERVAL);
			}
			catch(InterruptedException e) {
				Log.d(LOG, "sleep fail.");
			}

			// Canvasの確保
			Canvas canvas = getHolder().lockCanvas();
			if(canvas != null) {
//				canvas.drawBitmap(bmp, 0, 0, null);

				// イベントのタイプごとに処理を設定
				switch(type){
					case MotionEvent.ACTION_DOWN:	//最初のポイント
						//パスを初期化
						path = new Path();
						//パスの始点へ移動
						path.moveTo(posx, posy);
						break;
					case MotionEvent.ACTION_MOVE:	//途中のポイント
						//ひとつ前のポイントから線を引く
						path.lineTo(posx, posy);
						break;
					case MotionEvent.ACTION_UP:		//最後のポイント
						//ひとつ前のポイントから線を引く
						path.lineTo(posx, posy);
						break;
					default:
						break;
				}
				// 描画
				canvas.drawPath(path, paint);
				// Canvasの解放
				getHolder().unlockCanvasAndPost(canvas);
			}
		}
	}
}