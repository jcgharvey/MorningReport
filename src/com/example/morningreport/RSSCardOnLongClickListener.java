package com.example.morningreport;

import android.view.View;
import android.view.View.OnLongClickListener;

public class RSSCardOnLongClickListener implements OnLongClickListener {

	@Override
	public boolean onLongClick(View v) {
		v.showContextMenu();
		return false;
	}

}
