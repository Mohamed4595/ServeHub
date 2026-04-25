package com.mohamed.servicehub.core.repo

import com.mohamed.servicehub.MenuItem
import com.mohamed.servicehub.Restaurant
import com.mohamed.servicehub.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.plusAssign
import kotlin.text.iterator

class FirebaseRestaurantRepository(private val firestore: FirebaseFirestore) : RestaurantRepository {
    override suspend fun getRestaurants(): List<Restaurant> = withContext(Dispatchers.IO) {
        val restaurants = mutableListOf<Restaurant>()
        val restSnap = Tasks.await(firestore.collection("restaurants").get())
        for (doc in restSnap.documents) {
            val id = doc.id
            val name = doc.getString("name") ?: ""
            val cuisine = doc.getString("cuisine") ?: ""
            val phone = doc.getString("phoneNumber") ?: ""
            val ownerId = doc.getString("ownerId") ?: ""
            val ownerEmail = doc.getString("ownerEmail") ?: ""
            val menuSnap = Tasks.await(
                firestore.collection("restaurants").document(id).collection("menu").get()
            )
            val menu = menuSnap.documents.map { m ->
                MenuItem(
                    id = m.id,
                    name = m.getString("name") ?: "",
                    price = m.getDouble("price") ?: 0.0
                )
            }
            restaurants plusAssign Restaurant(id, name, cuisine, phone, ownerId, ownerEmail, menu)
        }
        restaurants
    }

    override suspend fun createRestaurant(owner: UserSession, name: String, cuisine: String, phoneNumber: String): Restaurant =
        withContext(Dispatchers.IO) {
            val docRef = firestore.collection("restaurants").document()
            val restaurant = Restaurant(
                id = docRef.id,
                name = name.trim(),
                cuisine = cuisine.trim(),
                phoneNumber = phoneNumber.trim(),
                ownerId = owner.id,
                ownerEmail = owner.email,
                menu = emptyList()
            )
            val data = mapOf(
                "name" to restaurant.name,
                "cuisine" to restaurant.cuisine,
                "phoneNumber" to restaurant.phoneNumber,
                "ownerId" to restaurant.ownerId,
                "ownerEmail" to restaurant.ownerEmail
            )
            Tasks.await(docRef.set(data))
            restaurant
        }

    override suspend fun addMenuItem(owner: UserSession, restaurantId: String, name: String, price: Double): Restaurant =
        withContext(Dispatchers.IO) {
            val restRef = firestore.collection("restaurants").document(restaurantId)
            val itemRef = restRef.collection("menu").document()
            val data = mapOf("name" to name.trim(), "price" to price)
            Tasks.await(itemRef.set(data))
            fetchRestaurantWithMenu(restaurantId)
        }

    override suspend fun deleteMenuItem(owner: UserSession, restaurantId: String, menuItemId: String): Restaurant =
        withContext(Dispatchers.IO) {
            val restRef = firestore.collection("restaurants").document(restaurantId)
            val updatedMenu = restRef.collection("menu").document(menuItemId)
            Tasks.await(updatedMenu.delete())
            fetchRestaurantWithMenu(restaurantId)
        }

    private suspend fun fetchRestaurantWithMenu(restaurantId: String): Restaurant {
        val restDoc = Tasks.await(firestore.collection("restaurants").document(restaurantId).get())
        val name = restDoc.getString("name") ?: ""
        val cuisine = restDoc.getString("cuisine") ?: ""
        val phone = restDoc.getString("phoneNumber") ?: ""
        val ownerId = restDoc.getString("ownerId") ?: ""
        val ownerEmail = restDoc.getString("ownerEmail") ?: ""
        val menuSnap = Tasks.await(firestore.collection("restaurants").document(restaurantId).collection("menu").get())
        val menu = menuSnap.documents.map { m ->
            MenuItem(
                id = m.id,
                name = m.getString("name") ?: "",
                price = m.getDouble("price") ?: 0.0
            )
        }
        return Restaurant(restaurantId, name, cuisine, phone, ownerId, ownerEmail, menu)
    }
}