package top.fanua.rimuruAndroid.utils

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import top.fanua.rimuruAndroid.data.*


/**
 *
 * @author Doctor_Yin
 * @since 2021/8/8:0:23
 */
interface HttpClient {
    @POST("authserver/authenticate")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("authserver/refresh")
    fun refresh(@Body refreshRequest: RefreshRequest): Call<RefreshResponse>

    @POST("authserver/validate")
    fun validate(@Body tokenRequest: TokenRequest): Call<Void>

    @POST("authserver/invalidate")
    fun invalidate(@Body tokenRequest: TokenRequest): Call<Void>

    @POST("authserver/signout")
    fun signOut(@Body signOutRequest: SignOutRequest): Call<Void>

    @GET("sessionserver/session/minecraft/profile/{uuid}")
    fun roleProperties(@Path("uuid") uuid: String, @Query("unsigned") unsigned: Boolean): Call<UserProfile>

    @GET("sessionserver/session/minecraft/profile/{uuid}")
    fun roleProperties(@Path("uuid") uuid: String): Call<UserProfile>

    @GET
    fun downloadImage(@Url fileUrl: String): Call<ResponseBody>
}

