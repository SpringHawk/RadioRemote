package martinzaubitzer.android.radioremote.utils

import java.text.SimpleDateFormat

class Constants {
    companion object {
        const val DB_NAME = "StationsDB"
        const val TABLE_NAME = "StationsTable"
        val dateFormatter = SimpleDateFormat("yyyyMMddHHmmss")
        const val BASE_URL = "http://api.dirble.com/v2/"
    }
}