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
	private int width, height;
	private int type = 0;		// イベントのタイプ
	private float posx = 0.0f;	// イベントが起きたX座標
	private float posy = 0.0f;	// イベントが起きたY座標
	private float bakx = 0.0f;	// 前回のX座標
	private float baky = 0.0f;	// 前回のY座標
	private float sttx = 0.0f;	// 初回のX座標
	private float stty = 0.0f;	// 初回のY座標
	private float rr = -1;
	private float centerx, centery;
	private final int REPEAT_INTERVAL = 50;
	private final int BRUSH_SIZE = 4;

	public final static int MODE_LINE = 0,
					MODE_CIRCLE = 1,
					MODE_RECT = 2;

	private int mode = MODE_LINE;
	private SurfaceHolder holder;
	private Thread thread = null;		// スレッドクラス

	private Path path = null;			// パス
	private Paint paint = null;			// 描画用
	private Bitmap bmp = null;
	private Canvas bmpCanvas;
	private Paint paint2 = null;
	private Bitmap bmp2 = null;
	private Canvas bmpCanvas2;
	private Bitmap bmpUndo = null;

	private static final String LOG = "MainSurfaceView";

	public PictView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Touchに対するイベントハンドラを登録
		setOnTouchListener(this);

		// SurfaceView描画に用いるコールバックを登録
		holder = getHolder();
		holder.addCallback(this);

		// 描画用の準備
		paint = new Paint();
		// アンチエイリアスを有効にする
		paint.setAntiAlias(true);
		// 黒色、透明度100
		paint.setColor(Color.BLACK);
