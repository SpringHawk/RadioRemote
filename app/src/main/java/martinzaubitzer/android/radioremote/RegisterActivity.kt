package martinzaubitzer.android.radioremote

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*
import martinzaubitzer.android.radioremote.utils.SharedPrefsUtils

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val sharedPrefsUtils = SharedPrefsUtils(this)

        if (sharedPrefsUtils.getIP()!! != "") {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
            return
        }

        set_ip_btn.setOnClickListener {
            val enteredIp = ip_et.text.toString()
            if (enteredIp == "" || enteredIp == " ") {
                ip_et.error = getString(R.string.err_enter_ip)
            } else {
                var utils = SharedPrefsUtils(this)
                utils.saveIP(enteredIp)
                val intent = Intent(this, FavoritesActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
