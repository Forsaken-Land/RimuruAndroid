package top.fanua.rimuruAndroid.ui

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import top.fanua.rimuruAndroid.data.*
import top.fanua.rimuruAndroid.models.LoginStatus.*
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.ui.theme.*
import kotlin.math.roundToInt

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/9:3:10
 */
@OptIn(ExperimentalComposeUiApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun LoginPage() {
    var isLoginStatus by remember { mutableStateOf(true) }

    val viewModel: RimuruViewModel = viewModel()
    val keyboardController = LocalSoftwareKeyboardController.current


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    val emailInteractionSource = remember {
        MutableInteractionSource()
    }
    val passwordInteractionSource = remember {
        MutableInteractionSource()
    }
    val isEmailWillWrite = emailInteractionSource.collectIsFocusedAsState().value
    val isPasswordWillWrite = passwordInteractionSource.collectIsFocusedAsState().value


    var showPassword by remember { mutableStateOf(false) }
    var canLogin by remember { mutableStateOf(false) }

    canLogin = email.contains("@") &&
            email.contains(".")


    var showImg by remember { mutableStateOf(false) }
    var showAccount by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        Header()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 150.dp)
        ) {
            Box(Modifier.width(320.dp).height(60.dp).offset(y = (-10).dp)) {
                TextField(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight().align(alignment = Alignment.Center),
                    value = email,
                    onValueChange = {
                        email = it.replace(" ", "").replace("\n", "")
                    },
                    leadingIcon = { Spacer(Modifier.size(75.dp)) },
                    trailingIcon = { Spacer(Modifier.size(75.dp)) },
                    placeholder = {
                        if (!isEmailWillWrite) InputText("输入邮箱")
                    },
                    readOnly = !isLoginStatus,
                    shape = RoundedCornerShape(30.dp),
                    singleLine = true,
                    maxLines = 1,
                    colors = TransparentInputField(),
                    textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 18.sp),
                    interactionSource = emailInteractionSource,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
                )


                var image by remember { mutableStateOf("") }
                viewModel.accounts.get(email).also {
                    showImg = if (it != null) {
                        image = it.saveAccount.icon.toString()
                        true
                    } else false
                }

                if (showImg && !showAccount) ImageHeader(image, Modifier.align(Alignment.CenterStart).offset(x = 8.dp))

                if (email.isNotEmpty() && isEmailWillWrite) Icon(
                    Icons.Rounded.Clear, contentDescription = null, tint = InputColor, modifier =
                    Modifier.align(Alignment.CenterEnd)
                        .offset(x = if (viewModel.accounts.isEmpty()) (-25).dp else (-55).dp).size(20.dp)
                        .clickableWithout(isLoginStatus) {
                            email = ""
                            password = ""
                        }
                )

                if (viewModel.accounts.isNotEmpty()) Icon(
                    if (showAccount) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd).offset(x = (-8).dp).size(45.dp)
                        .clickableWithout(isLoginStatus) {
                            keyboardController?.hide()
                            showAccount = !showAccount
                        },
                    tint = inputColor2
                )


            }
            var size by remember { mutableStateOf(0.0) }
            size = if (viewModel.accounts.size > 5) 60 * 4.5 else viewModel.accounts.size * 60.0
            AnimatedVisibility(visible = showAccount) {
                LazyColumn(
                    Modifier.clip(RoundedCornerShape(10.dp)).width(320.dp)
                        .height(size.dp).background(inputColor1)
                ) {
                    itemsIndexed(viewModel.accounts) { _, account ->
                        Box(
                            modifier = Modifier
                                .width(320.dp)
                                .height(60.dp)
                                .clickableWithout(isLoginStatus) {
                                    email = account.password.email
                                    password = account.password.password
                                    showAccount = false
                                }
                        ) {
                            ImageHeader(
                                account.saveAccount.icon.toString(),
                                modifier = Modifier.align(Alignment.CenterStart).offset(x = 10.dp),
                                size = viewModel.radian.dp
                            )
                            Text(
                                account.saveAccount.email,
                                modifier = Modifier.width(200.dp).align(Alignment.Center).offset(x = 10.dp),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                softWrap = false
                            )
                            Icon(
                                Icons.Rounded.Clear,
                                contentDescription = null,
                                tint = InputColor,
                                modifier = Modifier.align(Alignment.CenterEnd).offset(x = (-15).dp).size(20.dp)
                                    .clickableWithout(isLoginStatus) {

                                        //TODO
                                        showAccount = false
                                    }
                            )
                        }
                    }
                }
            }
            var loginStatus by remember { mutableStateOf(WAITING) }
            val errorClose = remember { mutableStateOf(false) }
            if (errorClose.value) {
                loginStatus = WAITING
                errorClose.value = false
            }
            isLoginStatus = when (loginStatus) {
                LOGGING_IN -> false
                else -> true
            }
            if (isLoginStatus) {
                if (loginStatus == ERROR) {
                    Dialog("登录出错", loginStatus.msg, errorClose)
                } else if (loginStatus == UNKNOWN) {
                    Dialog("未知错误", loginStatus.msg, errorClose)
                }
            }

            if (!showAccount) {
                Box(Modifier.offset(y = 10.dp).width(320.dp).height(60.dp)) {
                    TextField(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight().align(alignment = Alignment.Center),
                        value = password,
                        readOnly = !isLoginStatus,
                        onValueChange = {
                            password = it.replace("\n", "").replace(" ", "")
                        },
                        interactionSource = passwordInteractionSource,
                        leadingIcon = { Surface(Modifier.size(60.dp), color = Color.Transparent) { } },
                        trailingIcon = { Surface(Modifier.size(60.dp), color = Color.Transparent) { } },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        placeholder = {
                            if (!isPasswordWillWrite) InputText("输入密码")
                        },
                        shape = RoundedCornerShape(30.dp),
                        singleLine = true,
                        colors = TransparentInputField(),
                        textStyle = TextStyle(textAlign = TextAlign.Center).copy(
                            fontSize = 20.sp,
                            letterSpacing = if (showPassword) 0.sp else 10.sp
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                if (canLogin && isLoginStatus) {
                                    loginStatus = LOGGING_IN
                                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                                        loginStatus = viewModel.loginAccount(email, password)
                                        Log.i("登录信息", "$email: $loginStatus")
                                    }
                                }
                            }
                        )
                    )
                    if (isPasswordWillWrite) {
                        Icon(if (showPassword) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                            contentDescription = null,
                            tint = InputColor,
                            modifier = Modifier.align(Alignment.CenterStart).offset(x = 20.dp)
                                .clickableWithout(isLoginStatus) { showPassword = !showPassword }
                                .size(20.dp)
                        )
                        if (password.isNotEmpty()) {
                            Icon(
                                Icons.Rounded.Clear,
                                contentDescription = null,
                                tint = InputColor,
                                modifier = Modifier.align(Alignment.CenterEnd).offset(x = (-20).dp)
                                    .clickableWithout(isLoginStatus) {
                                        password = ""
                                    }.size(20.dp)
                            )
                        }

                    }
                }

                Icon(
                    imageVector = Icons.Rounded.ArrowForward, contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(80.dp)
                        .offset(y = 50.dp)
                        .clickableWithout(canLogin && isLoginStatus) {
                            keyboardController?.hide()
                            loginStatus = LOGGING_IN
                            viewModel.viewModelScope.launch(Dispatchers.IO) {
                                loginStatus = viewModel.loginAccount(email, password)
                                Log.i("登录信息", "$email: $loginStatus")
                            }

                        }
                        .clip(RoundedCornerShape(40.dp))
                        .background(if (canLogin) Color(90, 189, 225) else Color(231, 236, 242))
                        .padding(28.dp)
                )


            }

        }
        AccountServer(viewModel)
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AccountServer(viewModel: RimuruViewModel) {
    var show by remember { mutableStateOf(false) }
    var addAuth by remember { mutableStateOf(false) }
    if (addAuth) {
        AddAccountServer {
            viewModel.viewModelScope.launch(Dispatchers.IO) {

                val response = try {
                    OkHttpClient().newCall(Request.Builder().get().url(it).build())
                        .execute()
                } catch (e: Exception) {
                    null
                }
                if (response != null) {
                    val url = response.headers["x-authlib-injector-api-location"].orEmpty()
                    if (url.isNotEmpty()) {
                        val json = String(
                            OkHttpClient().newCall(Request.Builder().get().url(url).build()).execute().body!!.bytes()
                        )
                        val body = Json.decodeFromString<YggdrasilBody>(json)
                        val authServer = "$url/authserver/"
                        val sessionServer = "$url/sessionserver/"
                        viewModel.accountDao!!.insertSaveAuth(
                            SaveAuth(
                                authServer = authServer,
                                sessionServer = sessionServer,
                                name = body.meta.serverName
                            )
                        )
                        Log.e("url", url)
                    }
                }


                addAuth = false
                show = false
            }
        }
    }
    Box(modifier = Modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeable.placeRelative(100, constraints.maxHeight - placeable.height - 100)
        }
    }.height(200.dp).width(300.dp)) {
        Column {
            AnimatedVisibility(
                visible = show,
                enter = slideInVertically({ it }) + expandVertically(Alignment.Bottom, { it }) + fadeIn(0.3f),
                exit = slideOutVertically({ it }) + shrinkVertically(Alignment.Bottom, { it }) + fadeOut()
            ) {
                LazyColumn(
                    Modifier.clip(RoundedCornerShape(10.dp)).width(300.dp)
                        .height(160.dp).background(inputColor1)
                ) {
                    item {
                        Row(Modifier.fillParentMaxWidth().height(30.dp)) {
                            Text(
                                "正版(未支持)",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().clickable {

                                })
                        }

                    }
                    items(viewModel.authList) {
                        Row(Modifier.fillParentMaxWidth().height(40.dp)) {
                            Text(
                                it.name,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().clickable {
                                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                                        val authServer =
                                            viewModel.accountDao!!.getConfig("authServer").firstOrNull()!!
                                        val sessionServer =
                                            viewModel.accountDao!!.getConfig("sessionServer").firstOrNull()!!
                                        viewModel.accountDao!!.updateConfig(
                                            authServer.copy(value = it.authServer),
                                            sessionServer.copy(value = it.sessionServer)
                                        )
                                    }
                                }
                            )
                        }
                    }
                    item {
                        Row(Modifier.fillParentMaxWidth().height(40.dp)) {
                            Text(
                                "添加外置服务器",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().clickable {
                                    addAuth = true
                                }
                            )
                        }
                    }
                }
            }

        }
        IconButton(onClick = {
            show = !show
        }, modifier = Modifier.offset(y = 160.dp).shadow(5.dp, RoundedCornerShape(50.dp)).background(Color.Red)) {
            Icon(if (show) Icons.Outlined.Close else Icons.Outlined.Menu, null)
        }

    }

}


@Composable
private fun Header() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset(y = 40.dp)
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.6f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                modifier = Modifier.size(40.dp)

            )
            Text(
                text = "MC-Robot",
                fontSize = 30.sp,
                maxLines = 1,
                color = Color.Gray
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.8f).height(20.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text("by:遗落之地", color = Color.LightGray)
        }
    }
}

fun Modifier.clickableWithout(enable: Boolean = true, onClick: () -> Unit): Modifier {
    return this.clickable(
        interactionSource = MutableInteractionSource(),
        indication = null,
        onClick = onClick,
        enabled = enable
    )
}

fun List<EmailWithPassword>.get(string: String): EmailWithPassword? {
    if (isEmpty()) return null
    forEach {
        if (it.saveAccount.email == string) return it
    }
    return null
}


