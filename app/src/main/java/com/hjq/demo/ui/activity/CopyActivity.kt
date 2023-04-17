package com.hjq.demo.ui.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
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

    private val mEditProductRow: EditText? by lazy { findViewById(R.id.ed_product_row) }
    private val mEditProductColumns: EditText? by lazy { findViewById(R.id.ed_product_columns) }
    private val mDoneTv: TextView? by lazy { findViewById(R.id.tv_done) }

    override fun getLayoutId(): Int {
        return R.layout.copy_activity
    }

    override fun initView() {
        setNextEnable()
        intListener()
    }


    private fun intListener() {
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
        mEditProductColumns?.addTextChangedListener(object : TextWatcher {
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
            val rowStr = mEditProductRow?.text.toString().trim()
            val columnsStr = mEditProductColumns?.text.toString().trim()
            WpkSPUtil.put(WpkSPUtil.WPK_HOME_UI_ROW, rowStr)
            WpkSPUtil.put(WpkSPUtil.WPK_HOME_UI_COLUMNS, columnsStr)
            toast("设置首页UI成功,快去查看吧")
            mEditProductColumns?.apply {
                UserProductUtils.hideSoftInput(this);
            }
        }
    }

    override fun initData() {
        //DO NOTINGS
    }

    fun setNextEnable() {
        val describeStr = mEditProductColumns?.text.toString().trim()
        val labelStr = mEditProductRow?.text.toString().trim()
        val isEnable = !TextUtils.isEmpty(describeStr) && !TextUtils.isEmpty(labelStr)
        mDoneTv?.setBackgroundResource(if (isEnable) R.drawable.user_round_btn_purple else R.drawable.user_round_btn_gray)
        mDoneTv?.setTextColor(
            if (isEnable) UserProductUtils.getColor(R.color.white) else UserProductUtils.getColor(
                R.color.text_color_788A8F
            )
        )
        mDoneTv?.setEnabled(isEnable)
    }

}