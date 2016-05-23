package org.csware.ee.widget;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.csware.ee.shipper.R;


public class ActionSheet {

	public interface OnActionSheetSelected {
		void onClick(int whichButton);
	}

	private ActionSheet() {
	}

	public static Dialog showSheet(Context context, final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener,OnClickListener listener, final Dialog dlg) {
//		final Dialog dlg = new Dialog(context, R.style.MyDialogStyleBottom);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.actionsheet, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		TextView delete = (TextView) layout.findViewById(R.id.delete);
		TextView mCancel = (TextView) layout.findViewById(R.id.cancel);

		delete.setOnClickListener(listener);

/*		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actionSheetSelected.onClick(0);
				dlg.dismiss();
			}
		});*/

		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actionSheetSelected.onClick(1);
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(false);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

}
