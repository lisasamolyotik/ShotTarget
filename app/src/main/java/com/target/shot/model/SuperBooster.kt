package com.target.shot.model

import android.content.Context
import android.widget.RelativeLayout
import com.target.shot.R
import com.target.shot.util.DimensionConverter

class SuperBooster(context: Context) : Item(context) {
    init {
        layoutParams = RelativeLayout.LayoutParams(
            DimensionConverter.dpToPixels(44f, mContext),
            DimensionConverter.dpToPixels(44f, mContext)
        )
        setImageResource(R.drawable.super_booster)
    }
}