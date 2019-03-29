package martinzaubitzer.android.radioremote.utils.data

import android.content.Context
import android.util.Log
import martinzaubitzer.android.radioremote.utils.Constants.Companion.TABLE_NAME
import martinzaubitzer.android.radioremote.utils.Constants.Companion.dateFormatter
import martinzaubitzer.android.radioremote.utils.database
import org.jetbrains.anko.db.*

/**
 * Created by tsantana on 03/03/18.
 */
class NoteRepository(val context: Context) {

    fun findAll(): ArrayList<DatabaseModel> = context.database.use {
        val notes = ArrayList<DatabaseModel>()

        select(TABLE_NAME, "id", "station_id", "station_name", "station_img", "station_url", "creationDate")
            .parseList(object : MapRowParser<List<DatabaseModel>> {
                override fun parseRow(columns: Map<String, Any?>): List<DatabaseModel> {
                    val id = columns.getValue("id")
                    val stationId = columns.getValue("station_id")
                    val stationName = columns.getValue("station_name")
                    val stationImgUrl = columns.getValue("station_img")
                    val stationStreamUrl = columns.getValue("station_url")
                    val creationDate = columns.getValue("creationDate")

                    val note = DatabaseModel(
                        id.toString().toLong(),
                        stationId.toString(),
                        stationName.toString(),
                        stationImgUrl.toString(),
                        stationStreamUrl.toString()
                    )

                    //  note.creationDate = dateFormatter.parse(creationDate.toString())

                    notes.add(note)

                    return notes
                }
            })

        notes
    }

    fun create(note: DatabaseModel) = context.database.use {
        insert(
            TABLE_NAME,
            "station_id" to note.stationId,
            "station_name" to note.name,
            "station_img" to note.imageUrl,
            "station_url" to note.streamUrl,
            "creationDate" to dateFormatter.format(note.creationDate)
        )
    }

    fun update(note: DatabaseModel) = context.database.use {
        val updateResult = update(
            TABLE_NAME,
            "station_id" to note.stationId,
            "station_name" to note.name,
            "station_img" to note.imageUrl,
            "station_url" to note.streamUrl,
            "creationDate" to dateFormatter.format(note.creationDate)
        )
            .whereArgs("station_name = {name}", "name" to note.name)
            .exec()

        Log.d("DEBUG", "Update result code is $updateResult")
    }

    fun delete(note: DatabaseModel) = context.database.use {
        delete(TABLE_NAME, whereClause = "station_name = {name}", args = *arrayOf("name" to note.name))
    }
}