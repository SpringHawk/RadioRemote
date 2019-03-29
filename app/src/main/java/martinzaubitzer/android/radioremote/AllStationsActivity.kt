package martinzaubitzer.android.radioremote

import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_all_stations.*
import martinzaubitzer.android.radioremote.networking.SearchRequest
import martinzaubitzer.android.radioremote.networking.requestObjects.SearchRequestObject
import martinzaubitzer.android.radioremote.networking.responseObjects.Station
import martinzaubitzer.android.radioremote.utils.Constants.Companion.BASE_URL
import martinzaubitzer.android.radioremote.utils.MyAdapter
import martinzaubitzer.android.radioremote.utils.SharedPrefsUtils
import martinzaubitzer.android.radioremote.utils.data.DatabaseModel
import martinzaubitzer.android.radioremote.utils.data.NoteRepository
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class AllStationsActivity : AppCompatActivity(), MyAdapter.Listener {

    private var myAdapter: MyAdapter? = null
    private var myCompositeDisposable: CompositeDisposable? = null
    private var myRetroCryptoArrayList: ArrayList<Station>? = null

    var localIp: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_stations)

        val searchField: EditText = search_et
        searchField.background.setColorFilter(resources.getColor(R.color.colorLight), PorterDuff.Mode.SRC_IN)

        myCompositeDisposable = CompositeDisposable()
        initRecyclerView()
        // loadData()

        val utils = SharedPrefsUtils(this)
        localIp = utils.getIP()!!

        search_btn.setOnClickListener {
            run {
                val searchText = search_et.text
                searchStation(searchText.toString())
            }
        }
    }

    private fun initRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycler.layoutManager = layoutManager

    }

    private fun searchStation(searchText: String) {
        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(SearchRequest::class.java)
        myCompositeDisposable?.add(
            requestInterface.getSearchResult(SearchRequestObject(searchText))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse)
        )
    }

    private fun handleResponse(stationList: List<Station>) {
        myRetroCryptoArrayList = ArrayList(stationList)
        myAdapter = MyAdapter(myRetroCryptoArrayList!!, this)
        recycler.adapter = myAdapter
    }

    override fun onItemClick(station: Station) {
        // Instant play this station when clicked
        sendGet(station.streams[0].stream)
    }

    fun sendGet(stream: String) {
        val queue = Volley.newRequestQueue(this)
        val uri = Uri.encode("\"" + stream + "\"")
        val url = "$localIp/command?instant=$uri"

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
    }

    override fun onItemFavorite(station: Station) {
        Log.e("Favorite", "This is favorite now")
        createEntry(station)
    }

    private fun createEntry(station: Station) {
        val newNote = DatabaseModel(0, station.id, station.name, station.image.url, station.streams[0].stream)
        NoteRepository(this).create(newNote)
    }

    override fun onDestroy() {
        super.onDestroy()
        myCompositeDisposable?.clear()

    }
}
