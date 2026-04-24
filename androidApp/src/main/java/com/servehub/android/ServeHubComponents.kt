package com.servehub.android

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.servehub.shared.AppScreen

@Composable
internal fun ServeHubTopBar(
    screen: AppScreen,
    cartCount: Int,
    onBack: () -> Unit,
    onCart: () -> Unit
) {
    val title = when (screen) {
        AppScreen.Login -> ""
        AppScreen.Home -> "Cairo, Egypt"
        AppScreen.Dashboard -> ""
        AppScreen.Cart -> "Cart"
        AppScreen.CreateRestaurant -> "Create Restaurant"
        is AppScreen.AddMenuItem -> "Add Menu Item"
        is AppScreen.MenuManagement -> "Manage Menu"
        is AppScreen.Details -> ""
        AppScreen.Splash -> ""
    }

    TopAppBar(
        backgroundColor = Color.White,
        contentColor = ServeHubInk,
        elevation = 0.dp,
        title = {
            if (title.isNotBlank()) {
                Text(
                    text = title,
                    color = ServeHubInk,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                )
            }
        },
        navigationIcon = when (screen) {
            AppScreen.Home -> null
            AppScreen.Login -> null
            AppScreen.Splash -> null
            AppScreen.Dashboard -> {
                {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = null, tint = ServeHubInk)
                    }
                }
            }

            else -> {
                {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = ServeHubInk)
                    }
                }
            }
        },
        actions = {
            when (screen) {
                AppScreen.Home, AppScreen.Dashboard -> NotificationAction()
                is AppScreen.Details -> {
                    IconButton(onClick = onCart) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = ServeHubInk)
                    }
                    FavoriteAction()
                }

                AppScreen.Cart -> {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = ServeHubInk)
                    }
                }

                else -> {
                    if (cartCount > 0) {
                        IconButton(onClick = onCart) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = ServeHubInk)
                        }
                    }
                }
            }
        }
    )
}

@Composable
internal fun CustomerBottomBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BottomNavItem(icon = Icons.Default.Home, label = "Home", selected = true)
        BottomNavItem(icon = Icons.Default.Search, label = "Search", selected = false)
        BottomNavItem(icon = Icons.Default.ShoppingCart, label = "Cart", selected = false, badge = "2")
        BottomNavItem(icon = Icons.Default.PersonOutline, label = "Profile", selected = false)
    }
}

@Composable
internal fun DetailsCartBar(
    totalItems: Int,
    totalPrice: Double,
    onCart: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ServeHubPrimary)
            .navigationBarsPadding()
            .clickable(onClick = onCart)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("View Cart", color = Color.White, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        }
        Text("$totalItems items  •  ${totalPrice.toCurrency()} EGP", color = Color.White, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
    }
}

@Composable
internal fun ClearCartDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = null,
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .background(Color(0xFFFFF3E5), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.WarningAmber, contentDescription = null, tint = Color(0xFFFFB020))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Clear cart?", color = ServeHubInk, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "You already have items from Pizza Place.\nDo you want to clear your cart and add items from another restaurant?",
                    color = ServeHubMuted,
                    textAlign = TextAlign.Center
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("No, Keep It", color = ServeHubInk)
                }
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ServeHubPrimary)
                ) {
                    Text("Yes, Clear", color = Color.White)
                }
            }
        }
    )
}

@Composable
internal fun SearchHeader(search: String, onSearchChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = search,
            onValueChange = onSearchChange,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(14.dp),
            colors = outlinedFieldColors(),
            placeholder = { Text("Search restaurants...", color = ServeHubMuted) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ServeHubMuted) }
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .border(1.dp, ServeHubBorder, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Tune, contentDescription = null, tint = ServeHubInk)
        }
    }
}

@Composable
internal fun FormLayout(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(title, style = MaterialTheme.typography.h6, color = ServeHubInk)
        Spacer(modifier = Modifier.height(22.dp))
        content()
    }
}

@Composable
internal fun ImageUploadPlaceholder(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(148.dp)
            .border(1.dp, ServeHubPrimary.copy(alpha = 0.5f), RoundedCornerShape(18.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Image, contentDescription = null, tint = ServeHubPrimary, modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text, color = ServeHubPrimary, textAlign = TextAlign.Center)
        }
    }
}

@Composable
internal fun ServeHubTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leading: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        placeholder = { Text(label, color = ServeHubMuted) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        leadingIcon = leading,
        colors = outlinedFieldColors()
    )
}

@Composable
internal fun PrimaryActionButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = ServeHubPrimary)
    ) {
        Text(text, color = Color.White, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
    }
}

@Composable
internal fun OutlineActionButton(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(text, color = ServeHubInk, fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
    }
}

@Composable
internal fun PrimaryMiniButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = ServeHubPrimary),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, color = Color.White, fontSize = 12.sp)
    }
}

@Composable
internal fun OrDivider() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f), color = ServeHubBorder)
        Text("  or  ", color = ServeHubMuted)
        Divider(modifier = Modifier.weight(1f), color = ServeHubBorder)
    }
}

