package top.fanua.rimuruAndroid.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import top.fanua.rimuruAndroid.utils.Info


/**
 *
 * @author Doctor_Yin
 * @since 2021/8/7:18:55
 */
class UserViewModel : ViewModel() {
    var devVersion by mutableStateOf("")
    var releaseVersion by mutableStateOf("")
    var error by mutableStateOf(false)
    var load by mutableStateOf(true)
    var data by mutableStateOf(Info(emptyList()))
    var first by mutableStateOf(true)
}

enum class LoginStatus(var msg: String) {
    OK(""),
    ERROR(""),
    UNKNOWN(""),
    WAITING(""),
    LOGGING_IN("")
}







