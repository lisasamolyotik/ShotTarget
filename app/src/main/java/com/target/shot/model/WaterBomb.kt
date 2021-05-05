package com.target.shot.model

import android.content.Context
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import com.target.shot.R
import com.target.shot.util.DimensionConverter

class WaterBomb(context: Context): Item(context) {
    companion object {
        const val width = 35
        const val height = 35
    }

    init {
        layoutParams = RelativeLayout.LayoutParams(
            DimensionConverter.dpToPixels(35f, mContext),
            DimensionConverter.dpToPixels(35f, mContext))
        setImageResource(R.drawable.water_bomb)
    }
}