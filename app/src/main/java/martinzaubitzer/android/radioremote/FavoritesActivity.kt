package martinzaubitzer.android.radioremote

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_favorites.*
import martinzaubitzer.android.radioremote.utils.NotesListAdapter
import martinzaubitzer.android.radioremote.utils.SharedPrefsUtils
import martinzaubitzer.android.radioremote.utils.data.DatabaseModel
import martinzaubitzer.android.radioremote.utils.data.NoteRepository


class FavoritesActivity : AppCompatActivity() {

    var localIp: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        loadNotes()

        val utils = SharedPrefsUtils(this)
        localIp = utils.getIP()!!

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            val intent = Intent(this, AllStationsActivity::class.java)
            startActivity(intent)
        }

        listNotes.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val entry = parent.adapter.getItem(position) as DatabaseModel
            sendInstantPlay(entry.streamUrl)
        }

        // Create the Handler object (on the main thread by default)
        val handler = Handler()
        // Define the code block to be executed
        val runnableCode = object : Runnable {
            override fun run() {

                sendInfoRequest()

                // Repeat this the same runnable code block again another 2 seconds
                handler.postDelayed(this, 90000)
            }
        }
        // Start the initial runnable task by posting through the handler
        handler.post(runnableCode)
    }

    fun sendInstantPlay(stream: String) {
        val queue = Volley.newRequestQueue(this)
        val uri = Uri.encode("\"" + stream + "\"")
        val url = "$localIp/?instant=$uri"

        Log.e("URL", url)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                Log.e("RESPONSE", response)
            },
            Response.ErrorListener {
                Log.e("SomeError", "Error")
            })
        queue.add(stringRequest)
        Handler().postDelayed({
            sendInfoRequest()
        }, 5000)
    }

    fun sendInfoRequest() {
        val queue = Volley.newRequestQueue(this)
        val url = "$localIp/?infos"

        Log.e("URL", url)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                Log.e("RESPONSE", response)
                val splits: List<String> = response.split("\n")
                this.title = splits[3].substring(5)
            },
            Response.ErrorListener {
                Log.e("SomeError", "Error")
            })
        queue.add(stringRequest)
    }

    private fun loadNotes() {
        var notes = NoteRepository(this).findAll()
        val notesListAdapter = NotesListAdapter(this, notes)
        listNotes.adapter = notesListAdapter
        notesListAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

}
