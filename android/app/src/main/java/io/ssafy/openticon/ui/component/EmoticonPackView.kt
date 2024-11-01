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
import androidx.compose.ui.unit.dp
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPack

class EmoticonPackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = HORIZONTAL
    }

    fun setupEmoticonPack(pack: EmoticonPack, onPackClick: (List<Emoticon>) -> Unit) {
// 네모 박스를 만들기 위한 FrameLayout 생성
        val frameLayout = FrameLayout(context).apply {
            layoutParams = LayoutParams(
                dpToPx(50), // 네모 박스의 너비와 높이를 50으로 설정
                dpToPx(50)
            )
            setBackgroundColor(Color.parseColor("#F8F5F5")) // 박스의 배경색 설정 (선택 사항)

            // 박스 전체에 onClick 이벤트 설정
            setOnClickListener { onPackClick(pack.images) }
        }

// ImageView를 FrameLayout 중앙에 작게 배치
        val imageView = ImageView(context).apply {
            setImageResource(pack.mainImageResource)

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

        images.forEachIndexed { index, emoticon ->
            if (index % 4 == 0) {
                currentRow = TableRow(context).apply {
                    layoutParams = TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                    )
                    gravity = Gravity.CENTER // 가운데 정렬
                    setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8)) // 패딩 8dp 적용
                }
                tableLayout.addView(currentRow)
            }
            val imageView = ImageView(context).apply {
                setImageResource(emoticon.imageResource)
                layoutParams = TableRow.LayoutParams(dpToPx(95), dpToPx(95))
                setPadding(dpToPx(8),dpToPx(8),dpToPx(8),dpToPx(8))
                setOnClickListener {
                    onImageClick(emoticon)
                }
                setOnLongClickListener{
                    onImageLongClick(emoticon)
                    true // 이벤트 소비
                }
            }
            currentRow?.addView(imageView)
        }
        val lastRow = tableLayout.getChildAt(tableLayout.childCount - 1) as? TableRow
        val remainingViews = 4 - (images.size % 4)
        if (remainingViews in 1..3 && lastRow != null) {
            for (i in 1..remainingViews) {
                val spacerView = View(context).apply {
                    layoutParams = TableRow.LayoutParams(dpToPx(95), dpToPx(95))
                    setPadding(dpToPx(8),dpToPx(8),dpToPx(8),dpToPx(8))
                }
                lastRow.addView(spacerView)
            }
        }
    }

}
