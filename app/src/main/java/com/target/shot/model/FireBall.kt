package com.target.shot.model

import android.content.Context
import android.widget.RelativeLayout
import com.target.shot.R
import com.target.shot.util.DimensionConverter

class FireBall(context: Context) : Item(context) {
    init {
        layoutParams = RelativeLayout.LayoutParams(
            DimensionConverter.dpToPixels(93f, mContext),
            DimensionConverter.dpToPixels(33f, mContext)
        )
        setImageResource(R.drawable.fire1)
    }

    companion object {
        const val width = 93
        const val height = 33
    }
}