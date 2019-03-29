package martinzaubitzer.android.radioremote.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsUtils(context: Context) {
    private val SHARED_PREFS_NAME = "radioremote.prefs"
    private val IP_NAME = "IP_ADDRESS"

    private var prefs: SharedPreferences

    init {
        prefs = context.getSharedPreferences(SHARED_PREFS_NAME, 0)
    }

    fun saveIP(ip: String) {
        val url = "http://$ip"
        prefs.edit().putString(IP_NAME, url).apply()
    }

    fun getIP(): String? {
        return prefs.getString(IP_NAME, "")
    }
}