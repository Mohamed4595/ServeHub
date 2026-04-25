package com.mohamed.servicehub
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock



class FakeAuthRepository : AuthRepository {
    override suspend fun login(email: String, password: String): Result<UserSession> {
        delay(600)
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password are required"))
        }

        val normalizedEmail = email.trim().lowercase()
        val role = if ("owner" in normalizedEmail) UserRole.OWNER else UserRole.CUSTOMER

        return Result.success(
            UserSession(
                id = normalizedEmail,
                email = normalizedEmail,
                role = role
            )
        )
    }
}

class InMemoryRestaurantRepository : RestaurantRepository {
    private val mutex = Mutex()
    private val restaurants = mutableListOf(
        Restaurant(
            id = "rest-1",
            name = "Cedar Grill",
            cuisine = "Levantine",
            phoneNumber = "+201001112223",
            ownerId = "owner@servehub.app",
            ownerEmail = "owner@servehub.app",
            menu = listOf(
                MenuItem(id = "m-1", name = "Chicken Shawarma", price = 160.0),
                MenuItem(id = "m-2", name = "Hummus Bowl", price = 90.0),
                MenuItem(id = "m-3", name = "Mixed Grill", price = 240.0)
            )
        ),
        Restaurant(
            id = "rest-2",
            name = "Nile Pasta Lab",
            cuisine = "Italian Fusion",
            phoneNumber = "+201004445556",
            ownerId = "chef.owner@servehub.app",
            ownerEmail = "chef.owner@servehub.app",
            menu = listOf(
                MenuItem(id = "m-4", name = "Truffle Penne", price = 210.0),
                MenuItem(id = "m-5", name = "Pink Sauce Gnocchi", price = 195.0)
            )
        ),
        Restaurant(
            id = "rest-3",
            name = "Falafel District",
            cuisine = "Street Food",
            phoneNumber = "+201007778889",
            ownerId = "vendor@servehub.app",
            ownerEmail = "vendor@servehub.app",
            menu = listOf(
                MenuItem(id = "m-6", name = "Falafel Wrap", price = 65.0),
                MenuItem(id = "m-7", name = "Foul Bowl", price = 55.0)
            )
        )
    )

    override suspend fun getRestaurants(): List<Restaurant> = mutex.withLock {
        restaurants.toList()
    }

    override suspend fun createRestaurant(
        owner: UserSession,
        name: String,
        cuisine: String,
        phoneNumber: String
    ): Restaurant = mutex.withLock {
        require(owner.role == UserRole.OWNER) { "Only owners can create restaurants" }

        val restaurant = Restaurant(
            id = "rest-${restaurants.size + 1}",
            name = name.trim(),
            cuisine = cuisine.trim(),
            phoneNumber = phoneNumber.trim(),
            ownerId = owner.id,
            ownerEmail = owner.email,
            menu = emptyList()
        )
        restaurants += restaurant
        restaurant
    }

    override suspend fun addMenuItem(
        owner: UserSession,
        restaurantId: String,
        name: String,
        price: Double
    ): Restaurant = mutex.withLock {
        val index = restaurants.indexOfFirst { it.id == restaurantId }
        require(index >= 0) { "Restaurant not found" }
        val restaurant = restaurants[index]
        require(owner.role == UserRole.OWNER && restaurant.ownerId == owner.id) {
            "Only the restaurant owner can modify the menu"
        }

        val item = MenuItem(
            id = "${restaurantId}-item-${restaurant.menu.size + 1}",
            name = name.trim(),
            price = price
        )
        val updated = restaurant.copy(menu = restaurant.menu + item)
        restaurants[index] = updated
        updated
    }

    override suspend fun deleteMenuItem(
        owner: UserSession,
        restaurantId: String,
        menuItemId: String
    ): Restaurant = mutex.withLock {
        val index = restaurants.indexOfFirst { it.id == restaurantId }
        require(index >= 0) { "Restaurant not found" }
        val restaurant = restaurants[index]
        require(owner.role == UserRole.OWNER && restaurant.ownerId == owner.id) {
            "Only the restaurant owner can modify the menu"
        }

        val updated = restaurant.copy(menu = restaurant.menu.filterNot { it.id == menuItemId })
        restaurants[index] = updated
        updated
    }
}
