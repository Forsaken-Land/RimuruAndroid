package top.fanua.rimuruAndroid.ui.theme

import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces

val black = Color(25, 25, 25)
val black1 = Color(0xFF000000)
val white = Color(246, 246, 246)
val white1 = Color(255, 255, 255)
val white2 = Color(0xFFE5E5E5)
val white3 = Color(245, 245, 245)
val white4 = Color(242, 242, 242)
val blue = Color(0, 155, 255)


@Composable
fun TransparentInputField() = TextFieldDefaults.textFieldColors(
    backgroundColor = inputColor1,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent
)

val inputColor2 = Color(176, 179, 191)
val inputColor1 = Color(242, 243, 247)
val InputColor = Color(178, 180, 192)
val textColor = Color(121, 121, 121)
