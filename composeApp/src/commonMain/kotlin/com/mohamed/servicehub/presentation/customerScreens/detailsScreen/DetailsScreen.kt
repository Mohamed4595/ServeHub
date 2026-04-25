package com.mohamed.servicehub.presentation.customerScreens.detailsScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamed.servicehub.presentation.components.EmptyCenteredState
import com.mohamed.servicehub.PhonePreview
import com.mohamed.servicehub.PreviewContainer
import com.mohamed.servicehub.presentation.components.ServeHubGold
import com.mohamed.servicehub.presentation.components.ServeHubInk
import com.mohamed.servicehub.presentation.components.ServeHubMuted
import com.mohamed.servicehub.presentation.MenuItem
import com.mohamed.servicehub.presentation.Restaurant
import com.mohamed.servicehub.presentation.RootUiState
import com.mohamed.servicehub.previewCustomerState
import com.mohamed.servicehub.previewRestaurants
import org.jetbrains.compose.resources.painterResource
import servehub.composeapp.generated.resources.Res
import servehub.composeapp.generated.resources.icon_star

@Composable
internal fun DetailsScreen(
    restaurant: Restaurant?,
    cartState: RootUiState,
    onAddToCart: (MenuItem) -> Unit
) {
    if (restaurant == null) {
        EmptyCenteredState("Restaurant not found")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        RestaurantHero(restaurant = restaurant)
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(restaurant.name, style = MaterialTheme.typography.h5, color = ServeHubInk)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "${restaurant.cuisine}, Pizza  •  30-40 min",
                        color = ServeHubMuted,
                        fontSize = 13.sp
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.icon_star),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("4.5 (120)", color = ServeHubGold, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            TabRowLike()
            Spacer(modifier = Modifier.height(18.dp))
            Text("Popular", color = ServeHubInk, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = if (cartState.cart.totalQuantity > 0) 80.dp else 16.dp)
            ) {
                items(restaurant.menu) { item ->
                    MenuItemCard(item = item, onAdd = { onAddToCart(item) })
                }
            }
        }
    }
}

@PhonePreview
@Composable
private fun DetailsScreenPreview() = PreviewContainer {
    DetailsScreen(
        restaurant = previewRestaurants.first(),
        cartState = previewCustomerState,
        onAddToCart = {}
    )
}
