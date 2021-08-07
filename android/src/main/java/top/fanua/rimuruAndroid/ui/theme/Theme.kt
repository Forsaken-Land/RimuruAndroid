package top.fanua.rimuruAndroid.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import java.io.File

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun CustomBottomNavigationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun InputText(text: String) {
    Text(
        text,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        color = InputColor,
    )
}
@Composable
fun ImageHeader(imgUrl:String){
    Box(modifier = Modifier.padding(horizontal = 20.dp).size(45.dp)) {
        Image(
            painter = rememberImagePainter(
                data = File(imgUrl)
            ), contentDescription = null,
            modifier = Modifier.size(45.dp).clip(RoundedCornerShape(30.dp))
        )
        Image(
            painter = rememberImagePainter(data = File("$imgUrl.on")),
            contentDescription = null,
            modifier = Modifier.size(45.dp).clip(RoundedCornerShape(30.dp)).shadow(1.dp)
        )


    }
}

