package com.mohamed.servicehub.presentation.customerScreens.cartScreen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohamed.servicehub.PreviewContainer
import com.mohamed.servicehub.presentation.components.ServeHubInk
import com.mohamed.servicehub.presentation.components.ServeHubPrimary
import com.mohamed.servicehub.previewRestaurants

@Composable
 fun SourceRestaurantBanner(restaurantName: String) {
    Card(
        backgroundColor = Color(0xFFFFF5EB),
        shape = RoundedCornerShape(14.dp),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Your cart is from\n$restaurantName", color = ServeHubInk)
            Text("Change", color = ServeHubPrimary, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SourceRestaurantBannerPreview() = PreviewContainer {
    SourceRestaurantBanner(restaurantName = previewRestaurants.first().name)
}