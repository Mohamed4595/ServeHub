package com.mohamed.servicehub.presentation.customerScreens.cartScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mohamed.servicehub.presentation.components.EmptyMenuState
import com.mohamed.servicehub.PhonePreview
import com.mohamed.servicehub.PreviewContainer
import com.mohamed.servicehub.presentation.components.ServeHubAccent
import com.mohamed.servicehub.presentation.components.ServeHubInk
import com.mohamed.servicehub.presentation.MenuItem
import com.mohamed.servicehub.presentation.Restaurant
import com.mohamed.servicehub.presentation.RootUiState
import com.mohamed.servicehub.previewCustomerState
import com.mohamed.servicehub.previewRestaurants
import com.mohamed.servicehub.presentation.components.toCurrency
import org.jetbrains.compose.resources.painterResource
import servehub.composeapp.generated.resources.Res
import servehub.composeapp.generated.resources.icon_call

@Composable
internal fun CartScreen(
    state: RootUiState,
    restaurants: List<Restaurant>,
    onQuantityChange: (MenuItem, Int) -> Unit,
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
                        onMinus = { onQuantityChange(line.menuItem, -1) },
                        onPlus = { onQuantityChange(line.menuItem, 1) }
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
