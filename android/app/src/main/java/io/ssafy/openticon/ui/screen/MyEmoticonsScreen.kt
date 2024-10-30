package io.ssafy.openticon.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ssafy.openticon.R
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPack
import io.ssafy.openticon.ui.viewmodel.EmoticonViewModel
import io.ssafy.openticon.ui.viewmodel.MyEmoticonViewModel
import io.ssafy.openticon.ui.viewmodel.SearchScreenViewModel

@Preview
@Composable
fun MyEmoticonsScreen(viewModel: MyEmoticonViewModel = hiltViewModel()) {

    val visibleEmoticonPacks by viewModel.visibleEmoticonPacks.observeAsState(emptyList())
    val invisibleEmoticonPacks by viewModel.invisibleEmoticonPacks.observeAsState(emptyList())

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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                onClick = { isVisible = true },
                modifier =
                if(isVisible)
                    Modifier.background(Color.Gray)
                else
                    Modifier
            ) {
                Text(text = "순서 편집", color = MaterialTheme.colorScheme.primary)
            }
            TextButton(onClick = { isVisible = false },
                if(!isVisible)
                    Modifier.background(Color.Gray)
                else
                    Modifier
            ) {
                Text(text = "숨김 관리", color = MaterialTheme.colorScheme.primary)
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // List of Emoticons
        LazyColumn {
            items(emoticonPacks) { emoticonPack ->
                EmoticonItem(emoticonPack, viewModel)
            }
        }
    }
}

@Composable
fun EmoticonItem(emoticonPack: EmoticonPack, viewModel: MyEmoticonViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Emoticon image
        Image(
            painter = painterResource(id = emoticonPack.mainImageResource),
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Emoticon name and visibility
        Column(modifier = Modifier.weight(1f)) {
            Text(text = emoticonPack.name)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (emoticonPack.isPublic) "public" else "private",

                    modifier = Modifier
                        .background(
                            color = if (emoticonPack.isPublic) Color.Yellow else Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }

        // Visibility and Drag icons
        IconButton(onClick = { viewModel.changeVisible(emoticonPack) }) {
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