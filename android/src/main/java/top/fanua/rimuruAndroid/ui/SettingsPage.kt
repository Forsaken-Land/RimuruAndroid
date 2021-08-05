package top.fanua.rimuruAndroid.ui

import androidx.compose.material.*
import androidx.compose.runtime.*
import getPlatformName
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
@Composable
fun SettingsPage(accountFiles: MutableState<List<File>>, email: MutableState<String>) {

    MaterialTheme {
        Button(onClick = {
            accountFiles.value.forEach {
                if (it.name == "${email.value}.@doctor@") {
                    val pros = Properties()
                    pros.load(FileInputStream(it))
                    pros["isLogin"] = false.toString()
                    pros.store(FileOutputStream(it), "mc bot configuration file")
                    email.value = ""
                }
            }
        }) {
            Text("登出")
        }
    }
}
