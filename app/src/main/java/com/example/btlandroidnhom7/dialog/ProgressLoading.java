package com.example.btlandroidnhom7.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;

import com.example.btlandroidnhom7.R;

public class ProgressLoading {
    protected static ProgressDialog pd_loading;

    public static void show(Context context) {
        pd_loading = ProgressDialog.show(context, null, null, true, false);
        pd_loading.setContentView(R.layout.base_loading);
        pd_loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pd_loading.setCanceledOnTouchOutside(false);
        pd_loading.getWindow().setGravity(Gravity.CENTER);
        pd_loading.setCancelable(false);
        pd_loading.show();
    }

    public static void dismiss() {
        if (pd_loading != null) {
            pd_loading.dismiss();
        }
    }
}
