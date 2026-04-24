package com.servehub.android

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.servehub.shared.Restaurant
import com.servehub.shared.RootUiState
import com.servehub.shared.UserRole

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
                ownedRestaurants.firstOrNull()?.let { onRestaurantClick(it.id) } ?: onCreateRestaurant()
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
        PrimaryActionButton(text = "Create Restaurant", onClick = onCreateRestaurant)
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

@Composable
internal fun CreateRestaurantScreen(
    onCreate: (String, String, String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var cuisine by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    FormLayout(title = "Create Restaurant") {
        ImageUploadPlaceholder(text = "Upload Image\nJPG, PNG up to 5MB")
        Spacer(modifier = Modifier.height(18.dp))
        Text("Restaurant Name", color = ServeHubInk, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        ServeHubTextField(value = name, onValueChange = { name = it }, label = "Enter restaurant name")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Cuisine", color = ServeHubInk, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        ServeHubTextField(value = cuisine, onValueChange = { cuisine = it }, label = "Italian, Burgers...")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Image URL (optional)", color = ServeHubInk, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        ServeHubTextField(value = phone, onValueChange = { phone = it }, label = "Restaurant phone number")
        Spacer(modifier = Modifier.height(26.dp))
        PrimaryActionButton(text = "Save Restaurant", onClick = { onCreate(name, cuisine, phone) })
    }
}

@Composable
internal fun MenuManagementScreen(
    restaurant: Restaurant?,
    userRole: UserRole?,
    onAddItem: () -> Unit,
    onDeleteItem: (String) -> Unit
) {
    if (restaurant == null || userRole != UserRole.OWNER) {
        EmptyCenteredState("You do not have permission to manage this menu.")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(restaurant.name, style = MaterialTheme.typography.h6, color = ServeHubInk)
                Spacer(modifier = Modifier.height(4.dp))
                StatusChip("Active")
            }
            PrimaryMiniButton(text = "+ Add Item", onClick = onAddItem)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("Menu Items", color = ServeHubInk, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(14.dp))
        if (restaurant.menu.isEmpty()) {
            EmptyMenuState(
                title = "No menu items yet",
                subtitle = "Start by adding your first item to the menu."
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(restaurant.menu) { item ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        elevation = 0.dp,
                        backgroundColor = Color.White,
                        modifier = Modifier.border(1.dp, ServeHubBorder, RoundedCornerShape(14.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(item.name, color = ServeHubInk, fontWeight = FontWeight.Medium)
                                Text("${item.price.toCurrency()} EGP", color = ServeHubMuted, fontSize = 12.sp)
                            }
                            IconButton(onClick = { onDeleteItem(item.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = ServeHubPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun AddMenuItemScreen(
    restaurant: Restaurant?,
    onAdd: (String, String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }

    FormLayout(title = "Add Menu Item") {
        ImageUploadPlaceholder(text = "Upload Image\nJPG, PNG up to 5MB")
        Spacer(modifier = Modifier.height(18.dp))
        Text("Item Name", color = ServeHubInk, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        ServeHubTextField(value = name, onValueChange = { name = it }, label = "Enter item name")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Price (EGP)", color = ServeHubInk, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        ServeHubTextField(
            value = price,
            onValueChange = { price = it },
            label = "Enter price",
            keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryActionButton(
            text = "Save Item",
            onClick = { onAdd(name, price) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Adding to ${restaurant?.name ?: "restaurant"}",
            color = ServeHubMuted,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun DashboardActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    background: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = background,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = ServeHubPrimary)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, color = ServeHubInk)
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtitle, color = ServeHubMuted, fontSize = 12.sp)
            }
            Text("›", color = ServeHubInk, fontSize = 22.sp)
        }
    }
}

@Composable
private fun RestaurantMiniCard(
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
