package io.ssafy.openticon.ui.screen

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.ssafy.openticon.R
import io.ssafy.openticon.ui.sample.CarouselSample
import io.ssafy.openticon.ui.sample.EmoticonPackSampleData
import kotlinx.coroutines.launch
import kotlin.math.abs
import coil.compose.rememberImagePainter
import io.ssafy.openticon.ui.viewmodel.StoreViewModel


data class ItemData(val imageRes: Int, val title: String, val author: String)
@Composable
fun StoreScreen(viewModel: StoreViewModel = hiltViewModel(),
                navController : NavController) {
    val newEmoticonPack by viewModel.searchResult.collectAsState()
    val popularEmoticonPack by viewModel.popularEmoticonPack.collectAsState()
    val tag1EmoticonPack by viewModel.tag1EmoticonPack.collectAsState()
    val tag2EmoticonPack by viewModel.tag2EmoticonPack.collectAsState()
    val tag3EmoticonPack by viewModel.tag3EmoticonPack.collectAsState()
    val tagQuery1 by viewModel.tagQuery1.collectAsState()
    val tagQuery2 by viewModel.tagQuery2.collectAsState()
    val tagQuery3 by viewModel.tagQuery3.collectAsState()

//    val items = listOf(
//        ItemData(R.drawable.empty, "", ""), // 왼쪽 빈 이미지
//        ItemData(R.drawable.google, "이모티콘 제목 1", "작성자 1"),
//        ItemData(R.drawable.kakao, "이모티콘 제목 2", "작성자 2"),
//        ItemData(R.drawable.naver, "이모티콘 제목 3", "작성자 3"),
//        ItemData(R.drawable.google, "이모티콘 제목 4", "작성자 4"),
//        ItemData(R.drawable.kakao, "이모티콘 제목 5", "작성자 5"),
//        ItemData(R.drawable.naver, "이모티콘 제목 6", "작성자 6"),
//        ItemData(R.drawable.empty, "", "") // 오른쪽 빈 이미지
//    )

    var centerIndex by remember { mutableStateOf(3) }
    val listState = rememberLazyListState(centerIndex)
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    LaunchedEffect(listState.isScrollInProgress,Unit) {
        if (!listState.isScrollInProgress) {
            val viewportCenter = listState.layoutInfo.viewportEndOffset / 2
            val closestItem = listState.layoutInfo.visibleItemsInfo.minByOrNull { item ->
                abs((item.offset + item.size / 2) - viewportCenter)
            }

            closestItem?.let { item ->
                if (centerIndex != item.index) {
                    centerIndex = item.index
                    coroutineScope.launch {
                        listState.animateScrollToItem(centerIndex, scrollOffset = -viewportCenter + item.size / 2)
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val viewportCenter = listState.layoutInfo.viewportEndOffset / 2
            val itemSize = (viewportCenter/1.4).toInt()
            listState.scrollToItem(centerIndex, scrollOffset = -viewportCenter + itemSize / 2)
        }
    }

//    LaunchedEffect(Unit) {
//        val itemWidth = 150
//        val viewportWidth = listState.layoutInfo.viewportSize.width
//        listState.scrollToItem(centerIndex)
//        with(LocalDensity) {
//            val offset = (viewportWidth / 2) - (itemWidth / 2)
//            listState.animateScrollToItem(centerIndex, scrollOffset = offset.toInt())
//        }
//    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        item {
            Text(
                text = "신규",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(50.dp))
            LazyRow(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemsIndexed(newEmoticonPack) { index, item -> // Use itemsIndexed to get the index and item
                    Card(
                        shape = RoundedCornerShape(16.dp), // 둥근 모서리 적용
                        border = BorderStroke(1.dp, Color.LightGray), // 테두리 색상과 두께 설정
                        colors = CardDefaults.cardColors(containerColor = Color.White), // 카드 배경색
                        modifier = Modifier
                            .scale(if (index == centerIndex) 1.3f else 0.8f)
                            .alpha(if (index == centerIndex) 1f else 0.5f)
                            .width(150.dp)
                            .padding(bottom = 1.dp)
                            .clickable {
                                if (index == centerIndex) {
                                    navController.navigate("emoticonPack/${item.id}")
                                } else {
                                    centerIndex = index
                                    coroutineScope.launch {
                                        val viewportCenter = listState.layoutInfo.viewportSize.width / 2
                                        val itemSize = listState.layoutInfo.visibleItemsInfo.find { it.index == centerIndex }?.size ?: 0
                                        listState.animateScrollToItem(centerIndex, scrollOffset = -viewportCenter + itemSize / 2)
                                    }
                                }
                            }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(0.dp) // 카드 내부 여백 설정
                        ) {
                            Image(
                                painter = rememberImagePainter(data = item.thumbnail),
                                contentDescription = "New Emoticon Pack Image",
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(RoundedCornerShape(16.dp)) // 이미지에 둥근 모서리 추가
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = item.author,
                                color = Color.Gray,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }

        // 인기 섹션
        item {
            Text(
                text = "인기",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // 인기 항목 리스트
        item {
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                modifier = Modifier.padding(vertical = 0.dp)
            ) {
                val chunkedPopularItems = popularEmoticonPack.chunked(3)
                items(chunkedPopularItems) { chunk ->
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 0.dp)
                            .width(350.dp)
                            .background(MaterialTheme.colorScheme.surfaceContainer)

                    ) {
                        chunk.forEachIndexed { index, item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(vertical = 2.dp)
                                    .fillMaxWidth()
                                    .clickable { navController.navigate("emoticonPack/${item.id}") } // Handle click event
                            ) {
                                Image(
                                    painter = rememberImagePainter(data = item.thumbnail), // Use the item's thumbnail
                                    contentDescription = "인기 이모티콘 이미지",
                                    modifier = Modifier.size(60.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "${(chunkedPopularItems.indexOf(chunk) * 3) + (index + 1)}", // Calculate rank
                                    color = Color(0xFF7B82FF),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 30.sp,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.width(8.dp))

                                Column {
                                    Text(
                                        text = item.title, // Use the item's title
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = item.author, // Use the item's author
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }



        // 태그 섹션
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "태그",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        val tags = listOf("#"+tagQuery1, "#"+tagQuery2, "#"+tagQuery3)
        val emoticonPacks = listOf(tag1EmoticonPack, tag2EmoticonPack, tag3EmoticonPack)

        tags.forEachIndexed { index, tag ->
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(Color.LightGray)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    LazyRow(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .heightIn(max = 100.dp)
                    ) {
                        items(emoticonPacks[index]) { item -> // Use the corresponding emoticon pack
                            Image(
                                painter = rememberImagePainter(data = item.thumbnail), // Use the item's thumbnail
                                contentDescription = "태그 이모티콘 이미지",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp)) // 모든 모서리를 둥글게 설정
//                                    .border(2.dp, Color.Gray, RoundedCornerShape(16.dp)) // 테두리 추가
                                    .size(80.dp)
//                                    .padding(end = 8.dp)
                                    .clickable { navController.navigate("emoticonPack/${item.id}") } // Navigate on click
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }

                    }
                }
            }
        }

    }
}