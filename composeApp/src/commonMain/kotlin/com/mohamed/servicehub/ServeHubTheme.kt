package com.mohamed.servicehub
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ServeHubTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColors(
            primary = Color(0xFFFF6B1A),
            primaryVariant = Color(0xFFE95C0D),
            secondary = Color(0xFF4CAF50),
            surface = Color(0xFF1E1E1E),
            background = Color(0xFF121212),
            onPrimary = Color.White,
            onSurface = Color.White,
            onBackground = Color.White
        )
    } else {
        lightColors(
            primary = Color(0xFFFF6B1A),
            primaryVariant = Color(0xFFE95C0D),
            secondary = Color(0xFF4CAF50),
            background = Color(0xFFF8F8F8),
            surface = Color(0xFFFFFFFF),
            error = Color(0xFFE53935),
            onPrimary = Color.White,
            onSurface = Color(0xFF1E1E1E),
            onBackground = Color(0xFF1E1E1E)
        )
    }

    val typography = Typography(
        h4 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        ),
        h6 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        ),
        h5 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = Shapes(
            small = RoundedCornerShape(10.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(24.dp)
        ),
        content = content
    )
}
