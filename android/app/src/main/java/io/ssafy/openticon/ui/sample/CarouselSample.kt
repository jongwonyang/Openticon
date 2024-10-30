package io.ssafy.openticon.ui.sample

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

data class CarouselItem(
    val id: Int,
    @DrawableRes val imageResId: Int,
    @StringRes val contentDescriptionResId: Int
)

val items = EmoticonPackSampleData.latestPacks

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CarouselSample() {
    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { items.count() },
        modifier = Modifier.width(412.dp).height(230.dp),
        preferredItemWidth = 186.dp,
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) { i ->
        val item = items[i]

        Column {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(item.thumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = item.title,
                modifier = Modifier.height(180.dp).maskClip(MaterialTheme.shapes.extraLarge),
                contentScale = ContentScale.Crop
            )
//            Column(
//            ) {
//                Text(
//                    text = item.title,
//                    fontWeight = FontWeight.Bold,
//                    style = MaterialTheme.typography.bodyLarge
//                )
//                Text(
//                    text = item.author,
//                    color = Color.Gray, // 회색으로 설정
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }


        }
    }
}