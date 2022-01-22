package com.example.instagram.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.instagram.R;

public class DialogAlert_option extends Dialog implements View.OnClickListener{
    private Context context;
    private OnDialogClickListener onDialogClickListener;

    private TextView tv_title, tv_msg;
    private Button btn_ok, btn_cancel;
    private String title = "";
    private String msg = "";
    private String ok = "";
    private String cancel = "";

    public DialogAlert_option(Context context, String title, String msg, String ok, String cancel, OnDialogClickListener onDialogClickListener){
        super(context, R.style.DefaultDialog);

        this.context = context;
        this.title = title;
        this.msg = msg;
        this.ok = ok;
        this.cancel = cancel;
        this.onDialogClickListener = onDialogClickListener;

        setCancelable(true); //是否允許用返回鍵關閉Dialog
        setCanceledOnTouchOutside(true);

        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_alert_option, null);
        setContentView(view);
        tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText(title);
        tv_msg = view.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        btn_ok = view.findViewById(R.id.btn_ok);
        btn_ok.setText(ok);
        btn_ok.setOnClickListener(this);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_cancel.setText(cancel);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_ok) {
            onDialogClickListener.onDialogClick(0);
        }
        dismiss();
    }
}