//		paint.setColor(Color.argb(100, 0, 0, 255));
		// 線のみ(塗りつぶさない)
		paint.setStyle(Paint.Style.STROKE);
		// 線の太さ
		paint.setStrokeWidth(BRUSH_SIZE);
		// 線の両端を丸くする
		paint.setStrokeCap(Paint.Cap.ROUND);
		// 線のつなぎ目を丸くする
		paint.setStrokeJoin(Paint.Join.ROUND);

		// 描画用の準備
		paint2 = new Paint();
		// アンチエイリアスを有効にする
		paint2.setAntiAlias(true);
		// 黒色、透明度100
		paint2.setColor(Color.TRANSPARENT);
		paint2.setAlpha(0);
		// 線のみ(塗りつぶさない)
		paint2.setStyle(Paint.Style.STROKE);
		// 線の太さ
		paint2.setStrokeWidth(BRUSH_SIZE);
		// 線の両端を丸くする
		paint2.setStrokeCap(Paint.Cap.ROUND);
		// 線のつなぎ目を丸くする
		paint2.setStrokeJoin(Paint.Join.ROUND);

		Log.d(LOG, "start");
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		width = w;
		height = h;

		bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		bmpCanvas = new Canvas(bmp);
		bmpCanvas.drawColor(Color.WHITE);

		bmp2 = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		bmpCanvas2 = new Canvas(bmp2);
		bmpCanvas2.drawColor(Color.TRANSPARENT);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(LOG, "create");

		// スレッド開始
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.d(LOG, "change");
		// TODO 今回は何もしない。
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread = null;
	}

	public boolean onTouch(View view, MotionEvent event) {
		float rx, ry;

		type = event.getAction();	//イベントのタイプ
		posx = event.getX();		//イベントが起きたX座標
		posy = event.getY();		//イベントが起きたY座標

		// イベントのタイプごとに処理を設定
		switch(type){
			case MotionEvent.ACTION_DOWN:	//最初のポイント
				// 現状の保存
				bmpUndo = bmp.copy(Bitmap.Config.ARGB_8888, false);

				switch(mode) {
					// 通常描画時
					case MODE_LINE:
						// パスを初期化
						path = new Path();
						// パスの始点へ移動
						path.moveTo(posx, posy);
						break;
					// 円・四角描画時
					case MODE_CIRCLE:
					case MODE_RECT:
						sttx = posx;
						stty = posy;
						break;
				}
				break;
			case MotionEvent.ACTION_MOVE:	//途中のポイント
				switch(mode) {
					// 通常描画時
					case MODE_LINE:
						// ひとつ前のポイントから線を引く
						path.lineTo(posx, posy);
						// 描画バッファにパスを描画する
						if(path != null) {
							bmpCanvas.drawPath(path, paint);
						}
						break;
					// 円描画時
					case MODE_CIRCLE:
						if(rr >= 0) {
							bmp2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
							bmpCanvas2 = new Canvas(bmp2);
							bmpCanvas2.drawColor(Color.TRANSPARENT);
						}

						centerx = sttx + ((posx - sttx) / 2);
						centery = stty + ((posy - stty) / 2);

						rx = (float)Math.pow(posx - sttx, 2);
						ry = (float)Math.pow(posy - stty, 2);
						rr = (float)(Math.sqrt(rx + ry) / 2);

						bmpCanvas2.drawCircle(centerx, centery, rr, paint);
						break;
					// 四角描画時
					case MODE_RECT:
						bmp2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
						bmpCanvas2 = new Canvas(bmp2);
						bmpCanvas2.drawColor(Color.TRANSPARENT);

						float top, bottom, left, right;
						if(sttx > posx) {
							left  = posx;
							right = sttx;
						} else {
							left  = sttx;
							right = posx;
						}
						if(stty > posy) {
							top    = posy;
							bottom = stty;
						} else {
							top    = stty;
							bottom = posy;
						}

						bmpCanvas2.drawRect(left, top, right, bottom, paint);
						break;
				}
				break;
			case MotionEvent.ACTION_UP:		//最後のポイント
				switch(mode) {
					// 通常描画時
					case MODE_LINE:
						// 点を打つ
						if((posx == bakx) && (posy == baky)) {
							bmpCanvas.drawPoint(posx, posy, paint);
						}
						// ひとつ前のポイントから線を引く
						else {
							path.lineTo(posx, posy);
							// 描画バッファにパスを描画する
							if(path != null) {
								bmpCanvas.drawPath(path, paint);
							}
						}
						break;
					// 円描画時
					case MODE_CIRCLE:
						bmp2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
						bmpCanvas2 = new Canvas(bmp2);
						bmpCanvas2.drawColor(Color.TRANSPARENT);

						centerx = sttx + ((posx - sttx) / 2);
						centery = stty + ((posy - stty) / 2);

						rx = (float)Math.pow(posx - sttx, 2);
						ry = (float)Math.pow(posy - stty, 2);
						rr = (float)(Math.sqrt(rx + ry) / 2);

						bmpCanvas.drawCircle(centerx, centery, rr, paint);
						break;
					// 四角描画時
					case MODE_RECT:
						bmp2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
						bmpCanvas2 = new Canvas(bmp2);
						bmpCanvas2.drawColor(Color.TRANSPARENT);

						float top, bottom, left, right;
						if(sttx > posx) {
							left  = posx;
							right = sttx;
						} else {
							left  = sttx;
							right = posx;
						}
						if(stty > posy) {
							top    = posy;
							bottom = stty;
						} else {
							top    = stty;
							bottom = posy;
						}

						bmpCanvas.drawRect(left, top, right, bottom, paint);
						break;
				}
				break;
			default:
				break;
		}

		// 座標の保存
		bakx = posx;
		baky = posy;

		return true;
	}

	public void Clear() {
		bmpCanvas.drawColor(Color.WHITE);
		bmpCanvas2.drawColor(Color.WHITE);
	}

	public void SetColor(int color) {
		paint.setColor(color);
	}

	public void SetSize(int size) {
		paint.setStrokeWidth(size);
	}

	public void SetMode(int mode) {
		this.mode = mode;
	}

	public void Undo() {
		bmp = bmpUndo.copy(Bitmap.Config.ARGB_8888, true);
		bmpCanvas = new Canvas(bmp);
	}

	@Override
	public void run() {
		// Runnableインターフェースをimplementsしているので、runメソッドを実装する
		// これは、Threadクラスのコンストラクタに渡すために用いる。
		while(thread != null) {
			// Canvasの確保
			Canvas canvas = holder.lockCanvas();
			if(canvas != null) {
				// 描画バッファのデータを書き出し
				canvas.drawBitmap(bmp, 0, 0, null);
				canvas.drawBitmap(bmp2, 0, 0, null);
				// Canvasの解放
				holder.unlockCanvasAndPost(canvas);
			}

			// ウェイト
			try {
				thread.sleep(REPEAT_INTERVAL);
			}
			catch(InterruptedException e) {
			}
		}
	}
}