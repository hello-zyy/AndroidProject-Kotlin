package com.hjq.demo.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hjq.demo.R
import com.hjq.demo.ui.utils.UserProductUtils

class HomeLabelAdapter(private var mContext: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mProductInfo: List<String>? = null
    var mSelectLabel: String = ""
    var mListener: OnAdapterListener? = null

    fun setData(mRuleActionInfo: List<String>) {
        this.mProductInfo = mRuleActionInfo
        if (TextUtils.isEmpty(mSelectLabel) && mProductInfo?.isNotEmpty() == true) {
            mSelectLabel = mProductInfo?.get(0).toString()
        }
    }

    fun setSelectLabel(label: String) {
        this.mSelectLabel = label
    }

    fun setListener(mListener: OnAdapterListener) {
        this.mListener = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DeviceViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.find_label_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DeviceViewHolder).update(position)
    }


    override fun getItemCount(): Int {
        return if (mProductInfo == null) 0 else mProductInfo!!.size
    }

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun update(position: Int) {
            mNameTv.setTextColor(UserProductUtils.getColor(R.color.text_color_353535))
            mNameTv.textSize = 16f
            mSelectLintView.visibility = View.GONE
            mProductInfo?.get(position)?.apply {
                mNameTv.text = this
            }
            rootView.setOnClickListener { mListener?.onClickAction(mProductInfo?.get(position)) }
            rootView.setOnLongClickListener {
                mListener?.onLongClickAction(mProductInfo?.get(position))
                true
            }
        }

        private var mNameTv: TextView = itemView.findViewById(R.id.tv_label)
        private var mSelectLintView: View = itemView.findViewById(R.id.iv_select_line)
        var rootView: View = itemView
    }

    interface OnAdapterListener {
        fun onClickAction(labelStr: String?)
        fun onLongClickAction(labelStr: String?)
    }
}