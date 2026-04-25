package com.mohamed.servicehub.core.usecases

import com.mohamed.servicehub.Restaurant
import com.mohamed.servicehub.UserSession
import com.mohamed.servicehub.core.repo.RestaurantRepository
import com.mohamed.servicehub.MenuItem

// Get all restaurants (for Customer and Owner dashboards)
class GetRestaurantsUseCase(private val repository: RestaurantRepository) {
    suspend operator fun invoke(): List<Restaurant> = repository.getRestaurants()
}

// Get menu items for a restaurant
class GetMenuUseCase(private val repository: RestaurantRepository) {
    suspend operator fun invoke(restaurantId: String): List<MenuItem> {
        val all = repository.getRestaurants()
        return all.firstOrNull { it.id == restaurantId }?.menu ?: emptyList()
    }
}

// Add a new menu item to a restaurant (Owner only)
class AddMenuItemUseCase(private val repository: RestaurantRepository) {
    suspend operator fun invoke(owner: UserSession, restaurantId: String, name: String, price: Double): Restaurant {
        return repository.addMenuItem(owner, restaurantId, name, price)
    }
}

// Delete a menu item from a restaurant (Owner only)
class DeleteMenuItemUseCase(private val repository: RestaurantRepository) {
    suspend operator fun invoke(owner: UserSession, restaurantId: String, menuItemId: String): Restaurant {
        return repository.deleteMenuItem(owner, restaurantId, menuItemId)
    }
}
