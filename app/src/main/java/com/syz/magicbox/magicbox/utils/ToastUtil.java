package com.syz.magicbox.magicbox.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	public static void showToast(Context ctx,String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}
}
