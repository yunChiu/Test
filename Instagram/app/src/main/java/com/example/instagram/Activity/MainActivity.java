package com.example.instagram.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.Dialog.DialogBtn;
import com.example.instagram.Dialog.DialogList_icon;
import com.example.instagram.Dialog.OnDialogClickListener;
import com.example.instagram.R;
import com.example.instagram.model.DialogListItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_main);

    }

    /** Node **/
    public void setLike(int count, TextView textView) {
        if (count == 0)
            textView.setVisibility(View.GONE);
        else
            textView.setText(String.format("%d個讚", count));
    }

    public void setText(String name, String text, TextView textView) {
        StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);
        String userMsg = String.format("%s %s", name, text);
        SpannableString userMsgStyle = new SpannableString(userMsg);
        userMsgStyle.setSpan(bold, 0, name.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(userMsgStyle);
    }

    public void setComment(int count, TextView textView) {
        if (count == 0)
            textView.setVisibility(View.GONE);
        else
            textView.setText(String.format("查看全部%s則留言", count));
    }

    /** Dialog **/
    public void showDialogBtn_more(final Context context, final String copyLink, final String shareLink) {
        final DialogBtn dialogBtn = new DialogBtn(context, new OnDialogClickListener() {
            @Override
            public void onDialogClick(int action) {
                switch (action){
                    case 0:
                        //複製連結
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("text", copyLink);
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(context,"已複製連結",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //分享
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, shareLink);
                        intent.setType("text/plain");
                        startActivity(Intent.createChooser(intent,"IG Sharing"));
                        break;
                    case 2:
                        //檢舉
                        break;
                    case 3:
                        //為什麼
                        break;
                    case 4:
                        //隱藏
                        break;
                    case 5:
                        //退追
                        break;
                    default:
                        break;
                }
            }
        });
        dialogBtn.show();
    }

    public void showDialogList_menu(Context context) {
        ArrayList<DialogListItem> userMenu = new ArrayList<>();
//        node_more.add(new DialogListItem(R.drawable.setting,"設定"));
        DialogList_icon dialogListIcon = new DialogList_icon(context, userMenu, new OnDialogClickListener() {
            @Override
            public void onDialogClick(int action) {

            }
        });
        dialogListIcon.show();
    }


}