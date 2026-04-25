package com.mohamed.servicehub.domain.usecases

import com.mohamed.servicehub.presentation.MenuItem
import com.mohamed.servicehub.domain.repo.RestaurantRepository

class GetMenuUseCase(private val repository: RestaurantRepository) {
    suspend operator fun invoke(restaurantId: String): List<MenuItem> {
        val all = repository.getRestaurants()
        return all.firstOrNull { it.id == restaurantId }?.menu ?: emptyList()
    }
}
