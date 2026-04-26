package com.mohamed.servicehub.presentation.ownerScreens.dashboardScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mohamed.servicehub.PhonePreview
import com.mohamed.servicehub.PreviewContainer
import com.mohamed.servicehub.presentation.components.PrimaryActionButton
import com.mohamed.servicehub.presentation.components.ServeHubInk
import com.mohamed.servicehub.presentation.components.ServeHubMuted
import com.mohamed.servicehub.presentation.Restaurant
import com.mohamed.servicehub.presentation.RootUiState
import com.mohamed.servicehub.presentation.UserRole
import com.mohamed.servicehub.previewOwner
import com.mohamed.servicehub.previewOwnerState
import com.mohamed.servicehub.previewRestaurants

@Composable
internal fun DashboardScreen(
    state: RootUiState,
    ownedRestaurants: List<Restaurant>,
    onRestaurantClick: (String) -> Unit,
    onManageMenu: (String) -> Unit,
    onCreateRestaurant: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(18.dp)
    ) {
        Text(
            text = "Welcome, Owner \uD83D\uDC4B",
            style = MaterialTheme.typography.h5,
            color = ServeHubInk
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text("Manage your restaurant easily.", color = ServeHubMuted)
        Spacer(modifier = Modifier.height(22.dp))

        DashboardActionCard(
            title = "My Restaurant",
            subtitle = "View and edit your restaurant information",
            icon = Icons.Default.Storefront,
            background = Color(0xFFFFEFE5),
            onClick = {
                ownedRestaurants.firstOrNull()?.let { onRestaurantClick(it.id) }
                    ?: onCreateRestaurant()
            }
        )
        Spacer(modifier = Modifier.height(14.dp))
        DashboardActionCard(
            title = "Manage Menu",
            subtitle = "Add, edit or remove new menu items",
            icon = Icons.Default.Menu,
            background = Color(0xFFEFF9EB),
            onClick = {
                ownedRestaurants.firstOrNull()?.let { onManageMenu(it.id) }
            }
        )
        Spacer(modifier = Modifier.height(14.dp))
        // Show create restaurant option only for owners
        if (state.currentUser?.role == UserRole.STAFF) {
            PrimaryActionButton(text = "Create Restaurant", onClick = onCreateRestaurant)
        }
        Spacer(modifier = Modifier.height(22.dp))
        Text("Your restaurants", fontWeight = FontWeight.Bold, color = ServeHubInk)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(ownedRestaurants.ifEmpty { state.restaurants }) { restaurant ->
                RestaurantMiniCard(
                    restaurant = restaurant,
                    onClick = { onRestaurantClick(restaurant.id) },
                    trailing = if (restaurant.ownerId == state.currentUser?.id) "Active" else "Read only"
                )
            }
        }
    }
}

@PhonePreview
@Composable
private fun DashboardScreenPreview() = PreviewContainer {
    DashboardScreen(
        state = previewOwnerState,
        ownedRestaurants = previewRestaurants.filter { it.ownerId == previewOwner.id },
        onRestaurantClick = {},
        onManageMenu = {},
        onCreateRestaurant = {}
    )
}
