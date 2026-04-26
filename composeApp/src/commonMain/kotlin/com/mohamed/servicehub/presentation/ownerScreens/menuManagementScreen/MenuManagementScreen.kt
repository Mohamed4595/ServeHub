package com.mohamed.servicehub.presentation.ownerScreens.menuManagementScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
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
import com.mohamed.servicehub.presentation.components.EmptyMenuState
import com.mohamed.servicehub.PhonePreview
import com.mohamed.servicehub.PreviewContainer
import com.mohamed.servicehub.presentation.components.PrimaryMiniButton
import com.mohamed.servicehub.presentation.components.ServeHubBorder
import com.mohamed.servicehub.presentation.components.ServeHubInk
import com.mohamed.servicehub.presentation.components.ServeHubMuted
import com.mohamed.servicehub.presentation.components.StatusChip
import com.mohamed.servicehub.presentation.Restaurant
import com.mohamed.servicehub.presentation.UserRole
import com.mohamed.servicehub.previewOwner
import com.mohamed.servicehub.previewRestaurants
import com.mohamed.servicehub.presentation.components.toCurrency
import org.jetbrains.compose.resources.painterResource
import servehub.composeapp.generated.resources.Res
import servehub.composeapp.generated.resources.icon_delete

@Composable
internal fun MenuManagementScreen(
    restaurant: Restaurant?,
    userRole: UserRole?,
    onAddItem: () -> Unit,
    onDeleteItem: (String) -> Unit
) {
    if (restaurant == null || userRole != UserRole.STAFF) {
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
                                Image(
                                    painter = painterResource(Res.drawable.icon_delete),
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@PhonePreview
@Composable
private fun MenuManagementScreenPreview() = PreviewContainer {
    MenuManagementScreen(
        restaurant = previewRestaurants.first(),
        userRole = previewOwner.role,
        onAddItem = {},
        onDeleteItem = {}
    )
}
