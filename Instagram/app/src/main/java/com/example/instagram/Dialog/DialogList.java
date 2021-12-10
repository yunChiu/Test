package com.example.instagram.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;

import java.util.ArrayList;

public class DialogList extends Dialog {
    private Context context;
    private OnDialogClickListener onDialogClickListener;

    ArrayList<String> itemList;
    RecyclerView list;

    public DialogList(Context context, ArrayList<String> itemList, OnDialogClickListener onDialogClickListener) {
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
    }

    private void initView(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null);
        setContentView(view);
        list = view.findViewById(R.id.list);
        list.setAdapter(new ListAdapter(itemList));
        list.setLayoutManager(new LinearLayoutManager(context));
    }

    public class ListAdapter extends RecyclerView.Adapter<DialogList.ListAdapter.ViewHolder> {
        ArrayList<String> itemList;

        ListAdapter(ArrayList<String> itemList) {
            this.itemList = itemList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_item;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_item = itemView.findViewById(R.id.tv_item);
            }
        }

        @NonNull
        @Override
        public DialogList.ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_dialoglist, parent, false);
            return new DialogList.ListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DialogList.ListAdapter.ViewHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDialogClickListener.onDialogClick(position);
                    dismiss();
                }
            });
            holder.tv_item.setText(itemList.get(position));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }
}
