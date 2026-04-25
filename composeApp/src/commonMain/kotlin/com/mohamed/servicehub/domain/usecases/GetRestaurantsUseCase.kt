package com.mohamed.servicehub.domain.usecases

import com.mohamed.servicehub.domain.repo.RestaurantRepository
import com.mohamed.servicehub.presentation.Restaurant

class GetRestaurantsUseCase(private val repository: RestaurantRepository) {
    suspend operator fun invoke(): List<Restaurant> = repository.getRestaurants()
}
