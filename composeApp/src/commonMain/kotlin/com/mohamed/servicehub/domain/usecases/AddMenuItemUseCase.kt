package com.mohamed.servicehub.domain.usecases

import com.mohamed.servicehub.presentation.UserSession
import com.mohamed.servicehub.domain.repo.RestaurantRepository

class AddMenuItemUseCase(private val repository: RestaurantRepository) {
    suspend operator fun invoke(owner: UserSession, restaurantId: String, name: String, price: Double) {
        // Owner user session is passed; repository expects a User (alias maps to internal)
        repository.addMenuItem(owner, restaurantId, name, price)
    }
}
