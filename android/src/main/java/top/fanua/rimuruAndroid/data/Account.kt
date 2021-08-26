package top.fanua.rimuruAndroid.data

import androidx.room.*
import androidx.room.RoomMasterTable.TABLE_NAME
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.NotNull
import top.fanua.doctor.protocol.definition.play.client.GameMode

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
) {
    fun toRole(): Role {
        return Role(uuid.orEmpty(), name.orEmpty(), icon.orEmpty())
    }
}

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
    var icon: String,
    var isLogin: Boolean,
    var online: Int
)

@Entity(tableName = "SaveAuth")
data class SaveAuth(
    @PrimaryKey(autoGenerate = true) val uid: Long? = null,
    val authServer: String,
    val sessionServer: String,
    val name: String
)

@Entity(tableName = "SaveChat")
data class SaveChat(
    @PrimaryKey(autoGenerate = true) val uid: Long? = null,
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

    @Transaction
    @Query("SELECT * FROM SaveAccount WHERE authServer = :authServer")
    fun getSaveAccount(authServer: String): Flow<List<EmailWithPassword>>

    @Query("SELECT * FROM SaveServer WHERE email = :email")
    fun getSaveServers(email: String): Flow<List<SaveServer>>

    @Transaction
    @Query("SELECT * FROM SaveServer WHERE email = :email AND name = :name")
    fun getSaveChats(email: String, name: String): Flow<ServerWithChats>

    @Query("SELECT * FROM Config WHERE `key` = :key")
    fun getConfig(key: String): Flow<Config>

    @Query("SELECT * FROM SaveAuth")
    fun getSaveAuth(): Flow<List<SaveAuth>>

    @Insert
    fun insertSaveAccount(saveAccount: SaveAccount, password: Password)

    @Insert
    fun insertSaveServer(server: SaveServer)

    @Insert
    fun insertSaveAuth(saveAuth: SaveAuth)

    @Insert
    fun insertSaveChats(saveChat: SaveChat): Long

    @Insert
    fun insertConfig(config: Config)

    @Update
    fun updateSaveAccount(saveAccount: SaveAccount)

    @Update
    fun updatePassword(password: Password)

    @Update
    fun updateConfig(vararg config: Config)

    @Update
    fun updateSaveServer(server: SaveServer)

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
        Password::class,
        SaveAuth::class],
    version = 8
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE SaveServer ADD COLUMN isLogin INTEGER NOT NULL DEFAULT(0)")
    }
}
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE SaveServer ADD COLUMN online INTEGER NOT NULL DEFAULT(0)")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE Online (uuid TEXT NOT NULL, ownerId INTEGER NOT NULL, name TEXT NOT NULL, gameMode INTEGER NOT NULL, PRIMARY KEY(uuid))")
    }
}
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Online ADD COLUMN icon TEXT NOT NULL DEFAULT('')")
    }
}
val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE line (`uid` INTEGER PRIMARY KEY AUTOINCREMENT, `uuid` TEXT NOT NULL, `ownerId` INTEGER NOT NULL, `name` TEXT NOT NULL, `gameMode` INTEGER NOT NULL, `icon` TEXT NOT NULL)")
        database.execSQL("INSERT INTO line(uid, uuid, ownerId, name, gameMode, icon) SELECT NULL, uuid, ownerId, name, gameMode, icon FROM Online")
        database.execSQL("DROP TABLE Online")
        database.execSQL("ALTER TABLE line RENAME TO Online")
    }
}
val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE Online")
    }
}
val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE SaveAuth (uid INTEGER PRIMARY KEY AUTOINCREMENT, authServer TEXT NOT NULL, sessionServer TEXT NOT NULL, name TEXT NOT NULL)")
    }
}
