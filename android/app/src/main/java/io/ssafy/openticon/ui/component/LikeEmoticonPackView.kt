package io.ssafy.openticon.ui.component

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.load
import io.ssafy.openticon.R
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPackWithEmotions
import io.ssafy.openticon.data.model.LikeEmoticon
import io.ssafy.openticon.data.model.LikeEmoticonPack
import io.ssafy.openticon.data.model.SampleEmoticon
import io.ssafy.openticon.data.model.SampleEmoticonPack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LikeEmoticonPackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = HORIZONTAL
    }

    fun makeGray() {
        for (i in 0 until childCount) {
            val frameLayout = getChildAt(i) as? FrameLayout
            frameLayout?.setBackgroundColor(Color.parseColor("#80B0B0B0")) // 투명한 회색
        }
    }

    fun resetColor() {
        for (i in 0 until childCount) {
            val frameLayout = getChildAt(i) as? FrameLayout
            frameLayout?.setBackgroundColor(Color.TRANSPARENT) // 배경색을 투명으로 초기화
        }
    }

    suspend fun setupEmoticonPack(pack: LikeEmoticonPack, onPackClick: suspend (List<LikeEmoticon>) -> Unit) {
        val frameLayout = FrameLayout(context).apply {
            layoutParams = LayoutParams(dpToPx(50), dpToPx(50))
            setBackgroundColor(Color.parseColor("#F8F5F5"))

            setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    onPackClick(pack.emoticons) // onPackClick을 suspend 함수로 호출
                }
            }
        }

        val imageView = ImageView(context).apply {
            load(pack.filePath) {
                crossfade(true)
                placeholder(R.drawable.icon_1)
                error(R.drawable.icon_2)
                allowHardware(false)
            }
            layoutParams = FrameLayout.LayoutParams(dpToPx(30), dpToPx(30)).apply {
                gravity = Gravity.CENTER
            }
        }

        frameLayout.addView(imageView)
        addView(frameLayout)
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    fun displayImagesInTable(tableLayout: TableLayout, images: List<LikeEmoticon>, onImageClick: (LikeEmoticon) -> Unit, onImageLongClick: (LikeEmoticon) -> Unit = {}) {
        tableLayout.removeAllViews()
        var currentRow: TableRow? = null
        var width_size: Int = (tableLayout.width - dpToPx(10))/4
        images.forEachIndexed { index, emoticon ->
            if (index % 4 == 0) {
                currentRow = TableRow(context).apply {
                    layoutParams = TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                    )
                    gravity = Gravity.CENTER // 가운데 정렬
                    setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4)) // 패딩 8dp 적용
                }
                tableLayout.addView(currentRow)
            }
            val imageLoader = ImageLoader.Builder(context)
                .components {
                    add(GifDecoder.Factory()) // GIF 디코더 추가
                }
                .build()

// ImageView 생성 및 설정
            val imageView = ImageView(context).apply {
                layoutParams = TableRow.LayoutParams((width_size), (width_size))
                setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4))

                setOnClickListener {
                    onImageClick(emoticon)
                }
                setOnLongClickListener {
                    onImageLongClick(emoticon)
                    true
                }
            }

            imageView.load(emoticon.filePath) {
                crossfade(false) // 페이드 애니메이션 비활성화
                allowHardware(false) // 하드웨어 가속 비활성화로 GIF 애니메이션 멈춤
                placeholder(R.drawable.icon_1) // 로딩 중 기본 이미지
                error(R.drawable.icon_2) // 로딩 실패 시 표시할 이미지
            }

            currentRow?.addView(imageView)
        }
        val lastRow = tableLayout.getChildAt(tableLayout.childCount - 1) as? TableRow
        val remainingViews = 4 - (images.size % 4)
        if (remainingViews in 1..3 && lastRow != null) {
            for (i in 1..remainingViews) {
                val spacerView = View(context).apply {
                    layoutParams = TableRow.LayoutParams((width_size), (width_size))
                    setPadding(dpToPx(4),dpToPx(4),dpToPx(4),dpToPx(4))
                }
                lastRow.addView(spacerView)
            }
        }
    }

}
