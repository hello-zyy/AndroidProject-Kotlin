package com.hjq.demo.ui.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.hjq.demo.app.AppApplication
import com.hjq.demo.ui.obj.UserProductInfo

class UserProductUtils {
    companion object {
        var mProductList: List<UserProductInfo> = ArrayList() //hub bind wifi list
        var selectLabel: String = ""//选中的label
        var selectSecondaryLabel: String = ""//选中的label
        const val RECOMMENDED_PRODUCT = "推荐"
        const val GOTHAM_BOLD = "fonts/gotham_bold.ttf"
        const val GOTHAM_BOOK = "fonts/gotham_book.ttf"
        const val TRUE_STR = "true"
        const val FALSE_STR = "false"
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

        fun getFont(fileName: String?): Typeface? {
            return Typeface.createFromAsset(
                getResources().getAssets(),
                fileName
            )
        }

        //次标签SP
        fun getProductSecondaryLabelSp(fileName: String): String {
            return fileName + WpkSPUtil.WPK_PRODUCT_SECONDARY_LABEL
        }

        //根据label查询商品列表
//        fun getAllSecondaryTabListByLabel(): ArrayList<String> {
//            val mAllList = ArrayList<String>()
//            val mLocalList =
//                WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_INFO, UserProductInfo::class.java)
//            mLocalList?.forEach {
//                it?.apply {
//                    if (TextUtils.equals(selectLabel, this.label) || TextUtils.equals(
//                            selectLabel,
//                            RECOMMENDED_PRODUCT
//                        )
//                    ) {
//                        if (!mAllList.contains(this.secondary_label)) {
//                            mAllList.add(this.secondary_label)
//                        }
//                    }
//                }
//            }
//            return mAllList
//        }

        //根据label查询商品列表
        fun getAllSecondaryTabListByLabel(): ArrayList<String> {
            val mAllList = ArrayList<String>()
            val mLabelList: List<String>
            if (TextUtils.equals(selectLabel, RECOMMENDED_PRODUCT)) {
                mLabelList =
                    WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_SECONDARY_LABEL, String::class.java)
            } else {
                mLabelList =
                    WpkSPUtil.getListData(
                        getProductSecondaryLabelSp(selectLabel),
                        String::class.java
                    )
            }
            mAllList.addAll(mLabelList)
            return mAllList
        }

        //根据label查询商品列表
        fun getProductListByLabel(): ArrayList<UserProductInfo> {
            val mAllList = ArrayList<UserProductInfo>()
            val mLocalList =
                WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_INFO, UserProductInfo::class.java)
            mLocalList?.forEach {
                it?.apply {
                    if (checkTheRule(this)) {
                        mAllList.add(this)
                    }
                }
            }
            return mAllList
        }

        //根据名称查询商品列表
        fun getProductListByName(nameStr: String): ArrayList<UserProductInfo> {
            val mAllList = ArrayList<UserProductInfo>()
            val mLocalList =
                WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_INFO, UserProductInfo::class.java)
            mLocalList?.forEach {
                it?.apply {
                    if (TextUtils.equals(this.secondary_label, nameStr)) {
                        mAllList.add(this)
                    }
                }
            }
            return mAllList
        }

        //根据名称查询商品列表
        fun getFuzzyQueryProductListByName(nameStr: String): ArrayList<UserProductInfo> {
            val mAllList = ArrayList<UserProductInfo>()
            val mLocalList =
                WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_INFO, UserProductInfo::class.java)
            mLocalList?.forEach {
                it?.apply {
                    if (!TextUtils.isEmpty(nameStr)
                        && secondary_label.contains(nameStr)
                        || !TextUtils.isEmpty(nameStr)
                        && nameStr.contains(secondary_label)
                        || !TextUtils.isEmpty(nameStr)
                        && nameStr.contains(describe)
                        || !TextUtils.isEmpty(nameStr)
                        && describe.contains(nameStr)
                    ) {
                        mAllList.add(this)
                    }
                }
            }
            return mAllList
        }

        //查看是否符合规则
        private fun checkTheRule(info: UserProductInfo): Boolean {
            if (TextUtils.equals(selectLabel, RECOMMENDED_PRODUCT)
                && TextUtils.isEmpty(selectSecondaryLabel)
            ) {
                return true
            }
            if (TextUtils.equals(selectLabel, RECOMMENDED_PRODUCT)
                && TextUtils.equals(selectSecondaryLabel, info.secondary_label)
            ) {
                return true
            }
            if (TextUtils.equals(selectLabel, info.label)
                && TextUtils.isEmpty(selectSecondaryLabel)
            ) {
                return true
            }
            if (TextUtils.equals(selectLabel, info.label)
                && TextUtils.equals(selectSecondaryLabel, info.secondary_label)
            ) {
                return true

            }
            return false
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
                        if (TextUtils.equals(next.label, info.label)
                            && TextUtils.equals(next.describe, info.describe)
                            && TextUtils.equals(next.secondary_label, info.secondary_label)
                        ) {
                            remove()
                        }
                    }
                }
                WpkSPUtil.putListData(WpkSPUtil.WPK_PRODUCT_INFO, mLocalList)
            }
        }

        //删除次标签
        fun deleteSecondaryLableByInfo(mLabel: String?) {
            mLabel?.apply {
                val mLocalLabelList =
                    WpkSPUtil.getListData(WpkSPUtil.WPK_PRODUCT_SECONDARY_LABEL, String::class.java)
                val iterator = mLocalLabelList?.iterator()
                iterator?.apply {
                    while (hasNext()) {
                        val next = next()
                        if (TextUtils.equals(next, mLabel)) {
                            remove()
                        }
                    }
                }
                WpkSPUtil.putListData(WpkSPUtil.WPK_PRODUCT_SECONDARY_LABEL, mLocalLabelList)
            }
        }

        //删除主标签
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