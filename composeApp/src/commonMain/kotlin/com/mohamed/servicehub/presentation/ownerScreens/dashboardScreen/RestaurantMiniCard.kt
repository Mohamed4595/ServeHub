package com.mohamed.servicehub.presentation.ownerScreens.dashboardScreen
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamed.servicehub.presentation.components.FoodThumbnail
import com.mohamed.servicehub.PreviewContainer
import com.mohamed.servicehub.presentation.components.ServeHubBorder
import com.mohamed.servicehub.presentation.components.ServeHubInk
import com.mohamed.servicehub.presentation.components.ServeHubMuted
import com.mohamed.servicehub.presentation.components.StatusChip
import com.mohamed.servicehub.presentation.Restaurant
import com.mohamed.servicehub.previewRestaurants

@Composable
 fun RestaurantMiniCard(
    restaurant: Restaurant,
    onClick: () -> Unit,
    trailing: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White,
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ServeHubBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FoodThumbnail(label = restaurant.name, size = 60.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(restaurant.name, fontWeight = FontWeight.Bold, color = ServeHubInk)
                Text(restaurant.cuisine, color = ServeHubMuted, fontSize = 12.sp)
            }
            StatusChip(trailing)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun RestaurantMiniCardPreview() = PreviewContainer {
    RestaurantMiniCard(
        restaurant = previewRestaurants.first(),
        onClick = {},
        trailing = "Active"
    )
}
