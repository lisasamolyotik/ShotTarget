package com.target.shot.model

import android.content.Context
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import com.target.shot.R
import com.target.shot.util.DimensionConverter

class SuperBooster(context: Context) : Item(context) {
    init {
        layoutParams = RelativeLayout.LayoutParams(
            DimensionConverter.dpToPixels(44f, mContext),
            DimensionConverter.dpToPixels(44f, mContext))
        setImageResource(R.drawable.super_booster)
    }

    companion object {
        const val width = 44
        const val height = 44
    }
}