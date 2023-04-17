package com.hjq.demo.ui.utils;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


@SuppressWarnings("java:S1104")
public class BaseFragment extends Fragment {
    public String mPageId = "";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!titleBarSettled()) {
        }

        if (null != savedInstanceState) {
            mPageId = savedInstanceState.getString("iid");
        }
    }

    protected boolean titleBarSettled() {
        return false;
    }

    // fragment是否需要消耗/处理back事件.true是指消耗,false不需要处理
    public boolean onBackPressed() {
        return false;
    }

    // viewpager可以在OnPageChangeListener中调用该方法
    public void onPageSelected() {
        //viewpager可以在OnPageChangeListener中调用该方法
    }

    public FragmentActivity getValidActivity() {
        FragmentActivity a = super.getActivity();
        if (a == null || a.isFinishing()) {
            return null;
        }

        if (a.isDestroyed()) {
            return null;
        }

        return a;
    }

    public boolean isValid() {
        return isAdded() && !isDetached() && getValidActivity() != null;
    }
}
