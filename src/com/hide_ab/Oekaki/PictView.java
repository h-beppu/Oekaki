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
	private int type = 0;		// �C�x���g�̃^�C�v
	private float posx = 0.0f;	// �C�x���g���N����X���W
	private float posy = 0.0f;	// �C�x���g���N����Y���W
	private float bakx = 0.0f;	// �O���X���W
	private float baky = 0.0f;	// �O���Y���W
	private float sttx = 0.0f;	// �����X���W
	private float stty = 0.0f;	// �����Y���W
	private float rr = -1;
	private float centerx, centery;
	private final int REPEAT_INTERVAL = 50;
	private final int BRUSH_SIZE = 4;

	public final static int MODE_LINE = 0,
					MODE_CIRCLE = 1,
					MODE_RECT = 2;

	private int mode = MODE_LINE;
	private SurfaceHolder holder;
	private Thread thread = null;		// �X���b�h�N���X

	private Path path = null;			// �p�X
	private Paint paint = null;			// �`��p
	private Bitmap bmp = null;
	private Canvas bmpCanvas;
	private Paint paint2 = null;
	private Bitmap bmp2 = null;
	private Canvas bmpCanvas2;
	private Bitmap bmpUndo = null;

	private static final String LOG = "MainSurfaceView";

	public PictView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Touch�ɑ΂���C�x���g�n���h����o�^
		setOnTouchListener(this);

		// SurfaceView�`��ɗp����R�[���o�b�N��o�^
		holder = getHolder();
		holder.addCallback(this);

		// �`��p�̏���
		paint = new Paint();
		// �A���`�G�C���A�X��L���ɂ���
		paint.setAntiAlias(true);
		// ���F�A�����x100
		paint.setColor(Color.BLACK);
//		paint.setColor(Color.argb(100, 0, 0, 255));
		// ���̂�(�h��Ԃ��Ȃ�)
		paint.setStyle(Paint.Style.STROKE);
		// ���̑���
		paint.setStrokeWidth(BRUSH_SIZE);
		// ���̗��[���ۂ�����
		paint.setStrokeCap(Paint.Cap.ROUND);
		// ���̂Ȃ��ڂ��ۂ�����
		paint.setStrokeJoin(Paint.Join.ROUND);

		// �`��p�̏���
		paint2 = new Paint();
		// �A���`�G�C���A�X��L���ɂ���
		paint2.setAntiAlias(true);
		// ���F�A�����x100
		paint2.setColor(Color.TRANSPARENT);
		paint2.setAlpha(0);
		// ���̂�(�h��Ԃ��Ȃ�)
		paint2.setStyle(Paint.Style.STROKE);
		// ���̑���
		paint2.setStrokeWidth(BRUSH_SIZE);
		// ���̗��[���ۂ�����
		paint2.setStrokeCap(Paint.Cap.ROUND);
		// ���̂Ȃ��ڂ��ۂ�����
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

		// �X���b�h�J�n
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.d(LOG, "change");
		// TODO ����͉������Ȃ��B
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread = null;
	}

	public boolean onTouch(View view, MotionEvent event) {
		float rx, ry;

		type = event.getAction();	//�C�x���g�̃^�C�v
		posx = event.getX();		//�C�x���g���N����X���W
		posy = event.getY();		//�C�x���g���N����Y���W

		// �C�x���g�̃^�C�v���Ƃɏ�����ݒ�
		switch(type){
			case MotionEvent.ACTION_DOWN:	//�ŏ��̃|�C���g
				// ����̕ۑ�
				bmpUndo = bmp.copy(Bitmap.Config.ARGB_8888, false);

				switch(mode) {
					// �ʏ�`�掞
					case MODE_LINE:
						// �p�X��������
						path = new Path();
						// �p�X�̎n�_�ֈړ�
						path.moveTo(posx, posy);
						break;
					// �~�E�l�p�`�掞
					case MODE_CIRCLE:
					case MODE_RECT:
						sttx = posx;
						stty = posy;
						break;
				}
				break;
			case MotionEvent.ACTION_MOVE:	//�r���̃|�C���g
				switch(mode) {
					// �ʏ�`�掞
					case MODE_LINE:
						// �ЂƂO�̃|�C���g�����������
						path.lineTo(posx, posy);
						// �`��o�b�t�@�Ƀp�X��`�悷��
						if(path != null) {
							bmpCanvas.drawPath(path, paint);
						}
						break;
					// �~�`�掞
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
					// �l�p�`�掞
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
			case MotionEvent.ACTION_UP:		//�Ō�̃|�C���g
				switch(mode) {
					// �ʏ�`�掞
					case MODE_LINE:
						// �_��ł�
						if((posx == bakx) && (posy == baky)) {
							bmpCanvas.drawPoint(posx, posy, paint);
						}
						// �ЂƂO�̃|�C���g�����������
						else {
							path.lineTo(posx, posy);
							// �`��o�b�t�@�Ƀp�X��`�悷��
							if(path != null) {
								bmpCanvas.drawPath(path, paint);
							}
						}
						break;
					// �~�`�掞
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
					// �l�p�`�掞
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

		// ���W�̕ۑ�
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
		// Runnable�C���^�[�t�F�[�X��implements���Ă���̂ŁArun���\�b�h����������
		// ����́AThread�N���X�̃R���X�g���N�^�ɓn�����߂ɗp����B
		while(thread != null) {
			// Canvas�̊m��
			Canvas canvas = holder.lockCanvas();
			if(canvas != null) {
				// �`��o�b�t�@�̃f�[�^�������o��
				canvas.drawBitmap(bmp, 0, 0, null);
				canvas.drawBitmap(bmp2, 0, 0, null);
				// Canvas�̉��
				holder.unlockCanvasAndPost(canvas);
			}

			// �E�F�C�g
			try {
				thread.sleep(REPEAT_INTERVAL);
			}
			catch(InterruptedException e) {
			}
		}
	}
}