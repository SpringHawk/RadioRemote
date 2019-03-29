package martinzaubitzer.android.radioremote.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_element.view.*
import martinzaubitzer.android.radioremote.R
import martinzaubitzer.android.radioremote.networking.responseObjects.Station
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL


class MyAdapter(

    private val stationList: ArrayList<Station>, private val listener: Listener
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    interface Listener {
        fun onItemClick(station: Station)
        fun onItemFavorite(station: Station)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stationList[position], listener, position)
    }

    override fun getItemCount(): Int = stationList.count()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_element, parent, false)
        return ViewHolder(view)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(station: Station, listener: Listener, position: Int) {
            itemView.setOnClickListener { listener.onItemClick(station) }
            itemView.station_name.text = station.name
            if (station.image.url != null) {
                loadBitmap(URL(station.image.url))
            }
            itemView.fav_btn.setOnClickListener { listener.onItemFavorite(station) }
        }

        fun loadBitmap(url: URL) {
            doAsync {
                var bitmap: Bitmap? = null
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                uiThread {
                    //Update the UI thread here
                    itemView.station_img.setImageBitmap(bitmap)
                }
            }
        }
    }
}