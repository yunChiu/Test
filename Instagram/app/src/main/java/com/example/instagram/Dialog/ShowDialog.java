package com.example.instagram.Dialog;

import android.content.Context;

public class ShowDialog {
    public static void alert(Context context, String title, String msg, String ok, OnDialogClickListener onDialogClickListener){
        DialogAlert dialogAlert = new DialogAlert(context, title, msg, ok, onDialogClickListener);
        dialogAlert.show();
    }
}
