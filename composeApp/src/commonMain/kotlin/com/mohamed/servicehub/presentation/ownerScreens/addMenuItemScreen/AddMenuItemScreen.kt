package com.mohamed.servicehub.presentation.ownerScreens.addMenuItemScreen
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamed.servicehub.presentation.components.FormLayout
import com.mohamed.servicehub.presentation.components.ImageUploadPlaceholder
import com.mohamed.servicehub.PhonePreview
import com.mohamed.servicehub.PreviewContainer
import com.mohamed.servicehub.presentation.components.PrimaryActionButton
import com.mohamed.servicehub.presentation.components.ServeHubInk
import com.mohamed.servicehub.presentation.components.ServeHubMuted
import com.mohamed.servicehub.presentation.components.ServeHubTextField
import com.mohamed.servicehub.presentation.Restaurant
import com.mohamed.servicehub.previewRestaurants

@Composable
internal fun AddMenuItemScreen(
    restaurant: Restaurant?,
    onAdd: (String, String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }

    FormLayout(title = "Add Menu Item") {
        ImageUploadPlaceholder(text = "Upload Image\nJPG, PNG up to 5MB")
        Spacer(modifier = Modifier.height(18.dp))
        Text("Item Name", color = ServeHubInk, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        ServeHubTextField(value = name, onValueChange = { name = it }, label = "Enter item name")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Price (EGP)", color = ServeHubInk, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        ServeHubTextField(
            value = price,
            onValueChange = { price = it },
            label = "Enter price",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryActionButton(
            text = "Save Item",
            onClick = { onAdd(name, price) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Adding to ${restaurant?.name ?: "restaurant"}",
            color = ServeHubMuted,
            fontSize = 12.sp
        )
    }
}
@PhonePreview
@Composable
private fun AddMenuItemScreenPreview() = PreviewContainer {
    AddMenuItemScreen(
        restaurant = previewRestaurants.first(),
        onAdd = { _, _ -> }
    )
}
