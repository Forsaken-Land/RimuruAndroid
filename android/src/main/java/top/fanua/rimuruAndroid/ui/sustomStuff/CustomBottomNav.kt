package top.fanua.rimuruAndroid.ui.sustomStuff

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.fanua.rimuruAndroid.ui.theme.Theme

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/3:17:20
 */
@Composable
fun CustomBottomNavigation(
    currentScreenId: String,
    onItemSelected: (Screen) -> Unit
) {
    val items = Screen.Items.list

    Row(
        modifier = Modifier.background(Theme.colors.bottomBar)
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { screen ->
            CustomBottomNavigationItem(screen = screen, isSelected = screen.id == currentScreenId) {
                onItemSelected(screen)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomBottomNavigationItem(
    screen: Screen,
    isSelected: Boolean,
    onClick: () -> Unit
) {

//    val background = if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.1f) else Color.Transparent
    val contentColor = if (isSelected) Theme.colors.iconCurrent else Theme.colors.icon

    Box(
        modifier = Modifier.clip(CircleShape)
            .clickable(interactionSource = MutableInteractionSource(), indication = null, onClick = onClick)

    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = screen.icon,
                contentDescription = null,
                tint = contentColor
            )
            AnimatedVisibility(visible = isSelected) {
                Text(
                    text = screen.title,
                    color = contentColor
                )

            }
        }
    }
}
