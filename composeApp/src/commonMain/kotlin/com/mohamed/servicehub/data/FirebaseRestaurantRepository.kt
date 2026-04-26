package com.mohamed.servicehub.data

import com.mohamed.servicehub.presentation.MenuItem
import com.mohamed.servicehub.presentation.Restaurant
import com.mohamed.servicehub.presentation.UserSession
import com.mohamed.servicehub.domain.repo.RestaurantRepository
import dev.gitlive.firebase.firestore.FirebaseFirestore

class FirebaseRestaurantRepository(private val firestore: FirebaseFirestore) :
    RestaurantRepository {

    override suspend fun getRestaurants(): List<Restaurant> {
        val restSnap = firestore.collection("restaurants").get()
        return restSnap.documents.map { doc ->
            val id = doc.id
            val name: String = doc.get("name")
            val cuisine: String = doc.get("cuisine")
            val phone: String = doc.get("phoneNumber")
            val ownerId: String = doc.get("ownerId")
            val ownerEmail: String = doc.get("ownerEmail")

            val menuSnap = firestore.collection("restaurants").document(id).collection("menu").get()
            val menu = menuSnap.documents.map { m ->
                MenuItem(
                    id = m.id,
                    name = m.get("name"),
                    price = (m.get<Number?>("price")?.toDouble() ?: 0.0)
                )
            }
            Restaurant(id, name, cuisine, phone, ownerId, ownerEmail, menu)
        }
    }

    override suspend fun createRestaurant(owner: UserSession, name: String, cuisine: String, phoneNumber: String): Restaurant {
        // GitLive KMP SDK uses 'document' property for auto-generated IDs
        val docRef = firestore.collection("restaurants").document
        val restaurant = Restaurant(
            id = docRef.id,
            name = name.trim(),
            cuisine = cuisine.trim(),
            phoneNumber = phoneNumber.trim(),
            ownerId = owner.id,
            ownerEmail = owner.email ?: "",
            menu = emptyList()
        )
        val data = mapOf(
            "name" to restaurant.name,
            "cuisine" to restaurant.cuisine,
            "phoneNumber" to restaurant.phoneNumber,
            "ownerId" to restaurant.ownerId,
            "ownerEmail" to restaurant.ownerEmail
        )
        docRef.set(data)
        return restaurant
    }

    override suspend fun addMenuItem(owner: UserSession, restaurantId: String, name: String, price: Double): Restaurant {
        val restRef = firestore.collection("restaurants").document(restaurantId)
        // GitLive KMP SDK uses 'document' property for auto-generated IDs
        val itemRef = restRef.collection("menu").document
        val data = mapOf("name" to name.trim(), "price" to price)
        itemRef.set(data)
        return fetchRestaurantWithMenu(restaurantId)
    }

    override suspend fun deleteMenuItem(owner: UserSession, restaurantId: String, menuItemId: String): Restaurant {
        val restRef = firestore.collection("restaurants").document(restaurantId)
        val itemRef = restRef.collection("menu").document(menuItemId)
        itemRef.delete()
        return fetchRestaurantWithMenu(restaurantId)
    }

    private suspend fun fetchRestaurantWithMenu(restaurantId: String): Restaurant {
        val restDoc = firestore.collection("restaurants").document(restaurantId).get()
        val name: String = restDoc.get("name")
        val cuisine: String = restDoc.get("cuisine")
        val phone: String = restDoc.get("phoneNumber")
        val ownerId: String = restDoc.get("ownerId")
        val ownerEmail: String = restDoc.get("ownerEmail")

        val menuSnap = firestore.collection("restaurants").document(restaurantId).collection("menu").get()
        val menu = menuSnap.documents.map { m ->
            MenuItem(
                id = m.id,
                name = m.get("name"),
                price = (m.get<Number?>("price")?.toDouble() ?: 0.0)
            )
        }
        return Restaurant(restaurantId, name, cuisine, phone, ownerId, ownerEmail, menu)
    }
}