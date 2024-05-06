package ie.setu.volunteerhub.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize


    @Parcelize
    data class UserModel(
        var uid: String = "",
        var name: String = "",
        var location: String = "",
        var interests: String = "",
        var education: String = "",
        var email: String = "")
        : Parcelable
    {
        @Exclude
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "uid" to uid,
                "name" to name,
                "location" to location,
                "interests" to interests,
                "education" to education,
                "email" to email
            )
        }
    }