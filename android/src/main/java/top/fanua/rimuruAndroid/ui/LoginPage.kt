package top.fanua.rimuruAndroid.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.room.*
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.LoadPainterDefaults
import kotlinx.coroutines.flow.Flow
import top.fanua.rimuruAndroid.MainActivity
import top.fanua.rimuruAndroid.ui.theme.InputColor
import top.fanua.rimuruAndroid.ui.theme.InputField
import top.fanua.rimuruAndroid.ui.theme.InputText
import java.io.File

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/4:18:49
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginPage(userDao: UserDao) {
    val users = userDao.queryAll().value.orEmpty()

    val accounts = remember { mutableMapOf<String, User>() }
    users.forEach { user: User ->
        accounts[user.email] = user
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
                    if (accounts.isEmpty() && !isEmailWillWrite) Surface(
                        color = Color.Transparent,
                        modifier = Modifier.padding(horizontal = 10.dp).size(40.dp)
                    ) { }
                    if (isEmailWillWrite && email.isNotEmpty()) Icon(
                        Icons.Rounded.Clear,
                        contentDescription = null,
                        tint = InputColor,
                        modifier = Modifier.padding(horizontal = 20.dp).clickable {
                            email = ""
                        }.size(20.dp)
                    )
                    if (accounts.isNotEmpty()) Icon(
                        if (showAccount) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                        contentDescription = null,
                        tint = Color(176, 179, 191),
                        modifier = Modifier.padding(horizontal = 10.dp).size(40.dp).clickable {
                            showAccount = !showAccount
                            keyboardController?.hide()
                        }
                    )

                }
            },
            leadingIcon = {
                if (accounts[email] == null) Surface(
                    modifier = Modifier.offset(x = 5.dp).size(40.dp),
                    color = Color.Transparent
                ) { } else Surface(
                    modifier = Modifier.offset(x = 5.dp).size(40.dp),
                    color = if (showAccount) Color.Transparent else Color.White,
                    shape = RoundedCornerShape(30.dp)
                ) {
                    if (!showAccount) Image(
                        painter = rememberImagePainter(
                            data = "https://www.baidu.com/favicon.ico"
                        ), contentDescription = null
                    )
                }

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
                color = Color(242, 243, 247)
            ) { }
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
                }),
                leadingIcon = {
                    if (isPasswordWillWrite) {
                        if (!showPassword)
                            Icon(Icons.Rounded.VisibilityOff,
                                contentDescription = null,
                                tint = InputColor,
                                modifier = Modifier.padding(horizontal = 20.dp)
                                    .clickable { showPassword = true }
                                    .size(20.dp)
                            )
                        else Icon(
                            Icons.Rounded.Visibility,
                            contentDescription = null,
                            tint = InputColor,
                            modifier = Modifier.padding(horizontal = 20.dp)
                                .clickable { showPassword = false }
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
                            modifier = if (isPasswordWillWrite) Modifier.offset(x = 10.dp).clickable {
                                warning = !warning
                            }.size(20.dp) else Modifier.padding(horizontal = 20.dp).clickable {
                                warning = !warning
                            }.size(20.dp)
                        )
                        if (isPasswordWillWrite && password.isNotEmpty()) Icon(
                            Icons.Rounded.Clear,
                            contentDescription = null,
                            tint = InputColor,
                            modifier = Modifier.padding(horizontal = 20.dp).clickable {
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

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    val id: Int? = null,
    val email: String,
    val password: String,
    val imgUrl: String,
    val isLogin: Boolean
)

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun queryAll(): LiveData<List<User>>

    @Insert
    fun insert(entity: User)

    @Query("DELETE FROM user")
    fun delete()
}

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    //获得数据访问对象
    abstract fun getUserDao(): UserDao
}
