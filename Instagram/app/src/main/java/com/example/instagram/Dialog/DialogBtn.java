package com.example.instagram.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.instagram.R;

public class DialogBtn extends Dialog implements View.OnClickListener {
    private Context context;
    private OnDialogClickListener onDialogClickListener;

    View link, share, whistleblowing;
    TextView why, hide, unFollow;


    public DialogBtn(Context context, OnDialogClickListener onDialogClickListener) {
        super(context, R.style.DefaultDialog_anim);

        this.context = context;
        this.onDialogClickListener = onDialogClickListener;

        setCancelable(true); //是否允許用返回鍵關閉Dialog
        setCanceledOnTouchOutside(true);

        initView();

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        onWindowAttributesChanged(layoutParams);
    }

    public void initView(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_btn, null);
        setContentView(view);
        link = view.findViewById(R.id.ll_link);
        link.setOnClickListener(this);
        share = view.findViewById(R.id.ll_share);
        share.setOnClickListener(this);
        whistleblowing = view.findViewById(R.id.ll_whistleblowing);
        whistleblowing.setOnClickListener(this);
        why = view.findViewById(R.id.tv_why);
        why.setOnClickListener(this);
        hide = view.findViewById(R.id.tv_hide);
        hide.setOnClickListener(this);
        unFollow = view.findViewById(R.id.tv_unFollow);
        unFollow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ll_link:
                onDialogClickListener.onDialogClick(0);
                dismiss();
                break;
            case R.id.ll_share:
                onDialogClickListener.onDialogClick(1);
                dismiss();
                break;
            case R.id.ll_whistleblowing:
                onDialogClickListener.onDialogClick(2);
                dismiss();
                break;
            case R.id.tv_why:
                onDialogClickListener.onDialogClick(3);
                dismiss();
                break;
            case R.id.tv_hide:
                onDialogClickListener.onDialogClick(4);
                dismiss();
                break;
            case R.id.tv_unFollow:
                onDialogClickListener.onDialogClick(5);
                dismiss();
                break;
            default:
                break;
        }
    }
}
