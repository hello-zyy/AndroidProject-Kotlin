package com.hjq.demo.ui.utils

import android.content.Context
import android.content.res.Resources
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.hjq.demo.app.AppApplication
import com.hjq.demo.ui.obj.UserProductInfo

class UserProductUtils {
    companion object {
        var mProductList: List<UserProductInfo> = ArrayList() //hub bind wifi list
        var selectLabel: String = ""//选中的label
        const val RECOMMENDED_PRODUCT = "推荐"
        const val GOTHAM_BOLD = "fonts/gotham_bold.ttf"
        const val GOTHAM_BOOK = "fonts/gotham_bold.ttf"
        var numberOfHomeImages = 4

        fun dp2px(dpValue: Float): Int {
            val scale = Resources.getSystem().displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        fun getResources(): Resources {
            return AppApplication.getAppContext().resources
        }

        fun getColor(id: Int): Int {
            return getResources().getColor(id)
        }

        //根据label查询商品列表
        fun getProductListByLabel(): ArrayList<UserProductInfo> {
            val mAllList = ArrayList<UserProductInfo>()
            val mLocalList =
                WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_INFO, UserProductInfo::class.java)
            mLocalList?.forEach {
                it?.apply {
                    if (TextUtils.equals(selectLabel, this.label) || TextUtils.equals(
                            selectLabel,
                            RECOMMENDED_PRODUCT
                        )
                    ) {
                        mAllList.add(this)
                    }
                }
            }
            return mAllList
        }


        //删除商品
        fun deleteProductByInfo(info: UserProductInfo?) {
            info?.apply {
                val mLocalList =
                    WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_INFO, UserProductInfo::class.java)
                val iterator = mLocalList?.iterator()
                iterator?.apply {
                    while (hasNext()) {
                        val next = next()
                        if (TextUtils.equals(next.label, info.label) && TextUtils.equals(
                                next.name,
                                info.name
                            )
                        ) {
                            remove()
                        }
                    }
                }
                WpkSPUtil.putListData(WpkSPUtil.WPK_PRODUCT_INFO, mLocalList)
            }
        }

        //删除label
        fun deleteProductLableByInfo(mLabel: String?) {
            mLabel?.apply {
                val mLocalLabelList =
                    WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_LABEL, String::class.java)
                val iterator = mLocalLabelList?.iterator()
                iterator?.apply {
                    while (hasNext()) {
                        val next = next()
                        if (TextUtils.equals(next, mLabel)) {
                            remove()
                        }
                    }
                }
                WpkSPUtil.putListData(WpkSPUtil.WPK_PRODUCT_LABEL, mLocalLabelList)
                selectLabel = RECOMMENDED_PRODUCT //默认进来选中推荐Tab
            }
        }

        //隐藏键盘
        fun hideSoftInput(view: View) {
            val inputManager = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager?.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

    }
}