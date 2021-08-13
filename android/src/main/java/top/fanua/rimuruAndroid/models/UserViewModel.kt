package top.fanua.rimuruAndroid.models


/**
 *
 * @author Doctor_Yin
 * @since 2021/8/7:18:55
 */
//@OptIn(InternalCoroutinesApi::class)
//class UserViewModel(
//    private val accountPath: String,
//    val accountFiles: MutableState<List<File>>,
//    val accounts: MutableMap<String, File>,
//    val email: MutableState<String>
//) : ViewModel() {
//    private val fileExtension = "@doctor@"
//    private val authServerUrl = "https://skin.blackyin.xyz/api/yggdrasil/"
//
//    enum class ContentType(val type: MediaType) {
//        APPLICATION_JSON(MediaType.get("application/json")),
//        IMAGE_PNG(MediaType.get("image/png"))
//    }
//
//
//    @OptIn(ExperimentalSerializationApi::class)
//    private fun <T> createService(
//        serviceClass: Class<T>,
//        type: ContentType
//    ): T {
//        return Retrofit.Builder()
//            .baseUrl(authServerUrl)
//            .addConverterFactory(Json.asConverterFactory(type.type))
//            .build().create(serviceClass)
//    }
//
//    private val jsonHttpClient = createService(HttpClient::class.java, ContentType.APPLICATION_JSON)
//    private val imageHttpClient = createService(HttpClient::class.java, ContentType.IMAGE_PNG)
//
//
//    suspend fun getImg(uuid: String): ByteArray {
//        val userProfile = jsonHttpClient.roleProperties(uuid).execute().body()!!
//        var textureTemp = ""
//        userProfile.properties?.forEach {
//            if (it.name == "textures") textureTemp = it.value
//        }
//
//        val texture = String(Base64.decode(textureTemp.toByteArray(), Base64.DEFAULT))
//        val textureUrl = Json.parseToJsonElement(texture)
//            .jsonObject["textures"]!!
//            .jsonObject["SKIN"]!!
//            .jsonObject["url"]!!
//            .jsonPrimitive.content
//
//
//        val img = imageHttpClient.downloadImage(textureUrl).execute().body()!!
//        return img.bytes()
//
//    }
//
//    suspend fun login(email: String, password: String): LoginStatus {
//        val job = viewModelScope.launch(Dispatchers.IO) {
//            val loginRequest = LoginRequest(email, password, requestUser = true)
//            val response = jsonHttpClient.login(loginRequest).execute()
//            if (response.code() == 403) this.cancel("密码错误，或短时间内多次登录失败而被暂时禁止登录").also {
//                return@launch
//            }
//            if (response.code() == 200) {
//                val uuid = response.body()!!.selectedProfile?.id
//                if (uuid == null) {
//                    this.cancel("账号下未绑定角色")
//                    return@launch
//                }
//                val saveImg = "$accountPath/$email/$uuid"
//                val byteArray = getImg(uuid)
//                File(saveImg).write(byteArray)
//
//
//                val bitmap = BitmapFactory.decodeFile(saveImg)
//                val d = 16
//                val underBitmap = Bitmap.createBitmap(d * 8, d * 8, Bitmap.Config.ARGB_8888)
//                val onBitmap = Bitmap.createBitmap(d * 8, d * 8, Bitmap.Config.ARGB_8888)
//
//
//                for (x in 0..7) {
//                    for (y in 0..7) {
//                        for (i in 0 until d) {
//                            for (j in 0 until d) {
//                                underBitmap[(x * d) + j, (y * d) + i] = bitmap[x + 8, y + 8]
//                                onBitmap[(x * d) + j, (y * d + i)] = bitmap[x + 40, y + 8]
//                            }
//                        }
//                    }
//                }
//                File(saveImg).write(underBitmap)
//                File("$saveImg.on").write(onBitmap)
//
//
//                val account = Account(email, saveImg, password, true)
//                val file = File("$accountPath/$email.$fileExtension")
//                FileUtils.writeFile(file, account)
//                return@launch
//            }
//            this.cancel("未知错误")
//            return@launch
//        }
//        while (job.isActive) {
//        }
//        return if (job.isCompleted) LoginStatus.OK
//        else if (job.isCancelled) LoginStatus.ERROR.also {
//            it.msg = job.getCancellationException().message.toString()
//        } else LoginStatus.UNKNOWN
//
//    }
//
//    fun delUser(email: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            refresh()
//            val file = accounts[email]
//            file?.delete()
//            refresh()
//        }
//
//    }
//
//    fun signOut(): LoginStatus {
//        val job = viewModelScope.launch(Dispatchers.IO) {
//            val file = accounts[email.value]!!
//            val account = FileUtils.readFile<Account>(file)
//            FileUtils.writeFile(file, account.also {
//                it.isLogin = false
//            })
//            refresh()
//            Thread.sleep(10L)
//            email.value = ""
//        }
//        while (true) {
//            return if (job.isCompleted) LoginStatus.OK
//            else if (job.isCancelled) LoginStatus.ERROR.also {
//                it.msg = job.getCancellationException().message.toString()
//            }
//            else LoginStatus.UNKNOWN
//        }
//    }
//
//
//    fun refresh() {
//        accountFiles.value = File(accountPath).walk()
//            .maxDepth(1)
//            .filter { it.isFile }
//            .filter { it.extension == "@doctor@" }
//            .toList()
//        accounts.clear()
//        accountFiles.value.forEach {
//            accounts[it.name.replace(".$fileExtension", "")] = it
//        }
//    }
//
//    fun getUserDir(): File = File(accountPath)
//
//
//}

enum class LoginStatus(var msg: String) {
    OK(""),
    ERROR(""),
    UNKNOWN(""),
    WAITING(""),
    LOGGING_IN("")
}







