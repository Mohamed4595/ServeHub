package com.mohamed.servicehub.core.usecases

import com.mohamed.servicehub.core.repo.RestaurantRepository
import com.mohamed.servicehub.Restaurant

class GetRestaurantsUseCase(private val repository: RestaurantRepository) {
    suspend operator fun invoke(): List<Restaurant> = repository.getRestaurants()
}
