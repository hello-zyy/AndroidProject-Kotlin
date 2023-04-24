package com.hjq.demo.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.hjq.demo.R
import com.hjq.demo.app.TitleBarFragment
import com.hjq.demo.ui.activity.HomeActivity
import com.hjq.demo.ui.activity.SeachActivity
import com.hjq.demo.ui.obj.UserProductInfo
import com.hjq.demo.ui.utils.*


/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 首页 Fragment
 */
@SuppressLint("NotifyDataSetChanged")
class HomeFragment3 : TitleBarFragment<HomeActivity>() {

    companion object {
        fun newInstance(): HomeFragment3 {
            return HomeFragment3()
        }
    }

    private val mRootItem: ConstraintLayout? by lazy { findViewById(R.id.cl_home3_root) }
    private val rlAddProduct: RelativeLayout? by lazy { findViewById(R.id.rl_add_product) }
    private val searchIv: ImageView? by lazy { findViewById(R.id.iv_search) }
    private val viewPager: MyViewPager? by lazy { findViewById(R.id.vp_page) }
    private val tabLayout: TabLayout? by lazy { findViewById(R.id.tl_layout) }
    private lateinit var proudctList: List<UserProductInfo>
    private lateinit var labelList: MutableList<String>
    private var curPositon = 0
    private var holder: ViewHolder? = null

    override fun getLayoutId(): Int {
        return R.layout.home_fragment3
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
        UserProductUtils.selectLabel = UserProductUtils.RECOMMENDED_PRODUCT //默认进来选中推荐Tab
    }

    override fun initData() {
        refreshData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData() {
        tabLayout?.setupWithViewPager(viewPager)
        proudctList =
            WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_INFO, UserProductInfo::class.java)
        UserProductUtils.mProductList = proudctList
        labelList = WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_LABEL, String::class.java)
        tabLayout?.removeAllTabs()
        initPage()
        initTabView()
        initListener()
    }

    private fun initPage() {
        setTitleBar()
        val fragments = ArrayList<Fragment>()
        if (labelList.isNotEmpty() && proudctList.isNotEmpty()) {
            labelList.forEach {
                val childFragment = WyzeProfuctInfoFragment()
                fragments.add(childFragment)
            }
        }
        refreshUI()
        val adapter = MyFragmentStatePagerAdapter(
            childFragmentManager, fragments
        )
        viewPager?.offscreenPageLimit =
            0 //其中参数可以设为0或者1，参数小于1时，会默认用1来作为参数，未设置之前，ViewPager会默认加载两个Fragment,左右各1个
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
                tabLayout?.getTabAt(position)?.select();
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Do nothing because of override.
            }
        })
    }

    private fun initTabView() {
        holder = null
        tabLayout?.apply {
            for (i in labelList.indices) {
                val tab = getTabAt(i)
                tab?.apply {
                    //给tab设置自定义布局
                    setCustomView(R.layout.user_label_item)
                    holder = ViewHolder(this.customView!!)
                    //填充数据
                    holder!!.mTabItemTv.text = labelList.get(i)
                    //默认选择第一项
                    if (i == 0) {
                        UserProductUtils.selectLabel = labelList.get(i)
                        updateTabView(holder, isSelected)
                        holder!!.mTabItemTv.isSelected = true
                        holder!!.mTabItemTv.textSize = 18f
                        holder!!.mTabLine.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun initListener() {
        searchIv?.setOnClickListener {
            startActivity(Intent(activity, SeachActivity::class.java))
        }
        rlAddProduct?.setOnClickListener {
            HomeActivity.start(getAttachActivity()!!, FindFragment2::class.java)
        }
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.customView?.apply {
                    holder = ViewHolder(this)
                    UserProductUtils.selectLabel = labelList[tab.position]
                    updateTabView(holder, true)
                    //关联Viewpager
                    viewPager?.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                holder = ViewHolder(tab.customView!!)
                updateTabView(holder, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                //DO THINGS
            }
        })
    }

    //刷新Tab Item status
    private fun updateTabView(holder: ViewHolder?, selected: Boolean) {
        holder?.apply {
            if (selected) {
                mTabItemTv.isSelected = true
                //设置选中后的字体大小
                mTabItemTv.textSize = 18f
                mTabItemTv.setTextColor(UserProductUtils.getColor(R.color.text_color_353535))
                mTabLine.visibility = View.VISIBLE
            } else {
                mTabItemTv.isSelected = false
                //恢复默认字体大小
                mTabItemTv.textSize = 16f
                mTabLine.visibility = View.GONE
                mTabItemTv.setTextColor(UserProductUtils.getColor(R.color.text_color_9c9c9c))
            }
        }
    }

    internal inner class ViewHolder(tabView: View) {
        var mTabItemTv: TextView = tabView.findViewById<View>(R.id.tv_label) as TextView
        var mTabLine: View = tabView.findViewById(R.id.iv_select_line)
    }

    private fun setTitleBar() {
        val statusHeight = UserTitleBarUtil.getStatusHeight()
        tabLayout?.apply {
            val set = ConstraintSet()
            set.clone(activity, R.layout.home_fragment3)
            set.setMargin(id, ConstraintSet.TOP, statusHeight)
            searchIv?.apply {
                set.setMargin(id, ConstraintSet.TOP, statusHeight + UserProductUtils.dp2px(10f))
            }
            set.applyTo(mRootItem)
        }
    }

    private fun refreshUI() {
        val mLocalList =
            WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_INFO, UserProductInfo::class.java)
        rlAddProduct?.visibility = View.GONE
        tabLayout?.visibility = View.VISIBLE
        if (proudctList.isEmpty()) {
            rlAddProduct?.visibility = View.VISIBLE
        }
        if (proudctList.isEmpty() && mLocalList.isEmpty()) {
            tabLayout?.visibility = View.GONE
        }
    }

    //删除标签
    private fun deleteProductLabel(mLabel: String?) {
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
}