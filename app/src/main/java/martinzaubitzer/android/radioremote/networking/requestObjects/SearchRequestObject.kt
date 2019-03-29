package martinzaubitzer.android.radioremote.networking.requestObjects

class SearchRequestObject internal constructor(
    internal val query: String,
    internal val country: String = "DE",
    internal val per_page: Int = 20
)