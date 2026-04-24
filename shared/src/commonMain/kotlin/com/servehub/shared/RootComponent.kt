package com.servehub.shared

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val authRepository: AuthRepository = FakeAuthRepository()
    private val restaurantRepository: RestaurantRepository = InMemoryRestaurantRepository()

    private val _screen = MutableStateFlow<AppScreen>(AppScreen.Splash)
    val screen: StateFlow<AppScreen> = _screen.asStateFlow()

    private val _uiState = MutableStateFlow(RootUiState())
    val uiState: StateFlow<RootUiState> = _uiState.asStateFlow()

    val loginStore = LoginStore(
        authRepository = authRepository,
        scope = scope
    )

    init {
        scope.launch {
            refreshRestaurants()
        }

        scope.launch {
            loginStore.effects.collectLatest { effect ->
                when (effect) {
                    is LoginEffect.Authenticated -> {
                        _uiState.update { current ->
                            current.copy(currentUser = effect.user)
                        }
                        navigateForRole(effect.user.role)
                    }

                    is LoginEffect.Failed -> Unit
                }
            }
        }
    }

    fun onSplashFinished() {
        _screen.value = AppScreen.Login
    }

    fun navigateToCart() {
        _screen.value = AppScreen.Cart
    }

    fun goBack() {
        when (val current = _screen.value) {
            AppScreen.Cart -> navigateForRole(_uiState.value.currentUser?.role)
            AppScreen.CreateRestaurant -> _screen.value = AppScreen.Dashboard
            is AppScreen.AddMenuItem -> _screen.value = AppScreen.MenuManagement(current.restaurantId)
            is AppScreen.MenuManagement -> _screen.value = AppScreen.Dashboard
            is AppScreen.Details -> navigateForRole(_uiState.value.currentUser?.role)
            else -> Unit
        }
    }

    fun logout() {
        loginStore.reset()
        _uiState.update {
            it.copy(
                currentUser = null,
                cart = CartState(),
                pendingCartChange = null
            )
        }
        _screen.value = AppScreen.Login
    }

    fun openRestaurantDetails(restaurantId: String) {
        _screen.value = AppScreen.Details(restaurantId)
    }

    fun openCreateRestaurant() {
        if (_uiState.value.currentUser?.role == UserRole.OWNER) {
            _screen.value = AppScreen.CreateRestaurant
        }
    }

    fun openMenuManagement(restaurantId: String) {
        if (canManageRestaurant(restaurantId)) {
            _screen.value = AppScreen.MenuManagement(restaurantId)
        }
    }

    fun openAddMenuItem(restaurantId: String) {
        if (canManageRestaurant(restaurantId)) {
            _screen.value = AppScreen.AddMenuItem(restaurantId)
        }
    }

    fun createRestaurant(name: String, cuisine: String, phoneNumber: String) {
        val owner = _uiState.value.currentUser ?: return
        if (owner.role != UserRole.OWNER || name.isBlank() || cuisine.isBlank() || phoneNumber.isBlank()) {
            return
        }

        scope.launch {
            restaurantRepository.createRestaurant(owner, name, cuisine, phoneNumber)
            refreshRestaurants()
            _screen.value = AppScreen.Dashboard
        }
    }

    fun addMenuItem(restaurantId: String, name: String, priceText: String) {
        val owner = _uiState.value.currentUser ?: return
        val price = priceText.toDoubleOrNull() ?: return
        if (name.isBlank()) return

        scope.launch {
            restaurantRepository.addMenuItem(owner, restaurantId, name, price)
            refreshRestaurants()
            _screen.value = AppScreen.MenuManagement(restaurantId)
        }
    }

    fun deleteMenuItem(restaurantId: String, menuItemId: String) {
        val owner = _uiState.value.currentUser ?: return

        scope.launch {
            restaurantRepository.deleteMenuItem(owner, restaurantId, menuItemId)
            refreshRestaurants()
        }
    }

    fun addToCart(restaurantId: String, menuItem: MenuItem) {
        val cart = _uiState.value.cart
        if (cart.restaurantId != null && cart.restaurantId != restaurantId && cart.items.isNotEmpty()) {
            _uiState.update { current ->
                current.copy(
                    pendingCartChange = PendingCartChange(
                        restaurantId = restaurantId,
                        menuItem = menuItem
                    )
                )
            }
            return
        }

        appendCartItem(restaurantId, menuItem)
    }

    fun confirmCartReplacement() {
        val pending = _uiState.value.pendingCartChange ?: return
        _uiState.update { current ->
            current.copy(
                cart = CartState(),
                pendingCartChange = null
            )
        }
        appendCartItem(pending.restaurantId, pending.menuItem)
    }

    fun dismissCartReplacement() {
        _uiState.update { current ->
            current.copy(pendingCartChange = null)
        }
    }

    fun changeCartQuantity(menuItemId: String, delta: Int) {
        _uiState.update { current ->
            val updatedItems = current.cart.items.mapNotNull { item ->
                if (item.menuItem.id != menuItemId) {
                    item
                } else {
                    val newQuantity = item.quantity + delta
                    if (newQuantity > 0) {
                        item.copy(quantity = newQuantity)
                    } else {
                        null
                    }
                }
            }

            val restaurantId = updatedItems.firstOrNull()?.restaurantId
            current.copy(
                cart = CartState(
                    restaurantId = restaurantId,
                    items = updatedItems
                )
            )
        }
    }

    fun getRestaurant(restaurantId: String): Restaurant? {
        return _uiState.value.restaurants.firstOrNull { it.id == restaurantId }
    }

    fun getOwnedRestaurants(): List<Restaurant> {
        val ownerId = _uiState.value.currentUser?.id
        return _uiState.value.restaurants.filter { it.ownerId == ownerId }
    }

    private suspend fun refreshRestaurants() {
        val restaurants = restaurantRepository.getRestaurants()
        _uiState.update { current ->
            current.copy(restaurants = restaurants)
        }
    }

    private fun navigateForRole(role: UserRole?) {
        _screen.value = if (role == UserRole.OWNER) {
            AppScreen.Dashboard
        } else {
            AppScreen.Home
        }
    }

    private fun canManageRestaurant(restaurantId: String): Boolean {
        val user = _uiState.value.currentUser ?: return false
        return user.role == UserRole.OWNER && getRestaurant(restaurantId)?.ownerId == user.id
    }

    private fun appendCartItem(restaurantId: String, menuItem: MenuItem) {
        _uiState.update { current ->
            val existing = current.cart.items.firstOrNull { it.menuItem.id == menuItem.id }
            val items = if (existing == null) {
                current.cart.items + CartLineItem(
                    restaurantId = restaurantId,
                    menuItem = menuItem,
                    quantity = 1
                )
            } else {
                current.cart.items.map {
                    if (it.menuItem.id == menuItem.id) it.copy(quantity = it.quantity + 1) else it
                }
            }

            current.copy(
                cart = CartState(
                    restaurantId = restaurantId,
                    items = items
                ),
                pendingCartChange = null
            )
        }
    }
}
