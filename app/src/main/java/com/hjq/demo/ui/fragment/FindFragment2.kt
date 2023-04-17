package com.hjq.demo.ui.fragment

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.hjq.demo.R
import com.hjq.demo.app.TitleBarFragment
import com.hjq.demo.ui.activity.HomeActivity
import com.hjq.demo.ui.activity.ImageSelectActivity
import com.hjq.demo.ui.obj.UserProductInfo
import com.hjq.demo.ui.utils.UserProductUtils
import com.hjq.demo.ui.utils.UserTitleBarUtil
import com.hjq.demo.ui.utils.WpkSPUtil

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 发现 Fragment
 */
class FindFragment2 : TitleBarFragment<HomeActivity>() {

    companion object {

        fun newInstance(): FindFragment2 {
            return FindFragment2()
        }
    }

    private val mRootItem: ConstraintLayout? by lazy { findViewById(R.id.cl_add_root) }
    private val mTitleBar: TextView? by lazy { findViewById(R.id.title_bar) }
    private val mAddProductRl: RelativeLayout? by lazy { findViewById(R.id.rl_add_product) }
    private val mSelectIconIv: ImageView? by lazy { findViewById(R.id.iv_select_icon) }
    private val mEditDescribeEd: EditText? by lazy { findViewById(R.id.ed_product_describe) }
    private val mEditLabelEd: EditText? by lazy { findViewById(R.id.ed_product_label) }
    private val mDoneTv: TextView? by lazy { findViewById(R.id.tv_done) }
    private var imgeUrl: String = ""

    override fun getLayoutId(): Int {
        return R.layout.find_fragment2
    }

    override fun initView() {
        setNextEnable()
        mAddProductRl?.setOnClickListener {
            ImageSelectActivity.start(getAttachActivity()!!, object :
                ImageSelectActivity.OnPhotoSelectListener {
                override fun onSelected(data: MutableList<String>) {
                    refreshSelectIcon(data)
                }

                override fun onCancel() {
                    toast("取消了")
                }
            })
        }
        mSelectIconIv?.setOnClickListener {
            ImageSelectActivity.start(getAttachActivity()!!, object :
                ImageSelectActivity.OnPhotoSelectListener {
                override fun onSelected(data: MutableList<String>) {
                    refreshSelectIcon(data)
                }

                override fun onCancel() {
                    toast("取消了")
                }
            })
        }
        mEditDescribeEd?.addTextChangedListener(object : TextWatcher {
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
        mEditLabelEd?.addTextChangedListener(object : TextWatcher {
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
            val describeStr = mEditDescribeEd?.text.toString().trim()
            val labelStr = mEditLabelEd?.text.toString().trim()
            val labelList = WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_LABEL, String::class.java)
            val productListData =
                WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_INFO, UserProductInfo::class.java)
            if (labelList.isEmpty() || !labelList.contains(labelStr)) {
                labelList.add(labelStr)
                WpkSPUtil.putListData(WpkSPUtil.WPK_PRODUCT_LABEL, labelList)
            }
            productListData.add(
                UserProductInfo(
                    labelStr,
                    describeStr,
                    imgeUrl
                )
            )
            WpkSPUtil.putListData(WpkSPUtil.WPK_PRODUCT_INFO, productListData)
            toast("发布成功了,快去首页查看吧")
            resetStatus()
            mEditLabelEd?.apply {
                UserProductUtils.hideSoftInput(this);
            }
        }
    }

    private fun refreshSelectIcon(data: MutableList<String>) {
        activity?.apply {
            mSelectIconIv?.apply {
                imgeUrl = data[0]
                Glide.with(this@FindFragment2)
                    .load(data[0])
                    .error(R.mipmap.launcher_ic)
                    .transform(
                        MultiTransformation(
                            CenterCrop(),
                            RoundedCorners(resources.getDimension(R.dimen.dp_10).toInt())
                        )
                    )
                    .into(this)
            }
            if (data.isNotEmpty()) {
                mAddProductRl?.visibility = View.GONE
                mSelectIconIv?.visibility = View.VISIBLE
            }
        }
    }

    override fun initData() {
        setTitleBar()
    }

    fun setNextEnable() {
        val describeStr = mEditDescribeEd?.text.toString().trim()
        val labelStr = mEditLabelEd?.text.toString().trim()
        val isEnable = !TextUtils.isEmpty(describeStr) && !TextUtils.isEmpty(labelStr)
        mDoneTv?.setBackgroundResource(if (isEnable) R.drawable.user_round_btn_purple else R.drawable.user_round_btn_gray)
        mDoneTv?.setTextColor(
            if (isEnable) UserProductUtils.getColor(R.color.white) else UserProductUtils.getColor(
                R.color.text_color_788A8F
            )
        )
        mDoneTv?.isEnabled = isEnable
    }

    private fun setTitleBar() {
        val statusHeight = UserTitleBarUtil.getStatusHeight()
        mTitleBar?.apply {
            val set = ConstraintSet()
            set.clone(activity, R.layout.find_fragment2)
            set.constrainHeight(id, statusHeight + UserProductUtils.dp2px(40f))
            set.applyTo(mRootItem)
        }
    }

    private fun resetStatus() {
        mAddProductRl?.visibility = View.VISIBLE
        mSelectIconIv?.visibility = View.GONE
        mSelectIconIv?.setImageResource(0)
        mEditDescribeEd?.setText("")
        mEditLabelEd?.setText("")
        mEditDescribeEd?.isFocusable = true
        mEditDescribeEd?.isFocusableInTouchMode = true
        mEditDescribeEd?.requestFocus()
    }

}