package com.mohamed.servicehub.presentation.ownerScreens.createRestaurantScreen
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mohamed.servicehub.presentation.components.FormLayout
import com.mohamed.servicehub.presentation.components.ImageUploadPlaceholder
import com.mohamed.servicehub.PhonePreview
import com.mohamed.servicehub.PreviewContainer
import com.mohamed.servicehub.presentation.components.PrimaryActionButton
import com.mohamed.servicehub.presentation.components.ServeHubInk
import com.mohamed.servicehub.presentation.components.ServeHubTextField

@Composable
internal fun CreateRestaurantScreen(
    onCreate: (String, String, String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var cuisine by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    FormLayout(title = "Create Restaurant") {
        ImageUploadPlaceholder(text = "Upload Image\nJPG, PNG up to 5MB")
        Spacer(modifier = Modifier.height(18.dp))
        Text("Restaurant Name", color = ServeHubInk, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        ServeHubTextField(value = name, onValueChange = { name = it }, label = "Enter restaurant name")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Cuisine", color = ServeHubInk, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        ServeHubTextField(value = cuisine, onValueChange = { cuisine = it }, label = "Italian, Burgers...")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Image URL (optional)", color = ServeHubInk, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        ServeHubTextField(value = phone, onValueChange = { phone = it }, label = "Restaurant phone number")
        Spacer(modifier = Modifier.height(26.dp))
        PrimaryActionButton(text = "Save Restaurant", onClick = { onCreate(name, cuisine, phone) })
    }
}
@PhonePreview
@Composable
private fun CreateRestaurantScreenPreview() = PreviewContainer {
    CreateRestaurantScreen(onCreate = { _, _, _ -> })
}
