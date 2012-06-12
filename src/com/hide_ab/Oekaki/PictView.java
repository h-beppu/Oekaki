package com.hide_ab.Oekaki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class PictView extends  SurfaceView implements SurfaceHolder.Callback, Runnable, OnTouchListener {
	private int type = 0;				// �C�x���g�̃^�C�v
	private float posx = 0.0f;			// �C�x���g���N����X���W
	private float posy = 0.0f;			// �C�x���g���N����Y���W

	private Path path = null;			// �p�X

	private Paint paint = null;			// �`��p

	private Thread mainLoop = null;		// �X���b�h�N���X

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
		paint.setStrokeWidth(8);
		//���̗��[���ۂ�����
		paint.setStrokeCap(Paint.Cap.ROUND);
		//���̂Ȃ��ڂ��ۂ�����
		paint.setStrokeJoin(Paint.Join.ROUND);

		// �X���b�h�J�n
		mainLoop = new Thread(this);
		mainLoop.start();
	}

	public boolean onTouch(View view, MotionEvent event) {
		type = event.getAction();	//�C�x���g�̃^�C�v
		posx = event.getX();		//�C�x���g���N����X���W
		posy = event.getY();		//�C�x���g���N����Y���W
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO ����͉������Ȃ��B
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO ����͉������Ȃ��B
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO ����͉������Ȃ��B
	}

	@Override
	public void run() {
		// Runnable�C���^�[�t�F�[�X��implements���Ă���̂ŁArun���\�b�h����������
		// ����́AThread�N���X�̃R���X�g���N�^�ɓn�����߂ɗp����B
		while (true) {
			// Canvas�̊m��
			Canvas canvas = getHolder().lockCanvas();
			if(canvas != null) {
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