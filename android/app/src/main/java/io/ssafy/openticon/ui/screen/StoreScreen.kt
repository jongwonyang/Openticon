import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ssafy.openticon.R
import io.ssafy.openticon.ui.sample.CarouselSample
import io.ssafy.openticon.ui.sample.EmoticonPackSampleData
import kotlinx.coroutines.launch
import kotlin.math.abs

data class ItemData(val imageRes: Int, val title: String, val author: String)
@Composable
fun StoreScreen() {
    val baseItems = listOf(
        ItemData(R.drawable.google, "이모티콘 제목 1", "작성자 1"),
        ItemData(R.drawable.kakao, "이모티콘 제목 2", "작성자 2"),
        ItemData(R.drawable.naver, "이모티콘 제목 3", "작성자 3"),
        ItemData(R.drawable.google, "이모티콘 제목 4", "작성자 4"),
        ItemData(R.drawable.kakao, "이모티콘 제목 5", "작성자 5"),
        ItemData(R.drawable.naver, "이모티콘 제목 6", "작성자 6")
    )

    val items = List(5) { baseItems }.flatten()
    var centerIndex by remember { mutableStateOf(baseItems.size) } // 중앙 인덱스로 초기화
    val listState = rememberLazyListState(centerIndex)
    val coroutineScope = rememberCoroutineScope()

    // 스크롤 상태를 추적하여 가장 가까운 항목으로 스크롤
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val viewportCenter = listState.layoutInfo.viewportEndOffset / 2
            val closestItem = listState.layoutInfo.visibleItemsInfo.minByOrNull { item ->
                abs((item.offset + item.size / 2) - viewportCenter)
            }
            closestItem?.let {
                centerIndex = it.index
                coroutineScope.launch {
                    listState.animateScrollToItem(centerIndex, scrollOffset = -viewportCenter + it.size / 2)
                }
            }
        }
    }
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
        // 신규 섹션 (Header)
        item {
            Spacer(modifier = Modifier.height(50.dp))
            LazyRow(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemsIndexed(items) { index, item ->
                    val center = listState.layoutInfo.viewportEndOffset / 2
                    val itemOffset = listState.layoutInfo.visibleItemsInfo
                        .find { it.index == index }?.offset ?: 0
                    val offset = abs(center - itemOffset)

                    val scale by animateFloatAsState(
                        targetValue = if (index == centerIndex) 1.3f else 0.8f
                    )
                    val alpha by animateFloatAsState(
                        targetValue = if (index == centerIndex) 1f else 0.5f
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .scale(scale)
                            .alpha(alpha)
                            .width(150.dp)
                            .clickable { centerIndex = index }
                    ) {
                        Image(
                            painter = painterResource(id = item.imageRes),
                            contentDescription = "Carousel Image",
                            modifier = Modifier.size(150.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = item.author,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
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
                items(2) { outerIndex -> // LazyRow에서 2개의 Column만 표시
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 0.dp)
                            .width(350.dp) // Column의 너비 설정
                    ) {
                        // Column 내에서 3개의 Row를 표시
                        (1..3).forEach { innerIndex ->
                            val rank = outerIndex * 3 + innerIndex // 순위를 계산
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(vertical = 2.dp)
                                    .fillMaxWidth()
                            ) {
                                // 이모티콘 이미지
                                Image(
                                    painter = painterResource(id = R.drawable.otter), // 실제 이미지 리소스로 대체하세요
                                    contentDescription = "인기 이모티콘 이미지",
                                    modifier = Modifier.size(60.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                // 순위 번호
                                Text(
                                    text = "$rank",
                                    color = Color(0xFF7B82FF),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 30.sp, // 폰트 크기 설정
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.width(8.dp))

                                // 이모티콘 제목과 작성자 정보
                                Column {
                                    Text(
                                        text = "이모티콘 제목 $rank",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "작성자 이름", // 실제 작성자 이름으로 대체하세요
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

        // 가짜 데이터 -> 실제 데이터로 교체 필요
        val tags = listOf("#귀여운", "#고양이", "#강아지")
        val images = listOf(
            listOf(R.drawable.google, R.drawable.kakao, R.drawable.naver, R.drawable.google, R.drawable.kakao, R.drawable.naver),
            listOf(R.drawable.google, R.drawable.kakao, R.drawable.naver, R.drawable.google, R.drawable.kakao, R.drawable.naver),
            listOf(R.drawable.google, R.drawable.kakao, R.drawable.naver, R.drawable.google, R.drawable.kakao, R.drawable.naver)
        )

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
                            .heightIn(max = 100.dp) // 높이 제한 추가
                    ) {
                        items(images[index].size) { imgIndex ->
                            Image(
                                painter = painterResource(id = images[index][imgIndex]),
                                contentDescription = "태그 이모티콘 이미지",
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(end = 8.dp)
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                        }
                    }
                }
            }
        }
    }
}
