package io.ssafy.openticon.ui.component

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import io.ssafy.openticon.R

class CustomImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ImageView(context, attrs) {

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomImageView,
            0, 0
        ).apply {
            try {
                val customSrc = getResourceId(R.styleable.CustomImageView_customSrc, -1)
                if (customSrc != -1) {
                    setImageResource(customSrc)
                }
            } finally {
                recycle()
            }
        }
    }
}
