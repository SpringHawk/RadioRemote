package martinzaubitzer.android.radioremote.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import martinzaubitzer.android.radioremote.R
import martinzaubitzer.android.radioremote.utils.data.DatabaseModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

/**
 * Created by tsantana on 03/03/18.
 */
class NotesListAdapter(private var activity: Activity, private var values: ArrayList<DatabaseModel>) : BaseAdapter() {

    private class ViewHolder(row: View?) {
        var stationTitle: TextView? = null
        var favBtn: ImageButton? = null
        var img: ImageView? = null

        init {
            this.stationTitle = row?.findViewById(R.id.station_name)
            this.favBtn = row?.findViewById(R.id.fav_btn)
            this.img = row?.findViewById(R.id.station_img)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View?
        val viewHolder: ViewHolder

        if (convertView == null) {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.row_element, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val note = values[position]
        viewHolder.stationTitle?.text = note.name
        viewHolder.favBtn?.visibility = View.GONE
        try {
            loadBitmap(URL(note.imageUrl), viewHolder)
        } catch (err: Exception) {
        }
        return view as View

    }

    override fun getItem(position: Int): DatabaseModel {
        return values[position]
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }

    override fun getCount(): Int {
        return values.size
    }

    private fun loadBitmap(url: URL, viewHolder: ViewHolder) {
        doAsync {
            var bitmap: Bitmap? = null
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            uiThread {
                //Update the UI thread here
                viewHolder.img?.setImageBitmap(bitmap)
            }
        }
    }
}