package martinzaubitzer.android.radioremote.utils.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class DatabaseModel(
    val id: Long, val stationId: String,
    val name: String, val imageUrl: String,
    val streamUrl: String
) : Parcelable {
    var creationDate: Date = Date()
}