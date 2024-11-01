package io.ssafy.openticon.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.ssafy.openticon.data.model.SampleEmoticonPack
import io.ssafy.openticon.ui.viewmodel.MyEmoticonViewModel

@Preview(showBackground = true)
@Composable
fun MyEmoticonsScreen(viewModel: MyEmoticonViewModel = hiltViewModel()) {

    val visibleEmoticonPacks by viewModel.visibleSampleEmoticonPacks.observeAsState(emptyList())
    val invisibleEmoticonPacks by viewModel.invisibleSampleEmoticonPacks.observeAsState(emptyList())

    var isVisible by remember { mutableStateOf(true) }

    val emoticonPacks by remember {
        derivedStateOf { if (isVisible) visibleEmoticonPacks else invisibleEmoticonPacks }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "내 이모티콘 관리")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs (Sort Edit and Hide Management)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                onClick = { isVisible = true },
                modifier = Modifier
                    .weight(1f) // 버튼이 Row의 절반을 차지하게 함
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
                        Spacer(modifier = Modifier.height(1.dp)) // 빈 공간 유지
                    }
                }
            }

            TextButton(
                onClick = { isVisible = false },
                modifier = Modifier
                    .weight(1f) // 버튼이 Row의 절반을 차지하게 함
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
                        Spacer(modifier = Modifier.height(1.dp)) // 빈 공간 유지
                    }
                }
            }
        }

        // List of Emoticons
        LazyColumn {
            items(emoticonPacks) { emoticonPack ->
                EmoticonItem(emoticonPack, viewModel)
            }
        }
    }
}


@Composable
fun EmoticonItem(sampleEmoticonPack: SampleEmoticonPack, viewModel: MyEmoticonViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Emoticon image
        Image(
            painter = painterResource(id = sampleEmoticonPack.mainImageResource),
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Emoticon name and visibility
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
                    contentAlignment = Alignment.Center // 배경의 가운데에 텍스트 배치
                ) {
                    Text(
                        text = if (sampleEmoticonPack.isPublic) "public" else "private",
                        color = Color(0xFF4A4A4A),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp, // Text에만 패딩 적용
                    )
                }
            }


        }

        // Visibility and Drag icons
        IconButton(onClick = { viewModel.changeVisible(sampleEmoticonPack) }) {
            Icon(
                imageVector = Icons.Default.VisibilityOff,
                contentDescription = "Toggle Visibility"
            )
        }
        IconButton(onClick = { /* Handle drag action */ }) {
            Icon(
                imageVector = Icons.Default.DragHandle,
                contentDescription = "Drag"
            )
        }
    }
}