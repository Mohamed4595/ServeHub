package com.mohamed.servicehub.presentation.customerScreens.cartScreen
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamed.servicehub.presentation.components.FoodThumbnail
import com.mohamed.servicehub.PreviewContainer
import com.mohamed.servicehub.presentation.components.ServeHubBorder
import com.mohamed.servicehub.presentation.components.ServeHubInk
import com.mohamed.servicehub.presentation.components.ServeHubMuted
import com.mohamed.servicehub.presentation.components.Stepper
import com.mohamed.servicehub.previewMenuItems
import com.mohamed.servicehub.presentation.components.toCurrency

@Composable
 fun CartItemCard(
    itemName: String,
    itemPrice: Double,
    quantity: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White,
        elevation = 0.dp,
        modifier = Modifier.border(1.dp, ServeHubBorder, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FoodThumbnail(label = itemName, size = 66.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(itemName, fontWeight = FontWeight.Bold, color = ServeHubInk)
                Text("${itemPrice.toCurrency()} EGP", color = ServeHubMuted, fontSize = 12.sp)
            }
            Stepper(quantity = quantity, onMinus = onMinus, onPlus = onPlus)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CartItemCardPreview() = PreviewContainer {
    CartItemCard(
        itemName = previewMenuItems.first().name,
        itemPrice = previewMenuItems.first().price,
        quantity = 2,
        onMinus = {},
        onPlus = {}
    )
}