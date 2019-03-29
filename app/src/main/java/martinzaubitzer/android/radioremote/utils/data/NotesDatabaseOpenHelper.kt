package martinzaubitzer.android.radioremote.utils.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import martinzaubitzer.android.radioremote.utils.Constants.Companion.DB_NAME
import martinzaubitzer.android.radioremote.utils.Constants.Companion.TABLE_NAME
import org.jetbrains.anko.db.*

class NotesDatabaseOpenHelper(context: Context) : ManagedSQLiteOpenHelper(context, DB_NAME, null, 2) {

    companion object {
        private var instance: NotesDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(context: Context): NotesDatabaseOpenHelper {
            if (instance == null) {
                instance = NotesDatabaseOpenHelper(context.applicationContext)
            }

            return instance!!
        }
    }

    override fun onCreate(database: SQLiteDatabase) {

        database.createTable(
            TABLE_NAME, true,

            "id" to INTEGER + PRIMARY_KEY + UNIQUE,
            "station_id" to TEXT + NOT_NULL,
            "station_name" to TEXT,
            "station_img" to BLOB,
            "station_url" to TEXT + NOT_NULL,
            "creationDate" to TEXT
        )

    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.dropTable(TABLE_NAME, ifExists = true)
    }
}