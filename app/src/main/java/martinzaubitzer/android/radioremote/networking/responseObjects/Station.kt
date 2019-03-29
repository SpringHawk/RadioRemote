package martinzaubitzer.android.radioremote.networking.responseObjects

data class Station(
    val id: String,
    val name: String,
    val image: ImgObject,
    val streams: List<StationStream>
)