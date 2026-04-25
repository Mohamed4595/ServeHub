package com.mohamed.servicehub.presentation.customerScreens.homeScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.mohamed.servicehub.presentation.components.ServeHubInk
import com.mohamed.servicehub.presentation.components.ServeHubMuted
import com.mohamed.servicehub.presentation.Restaurant
import com.mohamed.servicehub.previewRestaurants
import com.mohamed.servicehub.presentation.components.toOneDecimal
import org.jetbrains.compose.resources.painterResource
import servehub.composeapp.generated.resources.Res
import servehub.composeapp.generated.resources.icon_star

@Composable
fun PopularRestaurantCard(
    restaurant: Restaurant,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        elevation = 0.dp,
        backgroundColor = Color.White
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            FoodThumbnail(label = restaurant.name, size = 88.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(restaurant.name, fontWeight = FontWeight.Bold, color = ServeHubInk)
                Spacer(modifier = Modifier.height(4.dp))
                Text(restaurant.cuisine, color = ServeHubMuted, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.icon_star),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${(4.1 + restaurant.menu.size * 0.1).toOneDecimal()}   30-40 min", color = ServeHubMuted, fontSize = 12.sp)
                }
            }
        }
    }
}
@Preview(showBackground = true, backgroundColor = 0xFFF8F8F8)
@Composable
private fun PopularRestaurantCardPreview() = PreviewContainer {
    PopularRestaurantCard(
        restaurant = previewRestaurants.first(),
        onClick = {}
    )
}