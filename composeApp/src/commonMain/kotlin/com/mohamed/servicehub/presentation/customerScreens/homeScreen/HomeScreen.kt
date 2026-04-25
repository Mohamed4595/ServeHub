package com.mohamed.servicehub.presentation.customerScreens.homeScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mohamed.servicehub.PhonePreview
import com.mohamed.servicehub.PreviewContainer
import com.mohamed.servicehub.presentation.components.SearchHeader
import com.mohamed.servicehub.presentation.components.ServeHubBackground
import com.mohamed.servicehub.presentation.components.ServeHubInk
import com.mohamed.servicehub.presentation.Restaurant
import com.mohamed.servicehub.previewRestaurants

@Composable
internal fun HomeScreen(
    restaurants: List<Restaurant>,
    onRestaurantClick: (String) -> Unit
) {
    var search by rememberSaveable { mutableStateOf("") }
    val filtered = restaurants.filter {
        it.name.contains(search, ignoreCase = true) || it.cuisine.contains(search, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ServeHubBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        SearchHeader(search = search, onSearchChange = { search = it })
        Spacer(modifier = Modifier.height(18.dp))
        Text("Popular Restaurants", fontWeight = FontWeight.Bold, color = ServeHubInk)
        Spacer(modifier = Modifier.height(14.dp))
        LazyColumn(
            contentPadding = PaddingValues(bottom = 84.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filtered) { restaurant ->
                PopularRestaurantCard(restaurant = restaurant, onClick = { onRestaurantClick(restaurant.id) })
            }
        }
    }
}

@PhonePreview
@Composable
private fun HomeScreenPreview() = PreviewContainer {
    HomeScreen(
        restaurants = previewRestaurants,
        onRestaurantClick = {}
    )
}
