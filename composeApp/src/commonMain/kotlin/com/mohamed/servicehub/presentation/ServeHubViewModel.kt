package com.mohamed.servicehub.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.servicehub.domain.repo.AuthRepository
import com.mohamed.servicehub.domain.repo.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.plus

class ServeHubViewModel(
    private val authRepository: AuthRepository,
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RootUiState())
    val uiState: StateFlow<RootUiState> = _uiState.asStateFlow()

    private val _screen = MutableStateFlow<AppScreen>(AppScreen.Splash)
    val screen: StateFlow<AppScreen> = _screen.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                val restaurants = restaurantRepository.getRestaurants()
                _uiState.update { it.copy(restaurants = restaurants) }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        _screen.value = screen
    }

    fun goBack() {
        // Simple back logic: if not home/splash, go home
        val current = _screen.value
        if (current !is AppScreen.Home && current !is AppScreen.Splash) {
            _screen.value = AppScreen.Home
        }
    }

    fun navigateToCart() {
        _screen.value = AppScreen.Cart
    }

    fun openRestaurantDetails(restaurantId: String) {
        _screen.value = AppScreen.Details(restaurantId)
    }

    fun onSplashFinished() {
        _screen.value = AppScreen.Login
    }

    fun addToCart(restaurantId: String, menuItem: MenuItem) {
        val currentCart = _uiState.value.cart
        if (currentCart.restaurantId != null && currentCart.restaurantId != restaurantId) {
            _uiState.update { it.copy(pendingCartChange = PendingCartChange(restaurantId, menuItem)) }
            return
        }

        updateCart(restaurantId, menuItem, 1)
    }

    fun confirmCartReplacement() {
        val pending = _uiState.value.pendingCartChange ?: return
        _uiState.update {
            it.copy(
                cart = CartState(restaurantId = pending.restaurantId),
                pendingCartChange = null
            )
        }
        updateCart(pending.restaurantId, pending.menuItem, 1)
    }

    fun dismissCartReplacement() {
        _uiState.update { it.copy(pendingCartChange = null) }
    }

    private fun updateCart(restaurantId: String, menuItem: MenuItem, delta: Int) {
        _uiState.update { state ->
            val currentItems = state.cart.items.toMutableList()
            val index = currentItems.indexOfFirst { it.menuItem.id == menuItem.id }

            if (index != -1) {
                val newItem = currentItems[index].copy(quantity = currentItems[index].quantity + delta)
                if (newItem.quantity <= 0) {
                    currentItems.removeAt(index)
                } else {
                    currentItems[index] = newItem
                }
            } else if (delta > 0) {
                currentItems.add(CartLineItem(restaurantId, menuItem, delta))
            }

            val newRestaurantId = if (currentItems.isEmpty()) null else restaurantId
            state.copy(cart = state.cart.copy(restaurantId = newRestaurantId, items = currentItems))
        }
    }

    fun changeCartQuantity(menuItem: MenuItem, delta: Int) {
        val restaurantId = _uiState.value.cart.restaurantId ?: return
        updateCart(restaurantId, menuItem, delta)
    }

    fun getRestaurant(id: String): Restaurant? {
        return _uiState.value.restaurants.find { it.id == id }
    }

    fun getOwnedRestaurants(): List<Restaurant> {
        val userId = _uiState.value.currentUser?.id ?: return emptyList()
        return _uiState.value.restaurants.filter { it.ownerId == userId }
    }

    fun openMenuManagement(restaurantId: String) {
        _screen.value = AppScreen.MenuManagement(restaurantId)
    }

    fun openCreateRestaurant() {
        _screen.value = AppScreen.CreateRestaurant
    }

    fun openAddMenuItem(restaurantId: String) {
        _screen.value = AppScreen.AddMenuItem(restaurantId)
    }

    fun setCurrentUser(user: UserSession?) {
        _uiState.update { it.copy(currentUser = user) }
    }

    fun createRestaurant(name: String, cuisine: String, phoneNumber: String) {
        val user = _uiState.value.currentUser ?: return
        viewModelScope.launch {
            try {
                val newRestaurant = restaurantRepository.createRestaurant(user, name, cuisine, phoneNumber)
                _uiState.update { it.copy(restaurants = it.restaurants + newRestaurant) }
                _screen.value = AppScreen.Dashboard
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun addMenuItem(restaurantId: String, name: String, price: Double) {
        val user = _uiState.value.currentUser ?: return
        viewModelScope.launch {
            try {
                val updatedRestaurant = restaurantRepository.addMenuItem(user, restaurantId, name, price)
                _uiState.update { state ->
                    state.copy(restaurants = state.restaurants.map {
                        if (it.id == restaurantId) updatedRestaurant else it
                    })
                }
                _screen.value = AppScreen.MenuManagement(restaurantId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun deleteMenuItem(restaurantId: String, menuItemId: String) {
        val user = _uiState.value.currentUser ?: return
        viewModelScope.launch {
            try {
                val updatedRestaurant = restaurantRepository.deleteMenuItem(user, restaurantId, menuItemId)
                _uiState.update { state ->
                    state.copy(restaurants = state.restaurants.map {
                        if (it.id == restaurantId) updatedRestaurant else it
                    })
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
