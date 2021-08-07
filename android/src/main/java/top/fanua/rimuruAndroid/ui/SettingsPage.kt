package top.fanua.rimuruAndroid.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.runtime.*
import top.fanua.rimuruAndroid.utils.UserViewModel

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/6:4:01
 */
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun SettingsPage(
    userViewModel: UserViewModel,
    email: MutableState<String>
) {
    MaterialTheme {
        Button(onClick = {
            userViewModel.signOut(email)

        }) {
            Text("登出")
        }
    }
}
