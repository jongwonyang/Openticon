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
import io.ssafy.openticon.data.model.EmoticonPackEntity
import io.ssafy.openticon.data.model.EmoticonPackWithEmotions
import io.ssafy.openticon.data.model.SampleEmoticon
import io.ssafy.openticon.data.model.SampleEmoticonPack

class EmoticonPackView @JvmOverloads constructor(
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

    fun setupEmoticonPack(pack: EmoticonPackWithEmotions, onPackClick: (List<Emoticon>) -> Unit) {
// 네모 박스를 만들기 위한 FrameLayout 생성
        val frameLayout = FrameLayout(context).apply {
            layoutParams = LayoutParams(
                dpToPx(50), // 네모 박스의 너비와 높이를 50으로 설정
                dpToPx(50)
            )
            setBackgroundColor(Color.parseColor("#F8F5F5")) // 박스의 배경색 설정 (선택 사항)

            // 박스 전체에 onClick 이벤트 설정
            setOnClickListener { onPackClick(pack.emotions) }
        }

// ImageView를 FrameLayout 중앙에 작게 배치
        val imageView = ImageView(context).apply {
            load(pack.emoticonPackEntity.listImg) {
                crossfade(true) // 크로스페이드 효과 (선택 사항)
                placeholder(R.drawable.icon_2) // 로딩 중일 때 사용할 이미지 (선택 사항)
                error(R.drawable.icon_1) // 에러 발생 시 사용할 이미지 (선택 사항)
                allowHardware(false)
            }

            // 이미지 크기를 박스보다 작게 설정하여 중앙에 배치
            layoutParams = FrameLayout.LayoutParams(
                dpToPx(30), // 너비를 작게 설정 (30)
                dpToPx(30)  // 높이를 작게 설정 (30)
            ).apply {
                gravity = Gravity.CENTER // 이미지가 중앙에 위치하도록 설정
            }
        }

// FrameLayout에 ImageView를 추가
        frameLayout.addView(imageView)
        addView(frameLayout)
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    fun displayImagesInTable(tableLayout: TableLayout, images: List<Emoticon>, onImageClick: (Emoticon) -> Unit, onImageLongClick: (Emoticon) -> Unit = {}) {
        tableLayout.removeAllViews()
        var currentRow: TableRow? = null
        val width_size: Int = (tableLayout.width - dpToPx(10))/4
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
