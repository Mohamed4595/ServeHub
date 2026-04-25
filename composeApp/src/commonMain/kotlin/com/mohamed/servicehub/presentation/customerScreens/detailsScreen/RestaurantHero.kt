package com.mohamed.servicehub.presentation.customerScreens.detailsScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamed.servicehub.PreviewContainer
import com.mohamed.servicehub.presentation.components.foodEmojiFor
import com.mohamed.servicehub.presentation.components.heroColorsFor
import com.mohamed.servicehub.presentation.Restaurant
import com.mohamed.servicehub.previewRestaurants

@Composable
 fun RestaurantHero(restaurant: Restaurant) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(
                Brush.linearGradient(
                    colors = heroColorsFor(restaurant.name)
                )
            )
    ) {
        Text(
            text = foodEmojiFor(restaurant.name),
            modifier = Modifier.align(Alignment.Center),
            fontSize = 90.sp
        )
    }
}
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 220)
@Composable
private fun RestaurantHeroPreview() = PreviewContainer {
    RestaurantHero(restaurant = previewRestaurants.first())
}