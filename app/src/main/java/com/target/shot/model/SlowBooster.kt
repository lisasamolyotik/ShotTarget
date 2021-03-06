package com.target.shot.model

import android.content.Context
import android.widget.RelativeLayout
import com.target.shot.R
import com.target.shot.util.DimensionConverter

class SlowBooster(context: Context) : Item(context) {
    init {
        layoutParams = RelativeLayout.LayoutParams(
            DimensionConverter.dpToPixels(40f, mContext),
            DimensionConverter.dpToPixels(40f, mContext)
        )
        setImageResource(R.drawable.slow_booster)
    }
}