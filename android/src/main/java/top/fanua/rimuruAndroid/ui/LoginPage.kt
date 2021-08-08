@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package top.fanua.rimuruAndroid.ui

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.fanua.rimuruAndroid.data.Account
import top.fanua.rimuruAndroid.ui.theme.InputColor
import top.fanua.rimuruAndroid.ui.theme.InputField
import top.fanua.rimuruAndroid.ui.theme.InputText
import top.fanua.rimuruAndroid.utils.FileUtils
import top.fanua.rimuruAndroid.models.LoginStatus.*
import top.fanua.rimuruAndroid.models.UserViewModel
import top.fanua.rimuruAndroid.ui.theme.ImageHeader
import java.io.FileNotFoundException

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/4:18:49
 */
@SuppressLint("NewApi")
@OptIn(ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun LoginPage(userViewModel: UserViewModel, loginEmail: MutableState<String>) {
    val composableScope = rememberCoroutineScope()
    val accounts = userViewModel.accounts
    val userDir = userViewModel.getUserDir()
    if (!userDir.exists()) userDir.mkdirs()
    userViewModel.refresh()


    val keyboardController = LocalSoftwareKeyboardController.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var onlyOne by remember { mutableStateOf(true) }
    if (accounts.isNotEmpty() && onlyOne && email.isEmpty() && password.isEmpty()) {
        val user = FileUtils.readFile<Account>(accounts.values.toList()[0])
        email = user.email
        password = user.password
        onlyOne = false
    }
    val emailInteractionSource = remember {
        MutableInteractionSource()
    }
    val passwordInteractionSource = remember {
        MutableInteractionSource()
    }
    val isEmailWillWrite = emailInteractionSource.collectIsFocusedAsState().value
    val isPasswordWillWrite = passwordInteractionSource.collectIsFocusedAsState().value
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp)
            .height(800.dp)
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
            modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight(0.1f),
            horizontalArrangement = Arrangement.End
        ) {
            Text("by:遗落之地", color = Color.LightGray)
        }

        var showAccount by remember { mutableStateOf(false) }
        if (accounts.isEmpty()) showAccount = false

        TextField(
            modifier = Modifier.width(320.dp),
            value = email,
            onValueChange = {
                email = it.replace(" ", "").replace("\n", "")
            },
            placeholder = {
                if (!isEmailWillWrite) InputText("输入邮箱")
            },
            shape = RoundedCornerShape(30.dp),
            singleLine = true,
            maxLines = 1,
            colors = InputField(),
            textStyle = TextStyle(textAlign = TextAlign.Center).copy(fontSize = 18.sp),
            interactionSource = emailInteractionSource,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
            trailingIcon = {
                Row {
                    if (accounts.isEmpty()) {
                        if (!isEmailWillWrite) Surface(
                            color = Color.Transparent,
                            modifier = Modifier.padding(horizontal = 10.dp).size(40.dp)
                        ) { }
                        if (isEmailWillWrite && email.isEmpty()) Surface(
                            color = Color.Transparent,
                            modifier = Modifier.padding(horizontal = 10.dp).size(40.dp)
                        ) { }
                    }
                    if (email.isNotEmpty()) {
                        if (isEmailWillWrite) {
                            if (accounts.isEmpty()) {
                                Surface(
                                    color = Color.Transparent,
                                    modifier = Modifier.size(20.dp)
                                ) { }
                            }
                            Icon(
                                Icons.Rounded.Clear,
                                contentDescription = null,
                                tint = InputColor,
                                modifier = Modifier
                                    .padding(
                                        horizontal = if (accounts.isEmpty()) 20.dp else 0.dp
                                    )
                                    .offset(
                                        y = if (accounts.isEmpty()) 0.dp else 10.dp,
                                        x = if (accounts.isEmpty()) 0.dp else 10.dp
                                    )
                                    .size(20.dp).clickable(
                                        interactionSource = MutableInteractionSource(),
                                        indication = null
                                    ) {
                                        email = ""
                                        password = ""
                                        showAccount = false
                                    }
                            )
                        } else Surface(
                            color = Color.Transparent,
                            modifier = Modifier.size(20.dp)
                        ) { }
                    } else Surface(color = Color.Transparent, modifier = Modifier.size(15.dp)) {}
                    if (accounts.isNotEmpty()) Icon(
                        if (showAccount) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                        contentDescription = null,
                        tint = Color(176, 179, 191),
                        modifier = Modifier.padding(horizontal = 10.dp).size(40.dp).clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            showAccount = !showAccount
                            keyboardController?.hide()
                        }
                    )

                }
            },
            leadingIcon = {
                userViewModel.refresh()
                if (accounts[email] == null) Surface(
                    modifier = Modifier.size(45.dp),
                    color = Color.Transparent
                ) { } else {
                    var imgUrl by remember { mutableStateOf("") }
                    imgUrl = accounts[email]?.let { FileUtils.readFile<Account>(it).imgUrl }.orEmpty()
                    if (!showAccount) {
                        ImageHeader(imgUrl)
                    }
                }
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier.size(60.dp)
                ) { }
                //头像获取
            }
        )


        var showPassword by remember { mutableStateOf(false) }
        var warning by remember { mutableStateOf(false) }
        if (password.length >= 8) warning = false

        if (showAccount) Box {
            val size: Double = if (accounts.size >= 5) {
                4.5 * 60
            } else {
                accounts.size * 60.0
            }
            Surface(
                modifier = Modifier.padding(top = 10.dp).width(320.dp).height(size.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                LazyColumn() {
                    itemsIndexed(accounts.values.toList()) { _, file ->
                        Surface(
                            modifier = Modifier.height(60.dp).fillParentMaxWidth(),
                            color = Color(242, 243, 247)
                        ) {
                            var account by remember { mutableStateOf<Account?>(null) }
                            composableScope.launch(Dispatchers.IO) {
                                Thread.sleep(10L)
                                account = try {
                                    FileUtils.readFile<Account>(file)
                                } catch (e: FileNotFoundException) {
                                    null
                                }

                            }
                            if (account != null) {
                                val localEmail = account!!.email
                                val imgUrl = account!!.imgUrl
                                val localPassword = account!!.password
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable(
                                        interactionSource = MutableInteractionSource(),
                                        indication = null
                                    ) {
                                        password = localPassword
                                        email = localEmail
                                        showAccount = false
                                    }

                                ) {
                                    ImageHeader(imgUrl)
                                    Text(
                                        localEmail,
                                        textAlign = TextAlign.Center,
                                        fontSize = 20.sp,
                                        maxLines = 1,
                                        modifier = Modifier.fillParentMaxWidth(0.6f)
                                    )
                                    Icon(
                                        Icons.Rounded.Clear,
                                        contentDescription = null,
                                        tint =
                                        InputColor,
                                        modifier = Modifier.size(20.dp).clickable(
                                            interactionSource = MutableInteractionSource(),
                                            indication = null
                                        ) {
                                            userViewModel.delUser(account!!.email)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            var loginStatus by remember { mutableStateOf(WAITING) }
            val errorClose = remember { mutableStateOf(false) }
            when (loginStatus) {
                OK -> loginEmail.value = email
                ERROR -> Dialog("登录出错", loginStatus.msg, errorClose).also {
                    loginStatus = WAITING
                }
                UNKNOWN -> Dialog("未知错误", loginStatus.msg, errorClose).also {
                    loginStatus = WAITING
                }
                WAITING -> Log.e("", "")
                LOGGING_IN -> Log.e("", "")
            }
            if (errorClose.value) {
                loginStatus = WAITING
                errorClose.value = false
            }
            TextField(
                modifier = Modifier.width(320.dp).padding(top = 10.dp),
                value = password,
                onValueChange = {
                    password = it.replace("\n", "").replace(" ", "")
                },
                interactionSource = passwordInteractionSource,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                placeholder = {
                    if (!isPasswordWillWrite) InputText("输入密码")
                },
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                colors = InputField(),
                textStyle = TextStyle(textAlign = TextAlign.Center).copy(
                    fontSize = 20.sp,
                    letterSpacing = if (showPassword) 0.sp else 10.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    if (password.length >= 8 &&
                        email.contains("@").also { email.contains(".") } &&
                        loginStatus != LOGGING_IN
                    ) {
                        loginStatus = LOGGING_IN
                        composableScope.launch(Dispatchers.IO) {
                            loginStatus = userViewModel.login(email, password)
                        }
                    }
                }),
                leadingIcon = {
                    if (isPasswordWillWrite) {
                        if (!showPassword)
                            Icon(Icons.Rounded.VisibilityOff,
                                contentDescription = null,
                                tint = InputColor,
                                modifier = Modifier.padding(horizontal = 20.dp)
                                    .clickable(
                                        interactionSource = MutableInteractionSource(),
                                        indication = null
                                    ) { showPassword = true }
                                    .size(20.dp)
                            )
                        else Icon(
                            Icons.Rounded.Visibility,
                            contentDescription = null,
                            tint = InputColor,
                            modifier = Modifier.padding(horizontal = 20.dp)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null
                                ) { showPassword = false }
                                .size(20.dp)
                        )
                    } else
                        Surface(
                            modifier = Modifier.size(20.dp),
                            color = Color.Transparent
                        ) { }
                },
                trailingIcon = {
                    Row {
                        if (!isPasswordWillWrite) {
                            if (password.isNotEmpty()) {
                                if (password.length >= 8) {
                                    Surface(
                                        modifier = Modifier.padding(horizontal = 10.dp).size(20.dp),
                                        color = Color.Transparent
                                    ) { }
                                }
                                Surface(
                                    modifier = Modifier.size(5.dp),
                                    color = Color.Transparent
                                ) { }
                            }
                        }
                        if (password.length > 7)
                            Surface(
                                modifier = Modifier.size(20.dp),
                                color = Color.Transparent
                            ) { }
                        if (password.isNotEmpty() && password.length < 8) Icon(
                            Icons.Rounded.WarningAmber,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = if (isPasswordWillWrite) Modifier.offset(x = 10.dp).clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                warning = !warning
                            }.size(20.dp) else Modifier.padding(horizontal = 20.dp).clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                warning = !warning
                            }.size(20.dp)
                        )
                        if (isPasswordWillWrite && password.isNotEmpty()) Icon(
                            Icons.Rounded.Clear,
                            contentDescription = null,
                            tint = InputColor,
                            modifier = Modifier.padding(horizontal = 20.dp).clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                password = ""
                            }.size(20.dp)
                        )
                        if (password.isEmpty()) {
                            Surface(
                                modifier = Modifier.padding(horizontal = 20.dp).size(20.dp),
                                color = Color.Transparent
                            ) { }
                            if (isPasswordWillWrite) {
                                Surface(
                                    modifier = Modifier.padding(horizontal = 5.dp).size(10.dp),
                                    color = Color.Transparent
                                ) { }
                            }
                        }
                    }
                }
            )
            Row(
                modifier = Modifier.width(320.dp).height(50.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (password.isNotEmpty() && warning) Text(
                    text = "密码长度需要8位\n 当前长度:${password.length}",
                    textAlign = TextAlign.Center,
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            Surface(
                modifier = Modifier.padding(top = 60.dp)
                    .size(80.dp)
                    .clickable(
                        indication = null,
                        interactionSource = MutableInteractionSource(),
                        enabled = (password.length >= 8 &&
                                email.contains("@").also { email.contains(".") } &&
                                loginStatus != LOGGING_IN)
                    ) {
                        keyboardController?.hide()
                        loginStatus = LOGGING_IN
                        composableScope.launch(Dispatchers.IO) {
                            loginStatus = userViewModel.login(email, password)
                        }

                    },
                shape = RoundedCornerShape(40.dp),
                color = if (password.length >= 8 && email.contains("@")
                        .also { email.contains(".") }
                ) Color(90, 189, 225)
                else Color(231, 236, 242)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowForward, contentDescription = null,
                    tint = White,
                    modifier = Modifier.padding(28.dp)
                )


            }
        }


    }


}
