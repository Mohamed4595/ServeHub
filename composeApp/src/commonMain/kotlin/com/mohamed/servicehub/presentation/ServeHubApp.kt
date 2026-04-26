package com.mohamed.servicehub.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mohamed.servicehub.presentation.auth.PhoneAuthIntent
import com.mohamed.servicehub.presentation.auth.PhoneAuthScreen
import com.mohamed.servicehub.presentation.auth.PhoneAuthViewModel
import com.mohamed.servicehub.presentation.components.ClearCartDialog
import com.mohamed.servicehub.presentation.components.CustomerBottomBar
import com.mohamed.servicehub.presentation.components.DetailsCartBar
import com.mohamed.servicehub.presentation.components.ServeHubBackground
import com.mohamed.servicehub.presentation.components.ServeHubTopBar
import com.mohamed.servicehub.presentation.customerScreens.cartScreen.CartScreen
import com.mohamed.servicehub.presentation.customerScreens.detailsScreen.DetailsScreen
import com.mohamed.servicehub.presentation.customerScreens.homeScreen.HomeScreen
import com.mohamed.servicehub.presentation.ownerScreens.addMenuItemScreen.AddMenuItemScreen
import com.mohamed.servicehub.presentation.ownerScreens.createRestaurantScreen.CreateRestaurantScreen
import com.mohamed.servicehub.presentation.ownerScreens.dashboardScreen.DashboardScreen
import com.mohamed.servicehub.presentation.ownerScreens.menuManagementScreen.MenuManagementScreen
import com.mohamed.servicehub.presentation.splashScreen.SplashScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ServeHubApp(
    viewModel: ServeHubViewModel = koinViewModel(),
    phoneAuthViewModel: PhoneAuthViewModel = koinViewModel()
) {
    val screen by viewModel.screen.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.pendingCartChange != null) {
        ClearCartDialog(
            onConfirm = viewModel::confirmCartReplacement,
            onDismiss = viewModel::dismissCartReplacement
        )
    }

    Scaffold(
        topBar = {
            if (screen != AppScreen.Splash) {
                ServeHubTopBar(
                    screen = screen,
                    cartCount = uiState.cart.totalQuantity,
                    onBack = viewModel::goBack,
                    onCart = viewModel::navigateToCart
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
                            onCart = viewModel::navigateToCart
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
                AppScreen.Splash -> SplashScreen(viewModel::onSplashFinished)
                AppScreen.Login -> PhoneAuthScreen(
                    viewModel = phoneAuthViewModel,
                    onNavigateToHome = {
                        viewModel.setCurrentUser(phoneAuthViewModel.state.value.userSession)
                        viewModel.navigateTo(AppScreen.Home)
                    },
                    onNavigateToStaff = {
                        viewModel.setCurrentUser(phoneAuthViewModel.state.value.userSession)
                        viewModel.navigateTo(AppScreen.Dashboard)
                    },
                    onNavigateToAdmin = { /* Navigate to admin panel */ }
                )
                AppScreen.Home -> HomeScreen(
                    restaurants = uiState.restaurants,
                    onRestaurantClick = viewModel::openRestaurantDetails
                )
                AppScreen.Dashboard -> DashboardScreen(
                    state = uiState,
                    ownedRestaurants = viewModel.getOwnedRestaurants(),
                    onRestaurantClick = viewModel::openRestaurantDetails,
                    onManageMenu = viewModel::openMenuManagement,
                    onCreateRestaurant = viewModel::openCreateRestaurant
                )
                is AppScreen.Details -> DetailsScreen(
                    restaurant = viewModel.getRestaurant(currentScreen.restaurantId),
                    cartState = uiState,
                    onAddToCart = { menuItem ->
                        viewModel.addToCart(currentScreen.restaurantId, menuItem)
                    }
                )
                AppScreen.Cart -> CartScreen(
                    state = uiState,
                    restaurants = uiState.restaurants,
                    onQuantityChange = { menuItem, amount ->
                        viewModel.changeCartQuantity(menuItem, amount)
                    },
                    onCall = { /* phoneDialer.dial(it) */ }
                )
                AppScreen.CreateRestaurant -> CreateRestaurantScreen(
                    onCreate = viewModel::createRestaurant
                )
                is AppScreen.MenuManagement -> MenuManagementScreen(
                    restaurant = viewModel.getRestaurant(currentScreen.restaurantId),
                    userRole = uiState.currentUser?.role,
                    onAddItem = { viewModel.openAddMenuItem(currentScreen.restaurantId) },
                    onDeleteItem = { viewModel.deleteMenuItem(currentScreen.restaurantId, it) }
                )
                is AppScreen.AddMenuItem -> AddMenuItemScreen(
                    restaurant = viewModel.getRestaurant(currentScreen.restaurantId),
                    onAdd = { name, price ->
                        viewModel.addMenuItem(currentScreen.restaurantId, name, price.toDouble())
                    }
                )
            }
        }
    }
}
