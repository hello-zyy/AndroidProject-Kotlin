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

class HomeLabelAdapter2(private var mContext: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TITLE_TYPE = 1
        const val DEVICE_TYPE = 2
    }

    var mProductInfo: List<String>? = null
    var mSelectLabel: String = ""
    var mListener: OnAdapterListener? = null

    fun setData(mRuleActionInfo: ArrayList<String>) {
        this.mProductInfo = mRuleActionInfo
    }

    fun setSelectLabel(label: String) {
        this.mSelectLabel = label
    }

    fun setListener(mListener: OnAdapterListener) {
        this.mListener = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TITLE_TYPE) {
            return TitleViewHolder(
                LayoutInflater.from(mContext)
                    .inflate(R.layout.user_label_title_item, parent, false)
            )
        } else {
            return DeviceViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.user_label_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TITLE_TYPE -> (holder as TitleViewHolder).update(position)
            DEVICE_TYPE -> (holder as DeviceViewHolder).update(position)
            else -> {}
        }
    }


    override fun getItemCount(): Int {
        return if (mProductInfo == null) 0 else mProductInfo!!.size
    }

    override fun getItemViewType(position: Int): Int {
        mProductInfo?.apply {
            val labelS: String = get(position)
            return if (TextUtils.equals(labelS, UserProductUtils.getResources().getString(R.string.small_labels))) {
                TITLE_TYPE
            } else {
                DEVICE_TYPE
            }
        }
        return DEVICE_TYPE
    }


    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun update(position: Int) {
            mNameTv.setTextColor(UserProductUtils.getColor(R.color.text_color_9c9c9c))
            mNameTv.textSize = 16f
            mSelectLintView.visibility = View.GONE
            val labelStr = mProductInfo?.get(position)
            if (TextUtils.equals(mSelectLabel, labelStr)) {
                mNameTv.textSize = 18f
                mNameTv.setTextColor(UserProductUtils.getColor(R.color.text_color_353535))
                mSelectLintView.visibility = View.VISIBLE
            }
            mProductInfo?.get(position)?.apply {
                mNameTv.text = this
            }
            rootView.setOnClickListener {
                mListener?.onClickAction(mProductInfo?.get(position))
            }
            rootView.setOnLongClickListener {
                mListener?.onLongClickAction(mProductInfo?.get(position))
                true
            }
        }

        private var mNameTv: TextView = itemView.findViewById(R.id.tv_label)
        private var mSelectLintView: View = itemView.findViewById(R.id.iv_select_line)
        var rootView: View = itemView
    }

    inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun update(position: Int) {
            //DO
        }
        var rootView: View = itemView
    }

    interface OnAdapterListener {
        fun onClickAction(labelStr: String?)
        fun onLongClickAction(labelStr: String?)
    }
}