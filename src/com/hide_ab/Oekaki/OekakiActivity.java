package com.hide_ab.Oekaki;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class OekakiActivity extends Activity {
	private PictView pview = null;
	// ���j���[�A�C�e��ID
	private static final int
		MENU_ITEM0 = 0,
		MENU_ITEM1 = 1;

	// �A�N�e�B�r�e�B���������ꂽ�Ƃ��ɌĂяo����郁�\�b�h
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(new PictView(this));
		setContentView(R.layout.main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem item0 = menu.add(Menu.NONE, MENU_ITEM0, Menu.NONE, "����");
		item0.setIcon(android.R.drawable.ic_menu_delete);
		MenuItem item1 = menu.add(Menu.NONE, MENU_ITEM1, Menu.NONE, "���̂�");
		item1.setIcon(android.R.drawable.ic_menu_add);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(this.pview == null) {
			this.pview = (PictView) findViewById(R.id.vw_canvas);
		}

		switch(item.getItemId()) {
			case MENU_ITEM0:
				this.pview.Clear();
				break;
			case MENU_ITEM1:
				break;
		}

		return true;
	}
}