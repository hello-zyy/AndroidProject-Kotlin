package com.hjq.demo.ui.adapter

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hjq.demo.R
import com.hjq.demo.ui.obj.UserProductInfo
import com.hjq.demo.ui.utils.UserProductUtils
import com.hjq.demo.ui.utils.WpkSPUtil

class HomeProudctAdapter(private var mContext: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mProductInfo: List<UserProductInfo>? = null
    var mListener: OnAdapterListener? = null

    fun setData(mRuleActionInfo: List<UserProductInfo>) {
        this.mProductInfo = mRuleActionInfo
    }

    fun setListener(mListener: OnAdapterListener) {
        this.mListener = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DeviceViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.user_product_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DeviceViewHolder).update(position)
    }


    override fun getItemCount(): Int {
        return if (mProductInfo == null) 0 else mProductInfo!!.size
    }

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rowNumber = WpkSPUtil.getString(WpkSPUtil.WPK_HOME_UI_ROW, "2") //行数
        fun update(position: Int) {
            val metrics: DisplayMetrics = mContext.resources.displayMetrics // 获取屏幕宽、高
            val lp = mIconIv.layoutParams
            lp.height = (metrics.heightPixels / rowNumber.toInt()) - UserProductUtils.dp2px(100f)
            mIconIv.layoutParams = lp

            mProductInfo?.get(position)?.apply {
                Glide.with(mContext)
                    .load(url)
                    .error(R.mipmap.launcher_ic)
                    .into(mIconIv)
                mNameTv.text = name
            }
            rootView.setOnClickListener { mListener?.onClickAction(mProductInfo?.get(position)) }
            rootView.setOnLongClickListener {
                mListener?.onLongClickAction(mProductInfo?.get(position))
                true
            }
        }

        private var mIconIv: ImageView = itemView.findViewById(R.id.iv_icon)
        var mNameTv: TextView = itemView.findViewById(R.id.tv_name)
        var rootView: View = itemView
    }

    interface OnAdapterListener {
        fun onClickAction(userProductObj: UserProductInfo?)

        fun onLongClickAction(userProductObj: UserProductInfo?)
    }
}