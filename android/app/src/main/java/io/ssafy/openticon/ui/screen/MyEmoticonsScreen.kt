package io.ssafy.openticon.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import io.ssafy.openticon.data.model.EmoticonPackEntity
import io.ssafy.openticon.ui.viewmodel.MyEmoticonViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyEmoticonsScreen(
    navController: NavController,
    viewModel: MyEmoticonViewModel = hiltViewModel()
) {
    val visibleEmoticonPacks by viewModel.visibleSampleEmoticonPacks.observeAsState(emptyList())
    val invisibleEmoticonPacks by viewModel.invisibleSampleEmoticonPacks.observeAsState(emptyList())

    var isVisible by remember { mutableStateOf(true) }

    val emoticonPacksState = remember { mutableStateListOf<EmoticonPackEntity>() }

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
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val itemHeightPx = with(density) { 66.dp.toPx() }

    // 상태 초기화를 위한 함수
    fun resetDragState() {
        draggedItemIndex = -1
        offsetY = 0f
    }

    val onDragEndOrCancel: () -> Unit = {
        // 드래그가 끝나거나 취소될 때 호출
        if (draggedItemIndex != -1) {
            // 변경된 리스트를 ViewModel에 업데이트
            viewModel.updateEmoticonPacks(emoticonPacksState.toList())
        }
        resetDragState()
    }

    // 람다를 익명 함수로 변경하여 'return' 사용
    val onDrag: (PointerInputChange, Offset) -> Unit = fun(change, dragAmount) {
        if (change.positionChange() != Offset.Zero) change.consume()

        if (draggedItemIndex == -1) return

        // 민감도 조절 (예: dragAmount.y / 2)
        offsetY += dragAmount.y

        // 리스트 경계를 넘지 않도록 제한
        val maxOffsetY = (emoticonPacksState.size - 1 - draggedItemIndex) * itemHeightPx
        val minOffsetY = -draggedItemIndex * itemHeightPx
        offsetY = offsetY.coerceIn(minOffsetY, maxOffsetY)

        val startIndex = draggedItemIndex
        val offsetItem = (offsetY / itemHeightPx).toInt()
        val endIndex = (startIndex + offsetItem).coerceIn(0, emoticonPacksState.size - 1)

        if (startIndex != endIndex) {
            emoticonPacksState.swap(startIndex, endIndex)
            draggedItemIndex = endIndex
            offsetY -= (endIndex - startIndex) * itemHeightPx
        }

        // 자동 스크롤
        val viewportHeight = lazyListState.layoutInfo.viewportEndOffset
        val itemInfo = lazyListState.layoutInfo.visibleItemsInfo.find { it.index == draggedItemIndex }
        val itemOffset = itemInfo?.offset ?: 0
        val itemEnd = itemOffset + itemHeightPx

        coroutineScope.launch {
            if (itemEnd + offsetY > viewportHeight) {
                lazyListState.scrollBy(itemHeightPx)
            } else if (itemOffset + offsetY < 0f) {
                lazyListState.scrollBy(-itemHeightPx)
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        // 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "내 이모티콘 관리",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 탭 (순서 편집, 숨김 관리)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (isVisible) {
                FilledIconButton(
                    onClick = { isVisible = true },
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 4.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "순서 편집",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontSize = 16.sp
                            ),
                        )
                    }
                }
            } else {
                TextButton(
                    onClick = { isVisible = true },
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 4.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "순서 편집",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontSize = 16.sp
                            ),
                        )
                    }
                }
            }
            if (isVisible) {
                TextButton(
                    onClick = { isVisible = false },
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 4.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "숨긴 이모티콘",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontSize = 16.sp
                            ),
                        )
                    }
                }
            } else {
                FilledIconButton(
                    onClick = { isVisible = false },
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 4.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "숨긴 이모티콘",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontSize = 16.sp
                            ),
                        )
                    }
                }
            }
        }

        // 이모티콘 리스트
        LazyColumn(state = lazyListState) {
            itemsIndexed(emoticonPacksState) { index, emoticonPack ->
                val isDragging = index == draggedItemIndex

                val itemModifier = Modifier
                    .zIndex(if (isDragging) 1f else 0f)
                    .offset {
                        IntOffset(
                            x = 0,
                            y = if (isDragging) offsetY.roundToInt() else 0
                        )
                    }

                EmoticonItem(
                    sampleEmoticonPack = emoticonPack,
                    viewModel = viewModel,
                    isVisible = isVisible,
                    modifier = itemModifier,
                    navController = navController,
                    index = index,
                    onDragStart = { draggedItemIndex = index },
                    onDrag = onDrag,
                    onDragEndOrCancel = onDragEndOrCancel
                )
            }
            item {
                Spacer(modifier = Modifier.height(128.dp))
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
    sampleEmoticonPack: EmoticonPackEntity,
    viewModel: MyEmoticonViewModel,
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    navController: NavController,
    index: Int,
    onDragStart: () -> Unit,
    onDrag: (PointerInputChange, Offset) -> Unit,
    onDragEndOrCancel: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 이모티콘 이미지
        Image(
            painter = rememberAsyncImagePainter(model = sampleEmoticonPack.thumbnail),
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // 이모티콘 이름 및 공개 여부
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    navController.navigate("emoticonPack/${sampleEmoticonPack.uuid}")
                }
        ) {
            Text(text = sampleEmoticonPack.title)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(22.dp)
                        .background(
                            color = if (sampleEmoticonPack.isPublic) Color(0xFFF4E845) else Color(
                                0xFFD3D3D3
                            ),
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
        if (isVisible) {
            IconButton(onClick = { viewModel.changeVisible(sampleEmoticonPack) }) {
                Icon(
                    imageVector = Icons.Default.VisibilityOff,
                    contentDescription = "Toggle Visibility"
                )
            }
        }
        // DragHandle 아이콘에 드래그 제스처 적용
        if (isVisible) {
            IconButton(
                onClick = { /* 필요하면 클릭 이벤트 처리 */ },
                modifier = Modifier.pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            onDragStart()
                        },
                        onDragEnd = onDragEndOrCancel,
                        onDragCancel = onDragEndOrCancel,
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
