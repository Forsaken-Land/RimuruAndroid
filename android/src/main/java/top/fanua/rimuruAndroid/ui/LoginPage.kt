@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package top.fanua.rimuruAndroid.ui

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.request.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import top.fanua.rimuruAndroid.ui.theme.InputColor
import top.fanua.rimuruAndroid.ui.theme.InputField
import top.fanua.rimuruAndroid.ui.theme.InputText
import top.fanua.rimuruAndroid.utils.AESUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/4:18:49
 */
@SuppressLint("NewApi")
@OptIn(ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun LoginPage(accountPath: String, loginEmail: MutableState<String>) {
    val composableScope = rememberCoroutineScope()
    val fileExtension = "@doctor@"
    val accounts = remember { mutableMapOf<String, File>() }
    val userDir = File(accountPath)
    if (!userDir.exists()) userDir.mkdirs()

    composableScope.launch(Dispatchers.IO) {
        val accountFiles = File(accountPath).walk()
            .maxDepth(1)
            .filter { it.isFile }
            .filter { it.extension == "@doctor@" }
            .toList()
        accountFiles.forEach {
            accounts[it.name.replace(".$fileExtension", "")] = it
        }
    }


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

        TextField(
            modifier = Modifier.width(320.dp),
            value = email,
            onValueChange = {
                email = it
            },
            placeholder = {
                if (!isEmailWillWrite) InputText("输入邮箱")
            },
            shape = RoundedCornerShape(30.dp),
            singleLine = true,
            colors = InputField(),
            textStyle = TextStyle(textAlign = TextAlign.Center).copy(fontSize = 18.sp),
            interactionSource = emailInteractionSource,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
                if (accounts[email] == null) Surface(
                    modifier = Modifier.size(45.dp),
                    color = Color.Transparent
                ) { } else Surface(
                    modifier = Modifier.size(45.dp),
                    color = Color.Transparent,
                    shape = RoundedCornerShape(30.dp)
                ) {
                    val imgUrl = remember { mutableStateOf("") }
                    composableScope.launch {
                        withContext(Dispatchers.IO) {
                            val pros = Properties()
                            val file = accounts[email]!!
                            pros.load(FileInputStream(file))
                            imgUrl.value = pros.getProperty("imgUrl")
                        }
                    }
                    if (!showAccount) {

                        Image(
                            painter = rememberImagePainter(
                                data = imgUrl.value
                            ), contentDescription = null
                        )
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
                            var pros by remember { mutableStateOf<Properties?>(null) }
                            composableScope.launch(Dispatchers.IO) {
                                val properties = Properties().also {
                                    it.load(FileInputStream(file))
                                }
                                Thread.sleep(10L)
                                pros = properties

                            }
                            if (pros != null) {
                                val localEmail = pros!!.getProperty("email")
                                val imgUrl = pros!!.getProperty("imgUrl")
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable(
                                        interactionSource = MutableInteractionSource(),
                                        indication = null
                                    ) {
                                        email = localEmail
                                        showAccount = false
                                    }

                                ) {
                                    Surface(
                                        modifier = Modifier.padding(horizontal = 20.dp).size(45.dp),
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(30.dp)
                                    ) {
                                        Image(
                                            painter = rememberImagePainter(
                                                data = imgUrl
                                            ), contentDescription = null
                                        )
                                    }
                                    Text(
                                        localEmail,
                                        textAlign = TextAlign.Center,
                                        fontSize = 20.sp,
                                        maxLines = 1,
                                        modifier = Modifier.fillParentMaxWidth(0.6f)
                                    )
                                }


                            }


                        }
                    }
                }
            }
        }
        else Box {
            TextField(
                modifier = Modifier.width(320.dp).padding(top = 10.dp),
                value = password,
                onValueChange = {
                    password = it
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
                    if (password.length >= 8 && email.contains("@").also { email.contains(".") }) {
                        composableScope.launch(Dispatchers.IO) {
                            val file = File("$userDir/$email.$fileExtension")
                            if (!file.exists()) file.createNewFile()
                            val pros = Properties()
                            pros.load(FileInputStream(file))
                            pros["email"] = email
                            pros["password"] = AESUtils.encrypt(
                                password,
                                "7CB2RGS6D1UIJIE2"
                            )
                            val client = OkHttpClient()
                            val getRequest: Request = Request.Builder()
                                .url("https://api.btstu.cn/sjtx/api.php?lx=b1")
                                .get()
                                .build()
                            val url = client.newCall(getRequest).execute().request().url().url().toString()
                            pros["imgUrl"] = url
                            pros["isLogin"] = true.toString()
                            pros.store(FileOutputStream(file), "mc bot configuration file")
                            loginEmail.value = email

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


        }


    }


}
