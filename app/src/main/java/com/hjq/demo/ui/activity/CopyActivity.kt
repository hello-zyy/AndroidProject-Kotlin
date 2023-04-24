package com.hjq.demo.ui.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.hjq.demo.R
import com.hjq.demo.app.AppActivity
import com.hjq.demo.ui.utils.UserProductUtils
import com.hjq.demo.ui.utils.WpkSPUtil

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 可进行拷贝的副本
 */
class CopyActivity : AppActivity() {

    private val mCheckIv: ImageView? by lazy { findViewById(R.id.iv_check) }
    private val mEditProductRow: EditText? by lazy { findViewById(R.id.ed_product_row) }
    private val mDoneTv: TextView? by lazy { findViewById(R.id.tv_done) }

    override fun getLayoutId(): Int {
        return R.layout.copy_activity
    }

    override fun initView() {
        setNextEnable()
        intListener()
    }


    private fun intListener() {
        refreshEditStatus()
        mCheckIv?.setOnClickListener {
            val isAllowEdit =
                WpkSPUtil.getString(WpkSPUtil.WPK_HOME_EDIT_STATUS, UserProductUtils.TRUE_STR)
            if (TextUtils.equals(UserProductUtils.TRUE_STR, isAllowEdit)) {
                WpkSPUtil.put(WpkSPUtil.WPK_HOME_EDIT_STATUS, UserProductUtils.FALSE_STR)
            } else {
                WpkSPUtil.put(WpkSPUtil.WPK_HOME_EDIT_STATUS, UserProductUtils.TRUE_STR)
            }
            refreshEditStatus()
        }
        mEditProductRow?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //DO NOTHING
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //DO NOTHING
            }

            override fun afterTextChanged(s: Editable?) {
                setNextEnable()
            }
        })
        mDoneTv?.setOnClickListener {
            val rowsStr = mEditProductRow?.text.toString().trim()
            WpkSPUtil.put(WpkSPUtil.WPK_HOME_UI_ROW, rowsStr)
            toast("设置首页UI成功,快去查看吧")
            mEditProductRow?.apply {
                UserProductUtils.hideSoftInput(this);
            }
        }
    }

    private fun refreshEditStatus() {
        val isAllowEdit =
            WpkSPUtil.getString(WpkSPUtil.WPK_HOME_EDIT_STATUS, UserProductUtils.TRUE_STR)
        if (TextUtils.equals(UserProductUtils.TRUE_STR, isAllowEdit)) {
            mCheckIv?.setImageResource(R.drawable.wpk_square_check_icon)
        } else {
            mCheckIv?.setImageResource(R.drawable.wpk_square_uncheck_icon)
        }
    }

    override fun initData() {
        //DO NOTINGS
    }

    fun setNextEnable() {
        val rowStr = mEditProductRow?.text.toString().trim()
        val isEnable = !TextUtils.isEmpty(rowStr)
        mDoneTv?.setBackgroundResource(if (isEnable) R.drawable.user_round_btn_purple else R.drawable.user_round_btn_gray)
        mDoneTv?.setTextColor(
            if (isEnable) UserProductUtils.getColor(R.color.white) else UserProductUtils.getColor(
                R.color.text_color_788A8F
            )
        )
        mDoneTv?.setEnabled(isEnable)
    }

}