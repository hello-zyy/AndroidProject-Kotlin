package com.hjq.demo.ui.activity

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.hjq.demo.R
import com.hjq.demo.app.AppActivity
import com.hjq.demo.ui.adapter.HomeProudctAdapter
import com.hjq.demo.ui.obj.UserProductInfo
import com.hjq.demo.ui.utils.UserProductUtils
import com.hjq.demo.ui.utils.WpkSPUtil

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 可进行拷贝的副本
 */
class SeachActivity : AppActivity() {
    private val mEditSearch: EditText? by lazy { findViewById(R.id.ed_search_product) }
    private val mStartSearchTv: TextView? by lazy { findViewById(R.id.tv_start_search) }
    private val rvProudctList: RecyclerView? by lazy { findViewById(R.id.rv_search_proudct) }
    private val tvNoFoundProudct: TextView? by lazy { findViewById(R.id.tv_no_product_found) }
    private val rlBigInfo: RelativeLayout? by lazy { findViewById(R.id.rl_big_info) }
    private val ivBigIcon: ImageView? by lazy { findViewById(R.id.iv_big_icon) }
    private val tvBigName: TextView? by lazy { findViewById(R.id.tv_big_name) }
    private val tvBigPrice: TextView? by lazy { findViewById(R.id.tv_big_price) }
    private val tvBigMemberPrice: TextView? by lazy { findViewById(R.id.tv_big_member_price) }
    private lateinit var proudctAdapter: HomeProudctAdapter

    override fun getLayoutId(): Int {
        return R.layout.seach_activity
    }

    override fun initView() {
        tvNoFoundProudct?.visibility = View.GONE
        setNextEnable()
        initRecyclerView()
        intListener()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView() {
        val rowNumber = WpkSPUtil.getString(WpkSPUtil.WPK_HOME_UI_ROW, "2") //行数
        getActivity()?.apply {
            val layoutManager =
                StaggeredGridLayoutManager(
                    rowNumber.toInt(),
                    StaggeredGridLayoutManager.VERTICAL
                )
            proudctAdapter = HomeProudctAdapter(this)
            rvProudctList?.layoutManager = layoutManager
        }
        proudctAdapter.setData(ArrayList())
        rvProudctList?.adapter = proudctAdapter
    }


    private fun intListener() {
        mEditSearch?.addTextChangedListener(object : TextWatcher {
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
        mStartSearchTv?.setOnClickListener {
            searchProduct()
        }
        mEditSearch?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchProduct()
                    return  true
                }
                return  false
            }
        })
        proudctAdapter.setListener(object : HomeProudctAdapter.OnAdapterListener {
            override fun onClickAction(userProductObj: UserProductInfo?) {
                userProductObj?.apply {
                    rlBigInfo?.visibility = View.VISIBLE
                    rvProudctList?.visibility = View.GONE
                    ivBigIcon?.apply {
                        Glide.with(this@SeachActivity)
                            .load(url)
                            .error(R.mipmap.launcher_ic)
                            .into(this)
                    }
                    tvBigName?.text = describe
                    tvBigPrice?.text = originalPrice
                    tvBigMemberPrice?.text = memberPrice
                }
                getActivity()?.apply {
                    val metrics: DisplayMetrics = resources.displayMetrics // 获取屏幕宽、高
                    val lp = ivBigIcon?.layoutParams
                    lp?.height = ((metrics.widthPixels * 1))
                    ivBigIcon?.layoutParams = lp
                }
            }

            override fun onLongClickAction(userProductObj: UserProductInfo?) {
                //no
            }
        })
        rlBigInfo?.setOnClickListener {
            rlBigInfo?.visibility = View.GONE
            rvProudctList?.visibility = View.VISIBLE
        }
    }

    private fun searchProduct() {
        val infoStr = mEditSearch?.text.toString().trim()
        initListInfo(infoStr)
        mEditSearch?.apply {
            UserProductUtils.hideSoftInput(this);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initListInfo(infoStr: String) {
        tvNoFoundProudct?.visibility = View.GONE
        val localList = ArrayList<UserProductInfo>()
        var productListByName = UserProductUtils.getProductListByName(infoStr)
        if (productListByName.isEmpty()) {
            productListByName = UserProductUtils.getFuzzyQueryProductListByName(infoStr)
        }
        if (productListByName.isNotEmpty()) {
            productListByName.forEach {
                localList.add(it)
            }
        } else {
            tvNoFoundProudct?.visibility = View.VISIBLE
        }
        proudctAdapter.setData(localList)
        rvProudctList?.adapter = proudctAdapter
        proudctAdapter.notifyDataSetChanged()
    }

    override fun initData() {
        //DO NOTINGS
    }

    fun setNextEnable() {
        val rowStr = mEditSearch?.text.toString().trim()
        val isEnable = !TextUtils.isEmpty(rowStr)
        mStartSearchTv?.setTextColor(UserProductUtils.getColor(if (isEnable) R.color.text_color_995DF6 else R.color.text_color_353535))
        mStartSearchTv?.isEnabled = isEnable
    }
}