@Composable
internal fun LogoBadge(size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Storefront,
            contentDescription = null,
            tint = ServeHubPrimary,
            modifier = Modifier.size(size * 0.72f)
        )
    }
}

@Composable
internal fun ServeHubWordmark() {
    Row(verticalAlignment = Alignment.Bottom) {
        Text("Serve", color = ServeHubInk, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 22.sp)
        Text("Hub", color = ServeHubPrimary, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 22.sp)
    }
}

@Composable
internal fun FoodThumbnail(label: String, size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(heroColorsFor(label))),
        contentAlignment = Alignment.Center
    ) {
        Text(foodEmojiFor(label), fontSize = (size.value * 0.4f).sp)
    }
}

@Composable
internal fun Stepper(quantity: Int, onMinus: () -> Unit, onPlus: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StepperButton(text = "-", onClick = onMinus)
        Text(quantity.toString(), color = ServeHubInk, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        StepperButton(text = "+", onClick = onPlus)
    }
}

@Composable
private fun StepperButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF6F6F6))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = ServeHubInk, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
    }
}

@Composable
internal fun StatusChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color(0xFFE9F8DE))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, color = Color(0xFF5D9E39), fontSize = 11.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
    }
}

@Composable
private fun NotificationAction() {
    Box(
        modifier = Modifier
            .padding(end = 12.dp)
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0xFFF7F7F7)),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = ServeHubInk)
    }
}

@Composable
private fun FavoriteAction() {
    Box(
        modifier = Modifier
            .padding(end = 12.dp)
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0x55000000)),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Outlined.FavoriteBorder, contentDescription = null, tint = Color.White)
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    badge: String? = null
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            Icon(
                icon,
                contentDescription = null,
                tint = if (selected) ServeHubPrimary else ServeHubMuted
            )
            if (badge != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(14.dp)
                        .background(ServeHubError, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(badge, color = Color.White, fontSize = 8.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = if (selected) ServeHubPrimary else ServeHubMuted, fontSize = 11.sp)
    }
}

@Composable
internal fun EmptyMenuState(title: String, subtitle: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = Color.White,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("\uD83D\uDECE️", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, color = ServeHubInk, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(subtitle, color = ServeHubMuted, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(18.dp))
            PrimaryMiniButton(text = "+ Add Item", onClick = {})
        }
    }
}

@Composable
internal fun EmptyCenteredState(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text, color = ServeHubMuted, textAlign = TextAlign.Center)
    }
}

@Composable
internal fun outlinedFieldColors() = TextFieldDefaults.outlinedTextFieldColors(
    backgroundColor = Color.White,
    textColor = ServeHubInk,
    focusedBorderColor = ServeHubPrimary,
    unfocusedBorderColor = ServeHubBorder,
    cursorColor = ServeHubPrimary
)

internal val ServeHubPrimary = Color(0xFFFF6B1A)
internal val ServeHubInk = Color(0xFF1E1E1E)
internal val ServeHubMuted = Color(0xFF8C8C8C)
internal val ServeHubBorder = Color(0xFFE7E7E7)
internal val ServeHubBackground = Color(0xFFF8F8F8)
internal val ServeHubAccent = Color(0xFF4CAF50)
internal val ServeHubError = Color(0xFFE53935)
internal val ServeHubGold = Color(0xFFFFB81A)

internal fun foodEmojiFor(label: String): String {
    val lower = label.lowercase()
    return when {
        "pizza" in lower -> "🍕"
        "burger" in lower -> "🍔"
        "sushi" in lower -> "🍣"
        "salad" in lower || "bowl" in lower -> "🥗"
        "fries" in lower -> "🍟"
        "shawarma" in lower || "grill" in lower -> "🥙"
        else -> "🍽️"
    }
}

internal fun heroColorsFor(label: String): List<Color> {
    val lower = label.lowercase()
    return when {
        "pizza" in lower -> listOf(Color(0xFF2C1409), Color(0xFFB15B16), Color(0xFFF0A94B))
        "burger" in lower -> listOf(Color(0xFF2E1E12), Color(0xFF97571C), Color(0xFFE1B35D))
        "sushi" in lower -> listOf(Color(0xFF1C2326), Color(0xFF4D6F73), Color(0xFFD98A54))
        else -> listOf(Color(0xFF382012), Color(0xFFB16426), Color(0xFFFFB673))
    }
}

internal fun menuDescription(itemName: String): String = when {
    "pizza" in itemName.lowercase() -> "Fresh tomatoes, mozzarella, basil"
    "burger" in itemName.lowercase() -> "Grilled chicken, lettuce, mayo"
    "fries" in itemName.lowercase() -> "Crispy golden fries"
    else -> "Chef-made favorite with bold flavor"
}

internal fun Double.toCurrency(): String = String.format("%.0f", this)

internal fun Double.toOneDecimal(): String = String.format("%.1f", this)
