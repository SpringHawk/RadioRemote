package martinzaubitzer.android.radioremote.networking.responseObjects

data class StationStream(
    val stream: String,
    val bitrate: Int,
    val content_type: String
)