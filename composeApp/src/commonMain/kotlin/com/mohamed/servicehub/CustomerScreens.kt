package com.mohamed.servicehub
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import servehub.composeapp.generated.resources.Res

// Imports for external shared types removed; using internal models instead

@Composable
internal fun HomeScreen(
    restaurants: List<Restaurant>,
    onRestaurantClick: (String) -> Unit
) {
    var search by rememberSaveable { mutableStateOf("") }
    val filtered = restaurants.filter {
        it.name.contains(search, ignoreCase = true) || it.cuisine.contains(search, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ServeHubBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        SearchHeader(search = search, onSearchChange = { search = it })
        Spacer(modifier = Modifier.height(18.dp))
        Text("Popular Restaurants", fontWeight = FontWeight.Bold, color = ServeHubInk)
        Spacer(modifier = Modifier.height(14.dp))
        LazyColumn(
            contentPadding = PaddingValues(bottom = 84.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filtered) { restaurant ->
                PopularRestaurantCard(restaurant = restaurant, onClick = { onRestaurantClick(restaurant.id) })
            }
        }
    }
}

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

@Composable
internal fun CartScreen(
    state: RootUiState,
    restaurants: List<Restaurant>,
    onQuantityChange: (String, Int) -> Unit,
    onCall: (String) -> Unit
) {
    val restaurant = restaurants.firstOrNull { it.id == state.cart.restaurantId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        if (restaurant != null) {
            SourceRestaurantBanner(restaurant.name)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (state.cart.items.isEmpty()) {
            EmptyMenuState(
                title = "Your cart is empty",
                subtitle = "Add items from a restaurant to continue."
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.cart.items) { line ->
                    CartItemCard(
                        itemName = line.menuItem.name,
                        itemPrice = line.menuItem.price,
                        quantity = line.quantity,
                        onMinus = { onQuantityChange(line.menuItem.id, -1) },
                        onPlus = { onQuantityChange(line.menuItem.id, 1) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", fontWeight = FontWeight.Bold, color = ServeHubInk)
                Text("${state.cart.totalPrice.toCurrency()} EGP", fontWeight = FontWeight.Bold, color = ServeHubInk)
            }
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = { restaurant?.let { onCall(it.phoneNumber) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 14.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = ServeHubAccent)
            ) {
                Image(
                    painter = painterResource(Res.drawable.icon_call),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Call Restaurant", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PopularRestaurantCard(
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

@Composable
private fun MenuItemCard(item: MenuItem, onAdd: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp,
        backgroundColor = Color.White,
        modifier = Modifier.border(1.dp, ServeHubBorder, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FoodThumbnail(label = item.name, size = 72.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.Bold, color = ServeHubInk)
                Spacer(modifier = Modifier.height(4.dp))
                Text(menuDescription(item.name), color = ServeHubMuted, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(6.dp))
                Text("${item.price.toCurrency()} EGP", color = ServeHubInk, fontWeight = FontWeight.Bold)
            }
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .clickable(onClick = onAdd),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.icon_add),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun RestaurantHero(restaurant: Restaurant) {
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

@Composable
private fun SourceRestaurantBanner(restaurantName: String) {
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

@Composable
private fun CartItemCard(
    itemName: String,
    itemPrice: Double,
    quantity: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White,
        elevation = 0.dp,
        modifier = Modifier.border(1.dp, ServeHubBorder, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FoodThumbnail(label = itemName, size = 66.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(itemName, fontWeight = FontWeight.Bold, color = ServeHubInk)
                Text("${itemPrice.toCurrency()} EGP", color = ServeHubMuted, fontSize = 12.sp)
            }
            Stepper(quantity = quantity, onMinus = onMinus, onPlus = onPlus)
        }
    }
}

@Composable
private fun TabRowLike() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("Menu", color = ServeHubInk, fontWeight = FontWeight.Bold)
            Text("Info", color = ServeHubMuted)
            Text("Reviews", color = ServeHubMuted)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .height(2.dp)
                .width(58.dp)
                .background(ServeHubPrimary, RoundedCornerShape(10.dp))
        )
    }
}

@PhonePreview
@Composable
private fun HomeScreenPreview() = PreviewContainer {
    HomeScreen(
        restaurants = previewRestaurants,
        onRestaurantClick = {}
    )
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

@PhonePreview
@Composable
private fun CartScreenPreview() = PreviewContainer {
    CartScreen(
        state = previewCustomerState,
        restaurants = previewRestaurants,
        onQuantityChange = { _, _ -> },
        onCall = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8F8)
@Composable
private fun PopularRestaurantCardPreview() = PreviewContainer {
    PopularRestaurantCard(
        restaurant = previewRestaurants.first(),
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun MenuItemCardPreview() = PreviewContainer {
    MenuItemCard(
        item = previewMenuItems.first(),
        onAdd = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 220)
@Composable
private fun RestaurantHeroPreview() = PreviewContainer {
    RestaurantHero(restaurant = previewRestaurants.first())
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SourceRestaurantBannerPreview() = PreviewContainer {
    SourceRestaurantBanner(restaurantName = previewRestaurants.first().name)
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CartItemCardPreview() = PreviewContainer {
    CartItemCard(
        itemName = previewMenuItems.first().name,
        itemPrice = previewMenuItems.first().price,
        quantity = 2,
        onMinus = {},
        onPlus = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TabRowLikePreview() = PreviewContainer {
    TabRowLike()
}
