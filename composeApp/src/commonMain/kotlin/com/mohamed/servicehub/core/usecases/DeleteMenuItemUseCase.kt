package com.mohamed.servicehub.core.usecases

import com.mohamed.servicehub.core.repo.RestaurantRepository
import com.mohamed.servicehub.UserSession

class DeleteMenuItemUseCase(private val repository: RestaurantRepository) {
    suspend operator fun invoke(owner: UserSession, restaurantId: String, menuItemId: String) {
        repository.deleteMenuItem(owner, restaurantId, menuItemId)
    }
}
