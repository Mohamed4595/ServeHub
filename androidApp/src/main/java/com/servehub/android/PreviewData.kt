package com.servehub.android

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.servehub.shared.CartLineItem
import com.servehub.shared.CartState
import com.servehub.shared.MenuItem
import com.servehub.shared.Restaurant
import com.servehub.shared.RootUiState
import com.servehub.shared.UserRole
import com.servehub.shared.UserSession

internal val previewMenuItems = listOf(
    MenuItem(id = "m1", name = "Margherita Pizza", price = 80.0),
    MenuItem(id = "m2", name = "Pepperoni Pizza", price = 95.0),
    MenuItem(id = "m3", name = "Chicken Burger", price = 120.0),
    MenuItem(id = "m4", name = "French Fries", price = 30.0)
)

internal val previewRestaurants = listOf(
    Restaurant(
        id = "rest-1",
        name = "Pizza Place",
        cuisine = "Italian, Pizza",
        phoneNumber = "+201001112223",
        ownerId = "owner@servehub.app",
        ownerEmail = "owner@servehub.app",
        menu = previewMenuItems
    ),
    Restaurant(
        id = "rest-2",
        name = "Burger House",
        cuisine = "Burgers, Fast Food",
        phoneNumber = "+201004445556",
        ownerId = "chef.owner@servehub.app",
        ownerEmail = "chef.owner@servehub.app",
        menu = previewMenuItems.takeLast(2)
    ),
    Restaurant(
        id = "rest-3",
        name = "Sushi World",
        cuisine = "Sushi, Japanese",
        phoneNumber = "+201007778889",
        ownerId = "vendor@servehub.app",
        ownerEmail = "vendor@servehub.app",
        menu = listOf(
            MenuItem(id = "m5", name = "California Roll", price = 110.0),
            MenuItem(id = "m6", name = "Salmon Nigiri", price = 140.0)
        )
    )
)

internal val previewCustomer = UserSession(
    id = "customer@servehub.app",
    email = "customer@servehub.app",
    role = UserRole.CUSTOMER
)

internal val previewOwner = UserSession(
    id = "owner@servehub.app",
    email = "owner@servehub.app",
    role = UserRole.OWNER
)

internal val previewCartItems = listOf(
    CartLineItem(
        restaurantId = "rest-1",
        menuItem = previewMenuItems[0],
        quantity = 1
    ),
    CartLineItem(
        restaurantId = "rest-1",
        menuItem = previewMenuItems[2],
        quantity = 2
    )
)

internal val previewCustomerState = RootUiState(
    currentUser = previewCustomer,
    restaurants = previewRestaurants,
    cart = CartState(
        restaurantId = "rest-1",
        items = previewCartItems
    )
)

internal val previewOwnerState = RootUiState(
    currentUser = previewOwner,
    restaurants = previewRestaurants,
    cart = CartState()
)

@Composable
internal fun PreviewContainer(content: @Composable () -> Unit) {
    ServeHubTheme {
        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8F8, widthDp = 360, heightDp = 800)
@Target(AnnotationTarget.FUNCTION)
annotation class PhonePreview
