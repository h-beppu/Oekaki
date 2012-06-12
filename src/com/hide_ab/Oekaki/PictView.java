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
	private int type = 0;		// �C�x���g�̃^�C�v
	private float posx = 0.0f;	// �C�x���g���N����X���W
	private float posy = 0.0f;	// �C�x���g���N����Y���W
	private float bakx = 0.0f;	// �O���X���W
	private float baky = 0.0f;	// �O���Y���W
	private final int REPEAT_INTERVAL = 50;
	private final int BRUSH_SIZE = 4;

	private SurfaceHolder holder;
	private Thread thread = null;		// �X���b�h�N���X

	private Path path = null;			// �p�X
	private Paint paint = null;			// �`��p
	private Bitmap bmp = null;
	private Canvas bmpCanvas;

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
		paint.setColor(Color.WHITE);
//		paint.setColor(Color.argb(100, 0, 0, 255));
		// ���̂�(�h��Ԃ��Ȃ�)
		paint.setStyle(Paint.Style.STROKE);
		// ���̑���
		paint.setStrokeWidth(BRUSH_SIZE);
		// ���̗��[���ۂ�����
		paint.setStrokeCap(Paint.Cap.ROUND);
		// ���̂Ȃ��ڂ��ۂ�����
		paint.setStrokeJoin(Paint.Join.ROUND);

		Log.d(LOG, "start");
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		bmpCanvas = new Canvas(bmp);
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
		type = event.getAction();	//�C�x���g�̃^�C�v
		posx = event.getX();		//�C�x���g���N����X���W
		posy = event.getY();		//�C�x���g���N����Y���W

		// �C�x���g�̃^�C�v���Ƃɏ�����ݒ�
		switch(type){
			case MotionEvent.ACTION_DOWN:	//�ŏ��̃|�C���g
				// �p�X��������
				path = new Path();
				// �p�X�̎n�_�ֈړ�
				path.moveTo(posx, posy);
				break;
			case MotionEvent.ACTION_MOVE:	//�r���̃|�C���g
				// �ЂƂO�̃|�C���g�����������
				path.lineTo(posx, posy);
				break;
			case MotionEvent.ACTION_UP:		//�Ō�̃|�C���g
				// �_��ł�
				if((posx == bakx) && (posy == baky)) {
					bmpCanvas.drawPoint(posx, posy, paint);
				}
				// �ЂƂO�̃|�C���g�����������
				else {
					path.lineTo(posx, posy);
				}
				break;
			default:
				break;
		}

		// �`��o�b�t�@�Ƀp�X��`�悷��
		if(path != null) {
			bmpCanvas.drawPath(path, paint);
		}

		// ���W�̕ۑ�
		bakx = posx;
		baky = posy;

		return true;
	}

	public void Clear() {
		// �`��o�b�t�@�Ƀp�X��`�悷��
		if(path != null) {
			bmpCanvas.drawRGB(0, 0, 0);
		}
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