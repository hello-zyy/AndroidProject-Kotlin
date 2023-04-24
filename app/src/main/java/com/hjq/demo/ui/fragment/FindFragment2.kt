package com.hjq.demo.ui.fragment

import android.annotation.SuppressLint
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.hjq.demo.R
import com.hjq.demo.app.TitleBarFragment
import com.hjq.demo.ui.activity.HomeActivity
import com.hjq.demo.ui.activity.ImageSelectActivity
import com.hjq.demo.ui.adapter.HomeLabelAdapter
import com.hjq.demo.ui.obj.UserProductInfo
import com.hjq.demo.ui.utils.*

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 发现 Fragment
 */
class FindFragment2 : TitleBarFragment<HomeActivity>() {

    companion object {
        const val TAG = "FindFragment2"
        fun newInstance(): FindFragment2 {
            return FindFragment2()
        }
    }

    private val mRootItem: ConstraintLayout? by lazy { findViewById(R.id.cl_add_root) }
    private val mScrollView: NestedScrollView? by lazy { findViewById(R.id.scroll_view) }
    private val mAddInfoRl: RelativeLayout? by lazy { findViewById(R.id.rl_add_info) }
    private val mTitleBar: TextView? by lazy { findViewById(R.id.title_bar) }
    private val mProductPrice: WpkHintFlyEditText? by lazy { findViewById(R.id.ed_product_price) }
    private val mMemberPrice: WpkHintFlyEditText? by lazy { findViewById(R.id.ed_member_price) }
    private val mAddProductRl: RelativeLayout? by lazy { findViewById(R.id.rl_add_product) }
    private val mSelectIconIv: ImageView? by lazy { findViewById(R.id.iv_select_icon) }
    private val mEditDescribeEd: WpkHintFlyEditText? by lazy { findViewById(R.id.ed_product_describe) }
    private val mEditLabelEd: WpkHintFlyEditText? by lazy { findViewById(R.id.ed_product_label) }
    private val mEditSecondaryLabelEd: WpkHintFlyEditText? by lazy { findViewById(R.id.ed_secondary_tab) }
    private val mLabelList: RecyclerView? by lazy { findViewById(R.id.rv_label) }
    private val mLabelList2: RecyclerView? by lazy { findViewById(R.id.rv_label2) }
    private val mDoneTv: TextView? by lazy { findViewById(R.id.tv_done) }
    private var imgeUrl: String = ""
    private lateinit var labelAdapter: HomeLabelAdapter
    private lateinit var labelAdapter2: HomeLabelAdapter
    private var isSelectPhoto = false

    override fun getLayoutId(): Int {
        return R.layout.find_fragment2
    }

    override fun initView() {
        setNextEnable()
        initRecyclerView()
        initRecyclerView2()
        initListener()
        initListener2()
        setListenerFotEditText()
    }

    override fun initData() {
        setTitleBar()
        initView()
    }


