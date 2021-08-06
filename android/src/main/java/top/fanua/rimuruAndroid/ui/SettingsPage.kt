package top.fanua.rimuruAndroid.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.runtime.*
import getPlatformName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.fanua.rimuruAndroid.MainActivity
import top.fanua.rimuruAndroid.ui.sustomStuff.Screen
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/6:4:01
 */
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun SettingsPage(
    path: String,
    accountFiles: MutableState<List<File>>,
    email: MutableState<String>
) {
    val composableScope = rememberCoroutineScope()
    MaterialTheme {
        Button(onClick = {
            composableScope.launch(Dispatchers.IO) {
                accountFiles.value = File(path).walk()
                    .maxDepth(1)
                    .filter { it.isFile }
                    .filter { it.extension == "@doctor@" }
                    .toList()
                accountFiles.value.forEach {
                    val pros = Properties()
                    pros.load(FileInputStream(it))
                    pros["isLogin"] = false.toString()
                    pros.store(FileOutputStream(it), "mc bot configuration file")
                }
                email.value = ""
            }
        }) {
            Text("登出")
        }
    }
}
