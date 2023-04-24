package com.hjq.demo.ui.fragment

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hjq.demo.R
import com.hjq.demo.app.TitleBarFragment
import com.hjq.demo.ui.activity.HomeActivity
import com.hjq.demo.ui.activity.ImageSelectActivity
import com.hjq.demo.ui.utils.UserProductUtils
import com.hjq.demo.ui.utils.UserTitleBarUtil
import com.hjq.demo.ui.utils.WpkSPUtil

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 消息 Fragment
 */
class MessageFragment2 : TitleBarFragment<HomeActivity>() {

    companion object {

        fun newInstance(): MessageFragment2 {
            return MessageFragment2()
        }
    }

    private val mTitleBar: TextView? by lazy { findViewById(R.id.title_bar) }
    private val mAddInfo: RelativeLayout? by lazy { findViewById(R.id.rl_add_info) }
    private val mTitleRight: TextView? by lazy { findViewById(R.id.title_right) }
    private val mSelectIconIv: ImageView? by lazy { findViewById(R.id.iv_select_icon) }
    private var imgeUrl: String = ""

    override fun getLayoutId(): Int {
        return R.layout.message_fragment2
    }

    override fun initView() {
        val listData = WpkSPUtil.getListData(WpkSPUtil.WPK_PRICE_LIST_IMAGE, String::class.java)
        if (listData.isNotEmpty()) {
            mAddInfo?.visibility = View.GONE
            refreshSelectIcon(listData)
        }
        mTitleRight?.setOnClickListener {
            ImageSelectActivity.start(getAttachActivity()!!, object :
                ImageSelectActivity.OnPhotoSelectListener {
                override fun onSelected(data: MutableList<String>) {
                    WpkSPUtil.putListData(WpkSPUtil.WPK_PRICE_LIST_IMAGE, data) //行数
                    refreshSelectIcon(data)
                }

                override fun onCancel() {
                    toast("取消了")
                }
            })
        }
        mAddInfo?.setOnClickListener {
            ImageSelectActivity.start(getAttachActivity()!!, object :
                ImageSelectActivity.OnPhotoSelectListener {
                override fun onSelected(data: MutableList<String>) {
                    mAddInfo?.visibility = View.GONE
                    WpkSPUtil.putListData(WpkSPUtil.WPK_PRICE_LIST_IMAGE, data) //行数
                    refreshSelectIcon(data)
                }

                override fun onCancel() {
                    toast("取消了")
                }
            })
        }
    }

    private fun refreshSelectIcon(data: MutableList<String>) {
        activity?.apply {
            mSelectIconIv?.apply {
                imgeUrl = data[0]
                Glide.with(this@MessageFragment2)
                    .load(data[0])
                    .error(R.mipmap.launcher_ic)
                    .into(this)
            }
            if (data.isNotEmpty()) {
                mSelectIconIv?.visibility = View.VISIBLE
            }
        }
    }

    override fun initData() {
        setTitleBar()
    }

    private fun setTitleBar() {
        val statusHeight = UserTitleBarUtil.getStatusHeight()
        mTitleBar?.apply {
            val lp = this.layoutParams
            lp.height = statusHeight + UserProductUtils.dp2px(40f)
            layoutParams = lp
        }
        mTitleRight?.apply {
            val lp = this.layoutParams
            lp.height = statusHeight + UserProductUtils.dp2px(40f)
            layoutParams = lp
        }
    }

}