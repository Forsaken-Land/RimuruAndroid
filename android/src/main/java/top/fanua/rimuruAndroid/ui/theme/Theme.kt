package top.fanua.rimuruAndroid.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.ProvideWindowInsets
import java.io.File

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
fun ImageHeader(imgUrl: String, modifier: Modifier = Modifier, size: Dp = 30.dp, dp: Dp = 45.dp) {
    Box(modifier = Modifier.size(dp).then(modifier)) {
        Image(
            painter = rememberImagePainter(
                data = File(imgUrl)
            ), contentDescription = null,
            modifier = Modifier.size(dp).then(modifier).clip(RoundedCornerShape(size * (dp / 45.dp)))
        )
        Image(
            painter = rememberImagePainter(data = File("$imgUrl.on")),
            contentDescription = null,
            modifier = Modifier.size(dp).then(modifier).clip(RoundedCornerShape(size * (dp / 45.dp))).shadow(1.dp)
        )


    }
}

private val LightColorPalette = RimuruColors(
    bottomBar = white.copy(alpha = 0.8f),
    background = white1,
    icon = black,
    iconCurrent = blue,
    timeText = textColor,
    divider = white2,
    onSurface = black1,
    textPrimary = black1,
    textPrimaryMe = white1,
    bubbleMe = blue,
    bubbleOthers = white1,
    chatBackground = white3,
)

//private val DarkColorPalette = RimuruColors(
//    bottomBar = black1,
//    background = black2,
//    icon = white5,
//    iconCurrent = green3,
//
//)

private val LocalRimuruColors = compositionLocalOf {
    LightColorPalette
}

@Stable
class RimuruColors(
    bottomBar: Color,
    background: Color,
    icon: Color,
    iconCurrent: Color,
    timeText: Color,
    divider: Color,
    onSurface: Color,
    textPrimary: Color,
    textPrimaryMe: Color,
    bubbleMe: Color,
    bubbleOthers: Color,
    chatBackground: Color,
) {
    var bottomBar: Color by mutableStateOf(bottomBar)
        private set
    var background: Color by mutableStateOf(background)
        private set
    var icon: Color by mutableStateOf(icon)
        private set
    var iconCurrent: Color by mutableStateOf(iconCurrent)
        private set
    var timeText: Color by mutableStateOf(timeText)
        private set
    var divider: Color by mutableStateOf(divider)
        private set
    var onSurface: Color by mutableStateOf(onSurface)
        private set
    var bubbleMe: Color by mutableStateOf(bubbleMe)
        private set
    var bubbleOthers: Color by mutableStateOf(bubbleOthers)
        private set
    var textPrimary: Color by mutableStateOf(textPrimary)
        private set
    var textPrimaryMe: Color by mutableStateOf(textPrimaryMe)
        private set
    var chatBackground: Color by mutableStateOf(chatBackground)
        private set
}

@Composable
fun Theme(theme: Theme.Type = Theme.Type.Light, content: @Composable () -> Unit) {
    val targetColors = when (theme) {
        Theme.Type.Light -> LightColorPalette
    }
    val bottomBar = animateColorAsState(targetColors.bottomBar, TweenSpec(600))
    val background = animateColorAsState(targetColors.background, TweenSpec(600))
    val icon = animateColorAsState(targetColors.icon, TweenSpec(600))
    val iconCurrent = animateColorAsState(targetColors.iconCurrent, TweenSpec(600))
    val timeText = animateColorAsState(targetColors.timeText, TweenSpec(600))
    val divider = animateColorAsState(targetColors.divider, TweenSpec(600))
    val onSurface = animateColorAsState(targetColors.onSurface, TweenSpec(600))
    val bubbleMe = animateColorAsState(targetColors.bubbleMe, TweenSpec(600))
    val bubbleOthers = animateColorAsState(targetColors.bubbleOthers, TweenSpec(600))
    val textPrimary = animateColorAsState(targetColors.textPrimary, TweenSpec(600))
    val textPrimaryMe = animateColorAsState(targetColors.textPrimaryMe, TweenSpec(600))
    val chatBackground = animateColorAsState(targetColors.chatBackground, TweenSpec(600))
    val colors = RimuruColors(
        bottomBar = bottomBar.value,
        background = background.value,
        icon = icon.value,
        iconCurrent = iconCurrent.value,
        timeText = timeText.value,
        divider = divider.value,
        onSurface = onSurface.value,
        bubbleMe = bubbleMe.value,
        bubbleOthers = bubbleOthers.value,
        textPrimary = textPrimary.value,
        textPrimaryMe = textPrimaryMe.value,
        chatBackground = chatBackground.value
    )

    CompositionLocalProvider(LocalRimuruColors provides colors) {
        MaterialTheme(
            shapes = shapes
        ) {
            ProvideWindowInsets(content = content)
        }
    }
}

@Stable
object Theme {
    val colors: RimuruColors
        @Composable
        get() = LocalRimuruColors.current

    enum class Type {
        Light
    }
}
