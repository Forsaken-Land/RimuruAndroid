package top.fanua.rimuruAndroid.ui

import android.os.Build
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import top.fanua.rimuruAndroid.models.RimuruViewModel

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/6:4:01
 */
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun SettingsPage() {
    Box(Modifier.fillMaxSize()) {
        val rimuruViewModel: RimuruViewModel = viewModel()
        Button(onClick = {
            rimuruViewModel.signOut()
        }) {
            Text("登出")
        }

    }
}
