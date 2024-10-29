package io.ssafy.openticon.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.ssafy.openticon.ui.sample.CarouselSample
import io.ssafy.openticon.ui.sample.EmoticonPackSampleData

@Preview(showBackground = false)
@Composable
fun StoreScreen() {
    val scrollState = rememberScrollState()
    val latestPacks = EmoticonPackSampleData.latestPacks

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Row(
            Modifier.padding(16.dp)
        ) {
            Text(
                text = "신규",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        CarouselSample()
    }
}