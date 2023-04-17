package com.hjq.demo.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.hjq.demo.R
import com.hjq.demo.ui.adapter.HomeProudctAdapter
import com.hjq.demo.ui.obj.UserProductInfo
import com.hjq.demo.ui.utils.BaseFragment
import com.hjq.demo.ui.utils.UserHintDialog
import com.hjq.demo.ui.utils.UserProductUtils
import com.hjq.demo.ui.utils.WpkSPUtil

class WyzeProfuctInfoFragment constructor(private var currentPos: Int) :
    BaseFragment() {
    private lateinit var rvProudctList: RecyclerView
    private lateinit var rlBigInfo: RelativeLayout
    private lateinit var ivBigIcon: ImageView
    private lateinit var tvBigName: TextView
    private lateinit var proudctAdapter: HomeProudctAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.product_info_fragment, null)
        rvProudctList = view.findViewById(R.id.rv_proudct)
        rlBigInfo = view.findViewById(R.id.rl_big_info)
        ivBigIcon = view.findViewById(R.id.iv_big_icon)
        tvBigName = view.findViewById(R.id.tv_big_name)
        return view
    }

    override fun onResume() {
        super.onResume()
        initData()
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
        var mCount = 0;
        val productListByLabel = UserProductUtils.getProductListByLabel()
        if (productListByLabel.isNotEmpty()) {
            for (i in productListByLabel.indices) {
                val it = productListByLabel.get(i)
                localList.add(it)
                if (localList.size > (UserProductUtils.numberOfHomeImages - 1) || i == productListByLabel.size - 1) {
                    mCount++
                    if (mCount == currentPos) {
                        proudctAdapter.setData(localList)
                        rvProudctList.adapter = proudctAdapter
                        proudctAdapter.notifyDataSetChanged()
                        return
                    }
                    localList.clear()
                }
            }
        }
        context?.apply {
            val metrics: DisplayMetrics = resources.displayMetrics // 获取屏幕宽、高
            val lp = ivBigIcon.layoutParams
            lp.height = (metrics.heightPixels / 2)
            ivBigIcon.layoutParams = lp
        }
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
                    tvBigName.text = name
                }
                context?.apply {
                    val metrics: DisplayMetrics = resources.displayMetrics // 获取屏幕宽、高
                    val lp = ivBigIcon.layoutParams
                    lp.height = ((metrics.widthPixels * 1.4).toInt())
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
    }

    //删除商品
    private fun deleteProduct(userProductObj: UserProductInfo?) {
        activity?.apply {
            val dialog = UserHintDialog(this)
            dialog.setClicklistener(object : UserHintDialog.ClickListenerInterface {
                override fun doConfirm() {
                    dialog.dismiss()
                    UserProductUtils.deleteProductByInfo(userProductObj)
                    initData()

                }

                override fun doCancel() {
                    dialog.dismiss()
                }
            })
            dialog.show()
            dialog.setTitleName("是否要删除此商品？")
        }
    }
}