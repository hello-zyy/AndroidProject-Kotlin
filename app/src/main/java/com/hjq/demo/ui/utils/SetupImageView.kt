package com.hjq.demo.ui.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView


class SetupImageView : AppCompatImageView {
    companion object {
        const val TAG = "SetupImageView"
    }

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val height: Int = (MeasureSpec.getSize(widthMeasureSpec) * 275/375 ).toInt()

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY))
    }
}