    //主标签 adapter
    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView() {
        activity?.apply {
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            labelAdapter = HomeLabelAdapter(this)
            mLabelList?.layoutManager = layoutManager
        }
        val labelList = WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_LABEL, String::class.java)
        mLabelList?.adapter = labelAdapter
        labelAdapter.setData(labelList)
        labelAdapter.setSelectLabel(UserProductUtils.selectLabel)
        labelAdapter.notifyDataSetChanged()
    }

    //次标签 adapter
    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView2() {
        activity?.apply {
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            labelAdapter2 = HomeLabelAdapter(this)
            mLabelList2?.layoutManager = layoutManager
        }
        val labelList =
            WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_SECONDARY_LABEL, String::class.java)
        mLabelList2?.adapter = labelAdapter2
        labelAdapter2.setData(labelList)
        labelAdapter2.setSelectLabel(UserProductUtils.selectLabel)
        labelAdapter2.notifyDataSetChanged()
    }

    private fun initListener() {
        labelAdapter.setListener(object : HomeLabelAdapter.OnAdapterListener {
            override fun onClickAction(labelStr: String?) {
                labelStr?.apply {
                    mEditLabelEd?.setText(this)
                    mEditLabelEd?.setSelection(length)
                }
            }

            override fun onLongClickAction(labelStr: String?) {
                deleteProductLabel(labelStr)//删除主标签
            }
        })
        labelAdapter2.setListener(object : HomeLabelAdapter.OnAdapterListener {
            override fun onClickAction(labelStr: String?) {
                labelStr?.apply {
                    mEditSecondaryLabelEd?.setText(labelStr)
                    mEditSecondaryLabelEd?.setSelection(length)
                }
            }

            override fun onLongClickAction(labelStr: String?) {
                deleteSecondaryLabel(labelStr)//删除次标签
            }
        })
    }

    private fun initListener2() {
        mAddProductRl?.setOnClickListener {
            ImageSelectActivity.start(getAttachActivity()!!, object :
                ImageSelectActivity.OnPhotoSelectListener {
                override fun onSelected(data: MutableList<String>) {
                    isSelectPhoto = true
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
                    isSelectPhoto = true
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
        mProductPrice?.addTextChangedListener(object : TextWatcher {
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
        mMemberPrice?.addTextChangedListener(object : TextWatcher {
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
        mEditSecondaryLabelEd?.addTextChangedListener(object : TextWatcher {
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
            val describeStr = mEditDescribeEd?.text.toString().trim() //描述
            val priceStr = mProductPrice?.text.toString().trim()//原价
            val memberPriceStr = mMemberPrice?.text.toString().trim()//会员价
            val labelStr = mEditLabelEd?.text.toString().trim()//大标签
            val secondaryLabelStr = mEditSecondaryLabelEd?.text.toString().trim()//小标签

            //主标签
            val labelList = WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_LABEL, String::class.java)
            if (labelList.isEmpty() || !labelList.contains(labelStr)) {
                labelList.add(labelStr)
                WpkSPUtil.putListData(WpkSPUtil.WPK_PRODUCT_LABEL, labelList)
            }
            //All所有小标签
            val secondaryLabelList =
                WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_SECONDARY_LABEL, String::class.java)
            if (secondaryLabelList.isEmpty() || !secondaryLabelList.contains(secondaryLabelStr)) {
                secondaryLabelList.add(secondaryLabelStr)
                WpkSPUtil.putListData(WpkSPUtil.WPK_PRODUCT_SECONDARY_LABEL, secondaryLabelList)
            }

            //某一个主标签拥有的次标签列表
            val secondaryLabelOfHostLabel =
                WpkSPUtil.getListData(
                    UserProductUtils.getProductSecondaryLabelSp(labelStr),
                    String::class.java
                )
            if (secondaryLabelOfHostLabel.isEmpty()
                || !secondaryLabelOfHostLabel.contains(secondaryLabelStr)
            ) {
                secondaryLabelOfHostLabel.add(secondaryLabelStr)
                WpkSPUtil.putListData(
                    UserProductUtils.getProductSecondaryLabelSp(labelStr),
                    secondaryLabelOfHostLabel
                )
            }

            val productListData =
                WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_INFO, UserProductInfo::class.java)
            productListData.add(
                UserProductInfo(
                    labelStr,
                    secondaryLabelStr,
                    describeStr,
                    priceStr,
                    memberPriceStr,
                    imgeUrl
                )
            )
            WpkSPUtil.putListData(WpkSPUtil.WPK_PRODUCT_INFO, productListData)
            toast("发布成功了,快去首页查看吧")
            resetStatus()
            mEditLabelEd?.apply {
                UserProductUtils.hideSoftInput(this)
            }
        }
    }

    //keyboard Listener
    private fun setListenerFotEditText() {
        WpkSoftKeyBoardListeners.setListener(
            activity,
            object : WpkSoftKeyBoardListeners.OnSoftKeyBoardChangeListener {
                @SuppressLint("LogNotTimber")
                override fun keyBoardShow(height: Int) {
                    Log.i(TAG, "keyboard height: $height")
                    softKeyboardOpened(height) //keyboardOpend
                }

                override fun keyBoardHide(height: Int) {
                    softKeyboardClosed() //keyboard closed
                }
            })
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

    fun setNextEnable() {
        val describeStr = mEditDescribeEd?.text.toString().trim()
        val priceStr = mProductPrice?.text.toString().trim()
        val memberPriceStr = mMemberPrice?.text.toString().trim()
        val labelStr = mEditLabelEd?.text.toString().trim()
        val isEnable =
            isSelectPhoto
                    && !TextUtils.isEmpty(describeStr)
                    && !TextUtils.isEmpty(priceStr)
                    && !TextUtils.isEmpty(memberPriceStr)
                    && !TextUtils.isEmpty(labelStr)
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
        isSelectPhoto = false
        mAddProductRl?.visibility = View.VISIBLE
        mSelectIconIv?.visibility = View.GONE
        mSelectIconIv?.setImageResource(0)
        mEditDescribeEd?.setHintText(
            UserProductUtils.getResources().getString(R.string.product_info)
        )
        mProductPrice?.setHintText(
            UserProductUtils.getResources().getString(R.string.product_price)
        )
        mMemberPrice?.setHintText(UserProductUtils.getResources().getString(R.string.member_price))
        mEditLabelEd?.setHintText(UserProductUtils.getResources().getString(R.string.product_label))
        mEditSecondaryLabelEd?.setHintText(
            UserProductUtils.getResources().getString(R.string.product_small_label)
        )
        mEditDescribeEd?.setText("")
        mProductPrice?.setText("")
        mMemberPrice?.setText("")
        mEditLabelEd?.setText("")
        mEditSecondaryLabelEd?.setText("")
        mAddProductRl?.isFocusable = true
        mAddProductRl?.isFocusableInTouchMode = true
        mAddProductRl?.requestFocus()
        mScrollView?.scrollTo(0, 0)
        initView()
    }

    //keyboard opened
    private fun softKeyboardOpened(height: Int) {
        Handler().post {
            val lp = mAddInfoRl?.layoutParams as FrameLayout.LayoutParams
            lp.bottomMargin = UserProductUtils.dp2px(100f) + height
            mAddInfoRl?.layoutParams = lp
        }
    }

    //keyboard closed
    private fun softKeyboardClosed() {
        Handler().post {
            val lp = mAddInfoRl?.layoutParams as FrameLayout.LayoutParams
            lp.bottomMargin = UserProductUtils.dp2px(100f)
            mAddInfoRl?.layoutParams = lp
        }
    }

    //删除主标签
    private fun deleteProductLabel(mLabel: String?) {
        val isAllowEdit =
            WpkSPUtil.getString(WpkSPUtil.WPK_HOME_EDIT_STATUS, UserProductUtils.TRUE_STR)
        if (TextUtils.equals(isAllowEdit, UserProductUtils.FALSE_STR)) {
            return
        }
        if (TextUtils.equals(mLabel, UserProductUtils.RECOMMENDED_PRODUCT)) {
            toast("此类型不允许删除")
            return
        }
        activity?.apply {
            val dialog = UserHintDialog(this)
            dialog.setClicklistener(object : UserHintDialog.ClickListenerInterface {
                override fun doConfirm() {
                    dialog.dismiss()
                    UserProductUtils.deleteProductLableByInfo(mLabel)
                    initData()
                }

                override fun doCancel() {
                    dialog.dismiss()
                }
            })
            dialog.show()
            dialog.setTitleName("是否要删除此类型？")
        }
    }

    //删除次标签
    private fun deleteSecondaryLabel(mLabel: String?) {
        val isAllowEdit =
            WpkSPUtil.getString(WpkSPUtil.WPK_HOME_EDIT_STATUS, UserProductUtils.TRUE_STR)
        if (TextUtils.equals(isAllowEdit, UserProductUtils.FALSE_STR)) {
            return
        }
        activity?.apply {
            val dialog = UserHintDialog(this)
            dialog.setClicklistener(object : UserHintDialog.ClickListenerInterface {
                override fun doConfirm() {
                    dialog.dismiss()
                    UserProductUtils.deleteSecondaryLableByInfo(mLabel)
                    initData()
                }

                override fun doCancel() {
                    dialog.dismiss()
                }
            })
            dialog.show()
            dialog.setTitleName("是否要删除此类型？")
        }
    }
}