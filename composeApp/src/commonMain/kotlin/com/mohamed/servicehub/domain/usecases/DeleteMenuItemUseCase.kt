package com.mohamed.servicehub.domain.usecases

import com.mohamed.servicehub.domain.repo.RestaurantRepository
import com.mohamed.servicehub.presentation.UserSession

class DeleteMenuItemUseCase(private val repository: RestaurantRepository) {
    suspend operator fun invoke(owner: UserSession, restaurantId: String, menuItemId: String) {
        repository.deleteMenuItem(owner, restaurantId, menuItemId)
    }
}
