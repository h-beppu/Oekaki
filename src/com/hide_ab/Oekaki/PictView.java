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
	private int type = 0;				// �C�x���g�̃^�C�v
	private float posx = 0.0f;			// �C�x���g���N����X���W
	private float posy = 0.0f;			// �C�x���g���N����Y���W
	private final int REPEAT_INTERVAL = 2;
	private final int BRUSH_SIZE = 4;

	private Path path = null;			// �p�X
	private Paint paint = null;			// �`��p

	private Thread thread = null;		// �X���b�h�N���X
	private static final String LOG = "MainSurfaceView";

	private Bitmap bmp = null;

	public PictView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Touch�ɑ΂���C�x���g�n���h����o�^
		setOnTouchListener(this);

		// SurfaceView�`��ɗp����R�[���o�b�N��o�^
		getHolder().addCallback(this);

		// �`��p�̏���
		paint = new Paint();
		//�A���`�G�C���A�X��L���ɂ���
		paint.setAntiAlias(true);
		//�F�A�����x100
		paint.setColor(Color.WHITE);
//		paint.setColor(Color.argb(100, 0, 0, 255));
		//���̂�(�h��Ԃ��Ȃ�)
		paint.setStyle(Paint.Style.STROKE);
		//���̑���8
		paint.setStrokeWidth(BRUSH_SIZE);
		//���̗��[���ۂ�����
		paint.setStrokeCap(Paint.Cap.ROUND);
		//���̂Ȃ��ڂ��ۂ�����
		paint.setStrokeJoin(Paint.Join.ROUND);

		Log.d(LOG, "start");
	}

	public boolean onTouch(View view, MotionEvent event) {
		type = event.getAction();	//�C�x���g�̃^�C�v
		posx = event.getX();		//�C�x���g���N����X���W
		posy = event.getY();		//�C�x���g���N����Y���W
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.d(LOG, "change");
		// TODO ����͉������Ȃ��B
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(LOG, "create");

		// Canvas�̊m��(1st�o�b�t�@)
		Canvas canvas = holder.lockCanvas();
		canvas.drawColor(Color.BLUE);
		// Canvas�̉��
		holder.unlockCanvasAndPost(canvas);

		// Canvas�̊m��(2nd�o�b�t�@)
		canvas = holder.lockCanvas();
		canvas.drawColor(Color.BLUE);
		// Canvas�̉��
		holder.unlockCanvasAndPost(canvas);

/*
		int w = getWidth();
		int h = getHeight();
		bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bmp);
		canvas.drawColor(Color.BLUE);
*/

		// �X���b�h�J�n
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread = null;
	}

	@Override
	public void run() {
		// Runnable�C���^�[�t�F�[�X��implements���Ă���̂ŁArun���\�b�h����������
		// ����́AThread�N���X�̃R���X�g���N�^�ɓn�����߂ɗp����B
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

			// Canvas�̊m��
			Canvas canvas = getHolder().lockCanvas();
			if(canvas != null) {
//				canvas.drawBitmap(bmp, 0, 0, null);

				// �C�x���g�̃^�C�v���Ƃɏ�����ݒ�
				switch(type){
					case MotionEvent.ACTION_DOWN:	//�ŏ��̃|�C���g
						//�p�X��������
						path = new Path();
						//�p�X�̎n�_�ֈړ�
						path.moveTo(posx, posy);
						break;
					case MotionEvent.ACTION_MOVE:	//�r���̃|�C���g
						//�ЂƂO�̃|�C���g�����������
						path.lineTo(posx, posy);
						break;
					case MotionEvent.ACTION_UP:		//�Ō�̃|�C���g
						//�ЂƂO�̃|�C���g�����������
						path.lineTo(posx, posy);
						break;
					default:
						break;
				}
				// �`��
				canvas.drawPath(path, paint);
				// Canvas�̉��
				getHolder().unlockCanvasAndPost(canvas);
			}
		}
	}
}