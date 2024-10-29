package io.ssafy.openticon.ui.component

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
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
        val imageView = ImageView(context).apply {
            setImageResource(pack.mainImageResource)
            layoutParams = LayoutParams(100, 100) // 적절한 크기 조정
            setOnClickListener { onPackClick(pack.images) }
        }
        addView(imageView)
    }

    fun displayImagesInTable(tableLayout: TableLayout, images: List<Emoticon>, onImageClick: (Emoticon) -> Unit) {
        tableLayout.removeAllViews()
        var currentRow: TableRow? = null

        images.forEachIndexed { index, emoticon ->
            if (index % 4 == 0) {
                currentRow = TableRow(context)
                tableLayout.addView(currentRow)
            }
            val imageView = ImageView(context).apply {
                setImageResource(emoticon.imageResource)
                layoutParams = TableRow.LayoutParams(200, 200)
                setOnClickListener {
                    onImageClick(emoticon)
                }
            }
            currentRow?.addView(imageView)
        }
    }

}
