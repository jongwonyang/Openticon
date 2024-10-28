package com.example.floatingtest

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import io.ssafy.openticon.R


class CustomImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ImageView(context, attrs) {

    init {
        // 커스텀 속성을 읽어와서 이미지 설정
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomImageView)
            val customSrc = typedArray.getResourceId(R.styleable.CustomImageView_customSrc, -1)
            if (customSrc != -1) {
                setImageResource(customSrc)
            }
            typedArray.recycle()
        }
    }

    var imageInfo: String = ""
    var otherData: Int = 0
}
