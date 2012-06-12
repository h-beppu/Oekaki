package com.hide_ab.Oekaki;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class OekakiActivity extends Activity {
	private PictView pview = null;
	// ���j���[�A�C�e��ID
	private static final int
		MENU_ITEM0 = 0,
		MENU_ITEM1 = 1,
		MENU_ITEM2 = 2,
		MENU_ITEM3 = 3;

	// set default value
	String[] str_colors = {"����", "����", "�͂�����", "����", "�݂ǂ�", "����", "������", "�݂�����", "�ނ炳��"};
	int[] colors = {Color.BLACK, Color.WHITE, Color.GRAY, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA};
	int result_color;

	String[] str_sizes = {"�ƂĂ��ق���", "�ق���", "�ӂ�", "�ӂƂ�", "�ƂĂ��ӂƂ�"};
	int[] sizes = {1, 2, 4, 8, 16};
	int result_size;

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
		MenuItem item1 = menu.add(Menu.NONE, MENU_ITEM1, Menu.NONE, "����");
		item1.setIcon(android.R.drawable.ic_menu_add);
		MenuItem item2 = menu.add(Menu.NONE, MENU_ITEM2, Menu.NONE, "�ӂƂ�");
		item2.setIcon(android.R.drawable.ic_menu_add);
		MenuItem item3 = menu.add(Menu.NONE, MENU_ITEM3, Menu.NONE, "���ǂ�");
		item3.setIcon(android.R.drawable.ic_menu_add);

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
				// Single Choice Dialog
				new AlertDialog.Builder(this)
					.setIcon(R.drawable.icon)
					.setTitle("����������ł�")
					.setSingleChoiceItems(str_colors, result_color,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								result_color = which;
							}
						})
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						/* OK�{�^�����N���b�N�������̏��� */
						public void onClick(DialogInterface dialog, int whichButton) {
							int color = colors[result_color];
							OekakiActivity.this.pview.SetColor(color);
//							new AlertDialog.Builder(OekakiActivity.this)
//								.setTitle("color=" + result_item)
//								.show();
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						/* Cancel �{�^�����N���b�N�������̏��� */
						public void onClick(DialogInterface dialog, int whichButton) {
							new AlertDialog.Builder(OekakiActivity.this)
								.setTitle("Canceled")
								.show();
						}
					})
					.show();
				break;
			case MENU_ITEM2:
				// Single Choice Dialog
				new AlertDialog.Builder(this)
					.setIcon(R.drawable.icon)
					.setTitle("�ӂƂ��������ł�")
					.setSingleChoiceItems(str_sizes, result_size,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								result_size = which;
							}
						})
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						/* OK�{�^�����N���b�N�������̏��� */
						public void onClick(DialogInterface dialog, int whichButton) {
							int size = sizes[result_size];
							OekakiActivity.this.pview.SetSize(size);
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						/* Cancel �{�^�����N���b�N�������̏��� */
						public void onClick(DialogInterface dialog, int whichButton) {
							new AlertDialog.Builder(OekakiActivity.this)
								.setTitle("Canceled")
								.show();
						}
					})
					.show();
				break;
			case MENU_ITEM3:
				this.pview.Undo();
				break;
		}

		return true;
	}
}