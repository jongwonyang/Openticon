package io.ssafy.openticon.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import io.ssafy.openticon.data.model.SampleEmoticonPack
import io.ssafy.openticon.ui.viewmodel.MyEmoticonViewModel
import kotlin.math.roundToInt

val itemHeight = 66.dp.value // EmoticonItem의 높이 (50.dp + 패딩 등)
val itemSpacing = 16.dp.value // 아이템 간의 간격

@Composable
fun MyEmoticonsScreen(viewModel: MyEmoticonViewModel = hiltViewModel()) {
    val visibleEmoticonPacks by viewModel.visibleSampleEmoticonPacks.observeAsState(emptyList())
    val invisibleEmoticonPacks by viewModel.invisibleSampleEmoticonPacks.observeAsState(emptyList())

    var isVisible by remember { mutableStateOf(true) }

    val emoticonPacksState = remember { mutableStateListOf<SampleEmoticonPack>() }

    LaunchedEffect(visibleEmoticonPacks, invisibleEmoticonPacks, isVisible) {
        emoticonPacksState.clear()
        if (isVisible) {
            emoticonPacksState.addAll(visibleEmoticonPacks)
        } else {
            emoticonPacksState.addAll(invisibleEmoticonPacks)
        }
    }

    var draggedItemIndex by remember { mutableIntStateOf(-1) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    val lazyListState = rememberLazyListState()

    val onDragStart: (Int) -> Unit = { index ->
        draggedItemIndex = index
    }

    val onDragEnd: () -> Unit = {
        draggedItemIndex = -1
        offsetY = 0f
        // 변경된 리스트를 ViewModel에 업데이트
        viewModel.updateEmoticonPacks(emoticonPacksState.toList())
    }

    val onDragCancel: () -> Unit = {
        draggedItemIndex = -1
        offsetY = 0f
    }

    val onDrag: (PointerInputChange, Offset) -> Unit = { change, dragAmount ->
        change.consume()
        offsetY += dragAmount.y

        val startIndex = draggedItemIndex
        val endIndex = (startIndex + (offsetY / (itemHeight + itemSpacing)).roundToInt())
            .coerceIn(0, emoticonPacksState.size - 1)

        if (startIndex != endIndex) {
            emoticonPacksState.swap(startIndex, endIndex)
            draggedItemIndex = endIndex
            offsetY -= (endIndex - startIndex) * (itemHeight + itemSpacing)
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        // 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "내 이모티콘 관리")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 탭 (순서 편집, 숨김 관리)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                onClick = { isVisible = true },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "순서 편집", color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(4.dp))
                    if (isVisible) {
                        Divider(
                            color = Color.Black,
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Spacer(modifier = Modifier.height(1.dp))
                    }
                }
            }

            TextButton(
                onClick = { isVisible = false },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "숨김 관리", color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(4.dp))
                    if (!isVisible) {
                        Divider(
                            color = Color.Black,
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Spacer(modifier = Modifier.height(1.dp))
                    }
                }
            }
        }

        // 이모티콘 리스트
        LazyColumn(state = lazyListState) {
            itemsIndexed(emoticonPacksState) { index, emoticonPack ->
                val isDragging = index == draggedItemIndex

                EmoticonItem(
                    sampleEmoticonPack = emoticonPack,
                    viewModel = viewModel,
                    isDragging = isDragging,
                    offsetY = if (isDragging) offsetY else 0f,
                    isVisible = isVisible,
                    onDragStart = { onDragStart(index) },
                    onDrag = onDrag,
                    onDragEnd = onDragEnd,
                    onDragCancel = onDragCancel,
                    modifier = Modifier
                        .zIndex(if (isDragging) 1f else 0f)
                        .offset { IntOffset(x = 0, y = if (isDragging) offsetY.roundToInt() else 0) }
                )
            }
        }
    }
}

// MutableList의 확장 함수로 swap 기능 추가
private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

@Composable
fun EmoticonItem(
    sampleEmoticonPack: SampleEmoticonPack,
    viewModel: MyEmoticonViewModel,
    isDragging: Boolean,
    offsetY: Float,
    onDragStart: () -> Unit,
    onDrag: (PointerInputChange, Offset) -> Unit,
    onDragEnd: () -> Unit,
    onDragCancel: () -> Unit,
    modifier: Modifier = Modifier,
    isVisible: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 이모티콘 이미지
        Image(
            painter = painterResource(id = sampleEmoticonPack.mainImageResource),
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // 이모티콘 이름 및 공개 여부
        Column(modifier = Modifier.weight(1f)) {
            Text(text = sampleEmoticonPack.name)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(22.dp)
                        .background(
                            color = if (sampleEmoticonPack.isPublic) Color(0xFFF4E845) else Color(0xFFD3D3D3),
                            shape = RoundedCornerShape(9.dp),
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (sampleEmoticonPack.isPublic) "public" else "private",
                        color = Color(0xFF4A4A4A),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                    )
                }
            }
        }

        // 가시성 토글 아이콘
        IconButton(onClick = { viewModel.changeVisible(sampleEmoticonPack) }) {
            Icon(
                imageVector = Icons.Default.VisibilityOff,
                contentDescription = "Toggle Visibility"
            )
        }
        // DragHandle 아이콘에만 드래그 제스처 적용
        if (isVisible) {
            IconButton(
                onClick = { /* 필요하면 클릭 이벤트 처리 */ },
                modifier = Modifier.pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { onDragStart() },
                        onDragEnd = { onDragEnd() },
                        onDragCancel = { onDragCancel() },
                        onDrag = onDrag
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.DragHandle,
                    contentDescription = "Drag"
                )
            }
        }
    }
}