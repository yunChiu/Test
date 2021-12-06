package com.example.instagram.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.model.DialogListItem;

import java.util.ArrayList;

public class DialogList_icon extends Dialog {
    private Context context;
    private OnDialogClickListener onDialogClickListener;

    ArrayList<DialogListItem> itemList;
    RecyclerView list;

    public DialogList_icon(Context context, ArrayList<DialogListItem> itemList, OnDialogClickListener onDialogClickListener) {
        super(context, R.style.DefaultDialog_anim);

        this.context = context;
        this.onDialogClickListener = onDialogClickListener;

        this.itemList = itemList;

        setCancelable(true); //是否允許用返回鍵關閉Dialog
        setCanceledOnTouchOutside(true);

        initView();

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        onWindowAttributesChanged(layoutParams);


        Window window = getWindow();
        //window.getAttributes().windowAnimations = R.anim.anim_dialog;
    }

    public void initView(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null);
        setContentView(view);
        list = view.findViewById(R.id.list);
        list.setAdapter(new ListAdapter(itemList));
        list.setLayoutManager(new LinearLayoutManager(context));
    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
        ArrayList<DialogListItem> itemList;

        ListAdapter(ArrayList<DialogListItem> itemList) {
            this.itemList = itemList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            View ll_cell;
            ImageView img_icon;
            TextView tv_item;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ll_cell = itemView.findViewById(R.id.ll_cell);
                img_icon = itemView.findViewById(R.id.img_icon);
                tv_item = itemView.findViewById(R.id.tv_item);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_dialog_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, final int position) {
            holder.ll_cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDialogClickListener.onDialogClick(position);
                    dismiss();
                }
            });
            holder.img_icon.setImageResource(itemList.get(position).getIconId());
            holder.tv_item.setText(itemList.get(position).getItem());
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }
}
