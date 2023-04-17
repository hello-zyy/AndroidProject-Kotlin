package com.hjq.demo.ui.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hjq.demo.R;


/**
 * author : Zyy
 * e-mail : yongyong.zhou@wyze.cn
 * date   : 2020/12/4
 */
@SuppressWarnings({"unused", "RedundantSuppression"})
public class UserHintDialog extends Dialog {
    private final Context context;
    private ClickListenerInterface clickListenerInterface;
    private TextView mTitleNameTv;
    private TextView mCancelTv;
    private TextView mSaveTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    public UserHintDialog(@NonNull Context context) {
        super(context, R.style.dialog_common);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = context;
    }

    private void initUI() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.user_hint_dialog, null);
        setContentView(view);
        mTitleNameTv = view.findViewById(R.id.tv_title);
        mCancelTv = view.findViewById(R.id.tv_cancel);
        mSaveTv = view.findViewById(R.id.tv_done);
        mCancelTv.setOnClickListener(new ClickListener());
        mSaveTv.setOnClickListener(new ClickListener());
        Window dialogWindow = getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = UserProductUtils.Companion.dp2px(270);
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);
        dialogWindow.setAttributes(lp);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    public interface ClickListenerInterface {
        void doConfirm();

        void doCancel();
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_cancel) {
                clickListenerInterface.doCancel();
            } else if (id == R.id.tv_done) {
                clickListenerInterface.doConfirm();
            }
        }
    }

    public void setSaveTvColor(int color) {
        if (mSaveTv != null) {
            mSaveTv.setTextColor(color);
        }
    }

    public void setCancelTvColor(int color) {
        if (mCancelTv != null) {
            mCancelTv.setTextColor(color);
        }
    }

    public void setContentColor(int color) {
        if (mTitleNameTv == null) {
            return;
        }
        mTitleNameTv.setTextColor(color);
    }

    public void setCancelName(String cancelName) {
        mCancelTv.setText(cancelName);
    }

    public void setTitleName(String mTitleName) {
        mTitleNameTv.setText(mTitleName);
    }

    public void setConfirmName(String confirmName) {
        mSaveTv.setText(confirmName);
    }

}
