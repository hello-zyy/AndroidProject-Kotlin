package com.hjq.demo.ui.fragment

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.hjq.demo.R
import com.hjq.demo.app.TitleBarFragment
import com.hjq.demo.ui.activity.HomeActivity
import com.hjq.demo.ui.adapter.HomeLabelAdapter
import com.hjq.demo.ui.obj.UserProductInfo
import com.hjq.demo.ui.utils.*

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 首页 Fragment
 */
@SuppressLint("NotifyDataSetChanged")
class HomeFragment2 : TitleBarFragment<HomeActivity>() {

    companion object {
        fun newInstance(): HomeFragment2 {
            return HomeFragment2()
        }
    }

    private val mRootItem: ConstraintLayout? by lazy { findViewById(R.id.cl_home_root) }
    private val rlAddProduct: RelativeLayout? by lazy { findViewById(R.id.rl_add_product) }
    private val viewPager: MyViewPager? by lazy { findViewById(R.id.vp_page) }
    private val rvLabelList: RecyclerView? by lazy { findViewById(R.id.rv_label) }
    private lateinit var proudctList: List<UserProductInfo>
    private var curPositon = 0
    private lateinit var labelAdapter: HomeLabelAdapter

    override fun getLayoutId(): Int {
        return R.layout.home_fragment2
    }

    override fun initView() {
        //首次初始化配置
        val labelList = WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_LABEL, String::class.java)
        if (labelList.isEmpty()) {
            labelList.add(UserProductUtils.RECOMMENDED_PRODUCT)
        }
        WpkSPUtil.putListData(WpkSPUtil.WPK_PRODUCT_LABEL, labelList)

        val rowNumber =
            WpkSPUtil.getString(WpkSPUtil.WPK_HOME_UI_ROW, WpkSPUtil.WPK_HOME_UI_ROW) //行数
        if (TextUtils.isEmpty(rowNumber.toString())) {
            WpkSPUtil.put(WpkSPUtil.WPK_HOME_UI_ROW, "2")
        }
        val columnsNumber =
            WpkSPUtil.getString(WpkSPUtil.WPK_HOME_UI_COLUMNS, WpkSPUtil.WPK_HOME_UI_COLUMNS)//列
        if (TextUtils.isEmpty(columnsNumber.toString())) {
            WpkSPUtil.put(WpkSPUtil.WPK_HOME_UI_ROW, "2")
        }
        UserProductUtils.selectLabel = UserProductUtils.RECOMMENDED_PRODUCT //默认进来选中推荐Tab
    }

    override fun initData() {
        val rowNumber = WpkSPUtil.getString(WpkSPUtil.WPK_HOME_UI_ROW, "2") //行数
        val columnsNumber = WpkSPUtil.getString(WpkSPUtil.WPK_HOME_UI_COLUMNS, "2")//列
        UserProductUtils.numberOfHomeImages = (rowNumber.toInt() * columnsNumber.toInt())
        initRecyclerView()
        refreshData()
        initListener()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData() {
        proudctList =
            WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_INFO, UserProductInfo::class.java)
        UserProductUtils.mProductList = proudctList
        initPage()
    }

    private fun initPage() {
        setTitleBar()
        val fragments = ArrayList<Fragment>()
        val localList = ArrayList<UserProductInfo>()
        var mCount = 0
        if (proudctList.isNotEmpty()) {
            for (i in proudctList.indices) {
                val it = proudctList[i]
                localList.add(it)
                if (localList.size > (UserProductUtils.numberOfHomeImages - 1) || i == proudctList.size - 1) {
                    mCount++
                    val childFragment = WyzeProfuctInfoFragment(mCount)
                    fragments.add(childFragment)
                    localList.clear()
                }
            }
        }
        refreshUI()
        val adapter = MyFragmentStatePagerAdapter(
            childFragmentManager, fragments
        )
        viewPager?.offscreenPageLimit =
            1 //其中参数可以设为0或者1，参数小于1时，会默认用1来作为参数，未设置之前，ViewPager会默认加载两个Fragment,左右各1个
        viewPager?.adapter = adapter
        viewPager?.setPageTransformer(true, ZoomOutPageTransformer())
        //初始化位置
        viewPager?.currentItem = curPositon
        viewPager?.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // Do nothing because of override.
            }

            override fun onPageSelected(position: Int) {
                // Do nothing
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Do nothing because of override.

            }
        })
    }

    private fun initListener() {
        labelAdapter.setListener(object : HomeLabelAdapter.OnAdapterListener {
            override fun onClickAction(labelStr: String?) {
                labelStr?.apply {
                    UserProductUtils.selectLabel = this
                    labelAdapter.setSelectLabel(this)
                    labelAdapter.notifyDataSetChanged()
                    proudctList = UserProductUtils.getProductListByLabel()
                    initPage()
                }
            }

            override fun onLongClickAction(labelStr: String?) {
                deleteProductLabel(labelStr)
            }
        })
        rlAddProduct?.setOnClickListener {
            HomeActivity.start(getAttachActivity()!!, FindFragment2::class.java)
        }
    }

    private fun initRecyclerView() {
        activity?.apply {
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            labelAdapter = HomeLabelAdapter(this)
            rvLabelList?.layoutManager = layoutManager
        }
        val labelList = WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_LABEL, String::class.java)
        rvLabelList?.adapter = labelAdapter
        labelAdapter.setData(labelList)
        labelAdapter.setSelectLabel(UserProductUtils.selectLabel)
        labelAdapter.notifyDataSetChanged()
    }

    private fun setTitleBar() {
        val statusHeight = UserTitleBarUtil.getStatusHeight()
        rvLabelList?.apply {
            val set = ConstraintSet()
            set.clone(activity, R.layout.home_fragment2)
            set.setMargin(id, ConstraintSet.TOP, statusHeight)
            set.applyTo(mRootItem)
        }
    }

    private fun refreshUI() {
        val mLocalList =
            WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_INFO, UserProductInfo::class.java)
        rlAddProduct?.visibility = View.GONE
        rvLabelList?.visibility = View.VISIBLE
        if (proudctList.isEmpty()) {
            rlAddProduct?.visibility = View.VISIBLE
        }
        if (proudctList.isEmpty() && mLocalList.isEmpty()) {
            rvLabelList?.visibility = View.GONE
        }
    }

    //删除商品
    private fun deleteProductLabel(mLabel: String?) {
        if (TextUtils.equals(mLabel,UserProductUtils.RECOMMENDED_PRODUCT)){
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
}