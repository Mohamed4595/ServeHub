package com.mohamed.servicehub

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun ServeHubApp() {
    val screen by rootComponent.screen.collectAsState()
    val uiState by rootComponent.uiState.collectAsState()
    val loginState by rootComponent.loginStore.state.collectAsState()
    val phoneDialer = remember(context) { PhoneDialer(context) }

    if (uiState.pendingCartChange != null) {
        ClearCartDialog(
            onConfirm = rootComponent::confirmCartReplacement,
            onDismiss = rootComponent::dismissCartReplacement
        )
    }

    Scaffold(
        backgroundColor = ServeHubBackground,
        topBar = {
            if (screen != AppScreen.Splash) {
                ServeHubTopBar(
                    screen = screen,
                    cartCount = uiState.cart.totalQuantity,
                    onBack = rootComponent::goBack,
                    onCart = rootComponent::navigateToCart
                )
            }
        },
        bottomBar = {
            when (screen) {
                AppScreen.Home -> CustomerBottomBar()
                is AppScreen.Details -> {
                    if (uiState.cart.totalQuantity > 0) {
                        DetailsCartBar(
                            totalItems = uiState.cart.totalQuantity,
                            totalPrice = uiState.cart.totalPrice,
                            onCart = rootComponent::navigateToCart
                        )
                    }
                }

                else -> Unit
            }
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = ServeHubBackground
        ) {
            when (val currentScreen = screen) {
                AppScreen.Splash -> SplashScreen(rootComponent::onSplashFinished)
                AppScreen.Login -> LoginScreen(
                    email = loginState.email,
                    password = loginState.password,
                    isLoading = loginState.isLoading,
                    errorMessage = loginState.errorMessage,
                    onEmailChange = { rootComponent.loginStore.dispatch(LoginIntent.UpdateEmail(it)) },
                    onPasswordChange = {
                        rootComponent.loginStore.dispatch(
                            LoginIntent.UpdatePassword(
                                it
                            )
                        )
                    },
                    onLogin = { rootComponent.loginStore.dispatch(LoginIntent.Submit) }
                )

                AppScreen.Home -> HomeScreen(
                    restaurants = uiState.restaurants,
                    onRestaurantClick = rootComponent::openRestaurantDetails
                )

                AppScreen.Dashboard -> DashboardScreen(
                    state = uiState,
                    ownedRestaurants = rootComponent.getOwnedRestaurants(),
                    onRestaurantClick = rootComponent::openRestaurantDetails,
                    onManageMenu = rootComponent::openMenuManagement,
                    onCreateRestaurant = rootComponent::openCreateRestaurant
                )

                is AppScreen.Details -> DetailsScreen(
                    restaurant = rootComponent.getRestaurant(currentScreen.restaurantId),
                    cartState = uiState,
                    onAddToCart = { menuItem ->
                        rootComponent.addToCart(currentScreen.restaurantId, menuItem)
                    }
                )

                AppScreen.Cart -> CartScreen(
                    state = uiState,
                    restaurants = uiState.restaurants,
                    onQuantityChange = rootComponent::changeCartQuantity,
                    onCall = { phoneDialer.dial(it) }
                )

                AppScreen.CreateRestaurant -> CreateRestaurantScreen(
                    onCreate = rootComponent::createRestaurant
                )

                is AppScreen.MenuManagement -> MenuManagementScreen(
                    restaurant = rootComponent.getRestaurant(currentScreen.restaurantId),
                    userRole = uiState.currentUser?.role,
                    onAddItem = { rootComponent.openAddMenuItem(currentScreen.restaurantId) },
                    onDeleteItem = { rootComponent.deleteMenuItem(currentScreen.restaurantId, it) }
                )

                is AppScreen.AddMenuItem -> AddMenuItemScreen(
                    restaurant = rootComponent.getRestaurant(currentScreen.restaurantId),
                    onAdd = { name, price ->
                        rootComponent.addMenuItem(currentScreen.restaurantId, name, price)
                    }
                )
            }
        }
    }
}
