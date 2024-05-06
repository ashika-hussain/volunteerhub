package ie.setu.volunteerhub.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class EventModel(
    var eventid: String = "",
    var name: String = "",
    var location: String = "",
    var date: String = "",
    var details: String =""
)
    : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "eventid" to eventid,
            "name" to name,
            "location" to location,
            "date" to date,
            "details" to details
        )
    }
}