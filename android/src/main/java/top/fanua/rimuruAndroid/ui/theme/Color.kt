package top.fanua.rimuruAndroid.ui.theme

import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces

val black = Color(25, 25, 25).convert(ColorSpaces.CieXyz)
val white = Color(246, 246, 246).convert(ColorSpaces.CieXyz)
val white1 = Color(255, 255, 255).convert(ColorSpaces.CieXyz)
val white2 = Color(0xFFE5E5E5).convert(ColorSpaces.CieXyz)
val blue = Color(0, 155, 255).convert(ColorSpaces.CieXyz)


@Composable
fun TransparentInputField() = TextFieldDefaults.textFieldColors(
    backgroundColor = inputColor1,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent
)

val inputColor2 = Color(176, 179, 191).convert(ColorSpaces.CieXyz)
val inputColor1 = Color(242, 243, 247).convert(ColorSpaces.CieXyz)
val InputColor = Color(178, 180, 192).convert(ColorSpaces.CieXyz)
val textColor = Color(121, 121, 121).convert(ColorSpaces.CieXyz)
