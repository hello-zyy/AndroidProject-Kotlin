package com.hjq.demo.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.hjq.demo.R
import com.hjq.demo.ui.activity.HomeActivity
import com.hjq.demo.ui.adapter.HomeLabelAdapter2
import com.hjq.demo.ui.adapter.HomeProudctAdapter
import com.hjq.demo.ui.obj.UserProductInfo
import com.hjq.demo.ui.utils.BaseFragment
import com.hjq.demo.ui.utils.UserHintDialog
import com.hjq.demo.ui.utils.UserProductUtils
import com.hjq.demo.ui.utils.WpkSPUtil

@SuppressLint("NotifyDataSetChanged")
class WyzeProfuctInfoFragment :
    BaseFragment() {
    private lateinit var rvSecondaryList: RecyclerView
    private lateinit var rvProudctList: RecyclerView
    private lateinit var rlBigInfo: RelativeLayout
    private lateinit var ivBigIcon: ImageView
    private lateinit var tvBigName: TextView
    private lateinit var tvBigPrice: TextView
    private lateinit var tvBigMemberPrice: TextView
    private lateinit var rlAddProduct: RelativeLayout
    private lateinit var proudctAdapter: HomeProudctAdapter
    private lateinit var labelAdapter2: HomeLabelAdapter2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.product_info_fragment, null)
        rvSecondaryList = view.findViewById(R.id.rv_secondary_layout)
        rvProudctList = view.findViewById(R.id.rv_proudct)
        rlBigInfo = view.findViewById(R.id.rl_big_info)
        ivBigIcon = view.findViewById(R.id.iv_big_icon)
        tvBigName = view.findViewById(R.id.tv_big_name)
        tvBigPrice = view.findViewById(R.id.tv_big_price)
        tvBigMemberPrice = view.findViewById(R.id.tv_big_member_price)
        rlAddProduct = view.findViewById(R.id.rl_no_product)
        return view
    }

    override fun onResume() {
        super.onResume()
        UserProductUtils.selectSecondaryLabel = ""
        initData()
        initRecyclerView2()
        initListener()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData() {
        val rowNumber = WpkSPUtil.getString(WpkSPUtil.WPK_HOME_UI_ROW, "2") //行数
        context?.apply {
            val layoutManager =
                StaggeredGridLayoutManager(
                    rowNumber.toInt(),
                    StaggeredGridLayoutManager.VERTICAL
                )
            proudctAdapter = HomeProudctAdapter(this)
            rvProudctList.layoutManager = layoutManager
        }
        val localList = ArrayList<UserProductInfo>()
        val productListByLabel = UserProductUtils.getProductListByLabel()
        if (productListByLabel.isNotEmpty()) {
            productListByLabel.forEach {
                localList.add(it)
            }
        }
        if (TextUtils.isEmpty(UserProductUtils.selectSecondaryLabel)) {
            rvSecondaryList.visibility = if (localList.isEmpty()) View.GONE else View.VISIBLE
        }
        rlAddProduct.visibility = if (localList.isEmpty()) View.VISIBLE else View.GONE
        proudctAdapter.setData(localList)
        rvProudctList.adapter = proudctAdapter
        proudctAdapter.notifyDataSetChanged()
        context?.apply {
            val metrics: DisplayMetrics = resources.displayMetrics // 获取屏幕宽、高
            val lp = ivBigIcon.layoutParams
            lp.height = (metrics.heightPixels / 2)
            ivBigIcon.layoutParams = lp
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView2() {
        activity?.apply {
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            labelAdapter2 = HomeLabelAdapter2(this)
            rvSecondaryList.layoutManager = layoutManager
        }
        val allSecondaryTabListByLabel = UserProductUtils.getAllSecondaryTabListByLabel()
        allSecondaryTabListByLabel.add(
            0,
            UserProductUtils.getResources().getString(R.string.small_labels)
        )
        rvSecondaryList.adapter = labelAdapter2
        labelAdapter2.setData(allSecondaryTabListByLabel)
        labelAdapter2.setSelectLabel(UserProductUtils.selectLabel)
        labelAdapter2.notifyDataSetChanged()
    }

    private fun initListener() {
        proudctAdapter.setListener(object : HomeProudctAdapter.OnAdapterListener {
            override fun onClickAction(userProductObj: UserProductInfo?) {
                userProductObj?.apply {
                    rlBigInfo.visibility = View.VISIBLE
                    rvProudctList.visibility = View.GONE
                    Glide.with(this@WyzeProfuctInfoFragment)
                        .load(url)
                        .error(R.mipmap.launcher_ic)
                        .into(ivBigIcon)
                    tvBigName.text = describe
                    tvBigPrice.text = originalPrice
                    tvBigMemberPrice.text = memberPrice
                }
                context?.apply {
                    val metrics: DisplayMetrics = resources.displayMetrics // 获取屏幕宽、高
                    val lp = ivBigIcon.layoutParams
                    lp.height = ((metrics.widthPixels * 1.2).toInt())
                    ivBigIcon.layoutParams = lp
                }
            }

            override fun onLongClickAction(userProductObj: UserProductInfo?) {
                deleteProduct(userProductObj)
            }
        })

        rlBigInfo.setOnClickListener {
            rlBigInfo.visibility = View.GONE
            rvProudctList.visibility = View.VISIBLE
        }
        rlAddProduct.setOnClickListener {
            activity?.apply {
                HomeActivity.start(this, FindFragment2::class.java)
            }
        }
        labelAdapter2.setListener(object : HomeLabelAdapter2.OnAdapterListener {
            override fun onClickAction(labelStr: String?) {
                labelStr?.apply {
                    if (TextUtils.equals(UserProductUtils.selectSecondaryLabel, this)) {
                        UserProductUtils.selectSecondaryLabel = ""
                    } else {
                        UserProductUtils.selectSecondaryLabel = this
                    }
                    initData()
                    labelAdapter2.setSelectLabel(UserProductUtils.selectSecondaryLabel)
                    labelAdapter2.notifyDataSetChanged()
                }
            }

            override fun onLongClickAction(labelStr: String?) {
                //DO
            }
        })
    }

    internal inner class ViewHolder(tabView: View) {
        var mTabItemTv: TextView = tabView.findViewById<View>(R.id.tv_label) as TextView
        var mTabLine: View = tabView.findViewById(R.id.iv_select_line)
    }

    //删除商品
    private fun deleteProduct(userProductObj: UserProductInfo?) {
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
                    UserProductUtils.deleteProductByInfo(userProductObj)
                    initData()
                    initListener()
                }

                override fun doCancel() {
                    dialog.dismiss()
                }
            })
            dialog.show()
            dialog.setTitleName("是否要删除此商品？")
        }
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

}