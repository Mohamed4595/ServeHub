package com.mohamed.servicehub.presentation
enum class UserRole {
    CUSTOMER,
    OWNER
}

data class UserSession(
    val id: String,
    val email: String,
    val role: UserRole
)

data class MenuItem(
    val id: String,
    val name: String,
    val price: Double
)

data class Restaurant(
    val id: String,
    val name: String,
    val cuisine: String,
    val phoneNumber: String,
    val ownerId: String,
    val ownerEmail: String,
    val menu: List<MenuItem>
)

data class CartLineItem(
    val restaurantId: String,
    val menuItem: MenuItem,
    val quantity: Int
)

data class CartState(
    val restaurantId: String? = null,
    val items: List<CartLineItem> = emptyList()
) {
    val totalQuantity: Int
        get() = items.sumOf { it.quantity }

    val totalPrice: Double
        get() = items.sumOf { it.menuItem.price * it.quantity }
}

sealed interface AppScreen {
    object Splash : AppScreen
    object Login : AppScreen
    object Home : AppScreen
    object Dashboard : AppScreen
    data class Details(val restaurantId: String) : AppScreen
    object Cart : AppScreen
    object CreateRestaurant : AppScreen
    data class MenuManagement(val restaurantId: String) : AppScreen
    data class AddMenuItem(val restaurantId: String) : AppScreen
}

data class PendingCartChange(
    val restaurantId: String,
    val menuItem: MenuItem
)

data class RootUiState(
    val currentUser: UserSession? = null,
    val restaurants: List<Restaurant> = emptyList(),
    val cart: CartState = CartState(),
    val pendingCartChange: PendingCartChange? = null
)
