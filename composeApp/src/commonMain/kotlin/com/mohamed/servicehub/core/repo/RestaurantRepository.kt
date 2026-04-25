package com.mohamed.servicehub.core.repo

import com.mohamed.servicehub.Restaurant
import com.mohamed.servicehub.User

interface RestaurantRepository {
    suspend fun getRestaurants(): List<Restaurant>
    suspend fun createRestaurant(owner: User, name: String, cuisine: String, phoneNumber: String): Restaurant
    suspend fun addMenuItem(owner: User, restaurantId: String, name: String, price: Double): Restaurant
    suspend fun deleteMenuItem(owner: User, restaurantId: String, menuItemId: String): Restaurant
}
