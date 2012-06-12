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
	private int type = 0;				//�C�x���g�̃^�C�v
	private float posx = 0.0f;			//�C�x���g���N����X���W
	private float posy = 0.0f;			//�C�x���g���N����Y���W
	private Path path = null;			//�p�X
	private Bitmap bitmap = null;		//View�̏�Ԃ�ۑ����邽�߂�Bitmap

	public PictView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnTouchListener(this);
	}

	public boolean onTouch(View view, MotionEvent event) {
		type = event.getAction();	//�C�x���g�̃^�C�v
		posx = event.getX();		//�C�x���g���N����X���W
		posy = event.getY();		//�C�x���g���N����Y���W

		//�C�x���g�̃^�C�v���Ƃɏ�����ݒ�
		switch(type){
			case MotionEvent.ACTION_DOWN:	//�ŏ��̃|�C���g
				//�p�X��������
				path = new Path();
				//�p�X�̎n�_�ֈړ�
				path.moveTo(posx, posy);
				break;
			case MotionEvent.ACTION_MOVE:	//�r���̃|�C���g
				//�ЂƂO�̃|�C���g����A��������
				path.lineTo(posx, posy);
				break;
			case MotionEvent.ACTION_UP:		//�Ō�̃|�C���g
				//�ЂƂO�̃|�C���g�����������
				path.lineTo(posx, posy);

				//���݂�View��bitmap�ɕۑ�����
				view.setDrawingCacheEnabled(true);
				bitmap = Bitmap.createBitmap(view.getDrawingCache());
				view.setDrawingCacheEnabled(false);
		}

		//View���X�V����
		view.invalidate();

		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		//�w�i�𔒂��h��Ԃ�
		canvas.drawColor(Color.WHITE);
		if(bitmap != null) {
			//�ۑ����Ă���Bitmap��`�悷��
			canvas.drawBitmap(bitmap, 0, 0, null);
		}

		Paint paint = new Paint();
		//�A���`�G�C���A�X��L���ɂ���
		paint.setAntiAlias(true);
		//�F�A�����x100
		paint.setColor(Color.argb(100, 0, 0, 255));
		//���̂�(�h��Ԃ��Ȃ�)
		paint.setStyle(Paint.Style.STROKE);
		//���̑���8
		paint.setStrokeWidth(8);
		//���̗��[���ۂ�����
		paint.setStrokeCap(Paint.Cap.ROUND);
		//���̂Ȃ��ڂ��ۂ�����
		paint.setStrokeJoin(Paint.Join.ROUND);

		if(path != null){
			//�p�X��`�悷��
			canvas.drawPath(path, paint);
		}
	}
}
