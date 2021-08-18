package top.fanua.rimuruAndroid.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


/**
 *
 * @author Doctor_Yin
 * @since 2021/8/7:18:55
 */
class UserViewModel : ViewModel() {
}

enum class LoginStatus(var msg: String) {
    OK(""),
    ERROR(""),
    UNKNOWN(""),
    WAITING(""),
    LOGGING_IN("")
}







