package top.fanua.rimuruAndroid.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.NotNull

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/7:18:38
 */
@Serializable
data class Account(
    val email: String,
    var imgUrl: String,
    val password: String
)


@Entity(tableName = "SaveAccount")
data class SaveAccount(
    @PrimaryKey val email: String,
    var accessToken: String?,
    var uuid: String?,
    var name: String?,
    var icon: String?,
    var radian: Int = 10,
    var authServer: String,
    var sessionServer: String
)

@Entity(tableName = "Password")
data class Password(
    @PrimaryKey val email: String,
    var password: String
)

data class EmailWithPassword(
    @Embedded val saveAccount: SaveAccount,
    @Relation(
        parentColumn = "email",
        entityColumn = "email"
    )
    val password: Password
)

@Entity(tableName = "Config")
data class Config(
    @PrimaryKey(autoGenerate = true) val uid: Int? = null,
    @NotNull val key: String,
    val value: String
)

@Entity(tableName = "SaveServer")
data class SaveServer(
    @PrimaryKey(autoGenerate = true) val uid: Int? = null,
    @NotNull val email: String,
    var host: String,
    var port: Int,
    var name: String,
    var icon: String
)

@Entity(tableName = "SaveChat")
data class SaveChat(
    @PrimaryKey(autoGenerate = true) val uid: Int? = null,
    @NotNull val ownerId: Int,
    var text: String,
    var time: Long,
    var uuid: String,
    var name: String,
    var icon: String
)

data class ServerWithChats(
    @Embedded val server: SaveServer,
    @Relation(
        parentColumn = "uid",
        entity = SaveChat::class,
        entityColumn = "ownerId"
    )
    val chats: List<SaveChat>
)

@Dao
interface AccountDao {
    @Transaction
    @Query("SELECT * FROM SaveAccount")
    fun getSaveAccount(): Flow<List<EmailWithPassword>>

//    @Transaction
//    @Query("SELECT * FROM SaveAccount WHERE email = :email")
//    fun getSaveAccount(email: String): EmailWithPassword

    @Query("SELECT * FROM SaveServer WHERE email = :email")
    fun getSaveServers(email: String): Flow<List<SaveServer>>

    @Transaction
    @Query("SELECT * FROM SaveServer WHERE email = :email AND name = :name")
    fun getSaveChats(email: String, name: String): Flow<ServerWithChats>

    @Query("SELECT * FROM Config WHERE `key` = :key")
    fun getConfig(key: String): Flow<Config>

    @Insert
    fun insertSaveAccount(saveAccount: SaveAccount, password: Password)

    @Insert
    fun insertSaveServer(server: SaveServer)

    @Insert
    fun insertSaveChats(vararg saveChat: SaveChat)

    @Insert
    fun insertConfig(config: Config)

    @Update
    fun updateSaveAccount(saveAccount: SaveAccount)

    @Update
    fun updatePassword(password: Password)

    @Update
    fun updateConfig(config: Config)

    @Delete
    fun delSaveServer(server: SaveServer)

    @Delete
    fun delConfig(config: Config)

    @Delete
    fun delSaveAccount(saveAccount: SaveAccount)
}

@Database(
    entities = [SaveAccount::class,
        Config::class,
        SaveServer::class,
        SaveChat::class,
        Password::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}

