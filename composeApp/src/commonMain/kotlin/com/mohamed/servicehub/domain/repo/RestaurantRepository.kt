package com.mohamed.servicehub.domain.repo

import com.mohamed.servicehub.presentation.Restaurant
import com.mohamed.servicehub.presentation.UserSession

interface RestaurantRepository {
    suspend fun getRestaurants(): List<Restaurant>
    suspend fun createRestaurant(owner: UserSession, name: String, cuisine: String, phoneNumber: String): Restaurant
    suspend fun addMenuItem(owner: UserSession, restaurantId: String, name: String, price: Double): Restaurant
    suspend fun deleteMenuItem(owner: UserSession, restaurantId: String, menuItemId: String): Restaurant
}
