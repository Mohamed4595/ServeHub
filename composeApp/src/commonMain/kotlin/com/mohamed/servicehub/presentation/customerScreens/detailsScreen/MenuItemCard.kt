package com.mohamed.servicehub.presentation.customerScreens.detailsScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamed.servicehub.presentation.components.FoodThumbnail
import com.mohamed.servicehub.PreviewContainer
import com.mohamed.servicehub.presentation.components.ServeHubBorder
import com.mohamed.servicehub.presentation.components.ServeHubInk
import com.mohamed.servicehub.presentation.components.ServeHubMuted
import com.mohamed.servicehub.presentation.components.menuDescription
import com.mohamed.servicehub.presentation.MenuItem
import com.mohamed.servicehub.previewMenuItems
import com.mohamed.servicehub.presentation.components.toCurrency
import org.jetbrains.compose.resources.painterResource
import servehub.composeapp.generated.resources.Res
import servehub.composeapp.generated.resources.icon_add

@Composable
 fun MenuItemCard(item: MenuItem, onAdd: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp,
        backgroundColor = Color.White,
        modifier = Modifier.border(1.dp, ServeHubBorder, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FoodThumbnail(label = item.name, size = 72.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.Bold, color = ServeHubInk)
                Spacer(modifier = Modifier.height(4.dp))
                Text(menuDescription(item.name), color = ServeHubMuted, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(6.dp))
                Text("${item.price.toCurrency()} EGP", color = ServeHubInk, fontWeight = FontWeight.Bold)
            }
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .clickable(onClick = onAdd),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.icon_add),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun MenuItemCardPreview() = PreviewContainer {
    MenuItemCard(
        item = previewMenuItems.first(),
        onAdd = {}
    )
}