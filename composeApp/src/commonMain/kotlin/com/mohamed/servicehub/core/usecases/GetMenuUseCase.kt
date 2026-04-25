package com.mohamed.servicehub.core.usecases

import com.mohamed.servicehub.MenuItem
import com.mohamed.servicehub.Restaurant
import com.mohamed.servicehub.core.repo.RestaurantRepository

class GetMenuUseCase(private val repository: RestaurantRepository) {
    suspend operator fun invoke(restaurantId: String): List<MenuItem> {
        val all = repository.getRestaurants()
        return all.firstOrNull { it.id == restaurantId }?.menu ?: emptyList()
    }
}
