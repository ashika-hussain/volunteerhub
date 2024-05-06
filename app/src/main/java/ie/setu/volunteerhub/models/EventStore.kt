package ie.setu.volunteerhub.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface EventStore {

    fun findAll(events:
                MutableLiveData<List<EventModel>>
    )
    fun create(userid: String, event: EventModel)
    fun delete(userid:String, eventid: String)
    fun updateevent(userid:String, eventid: String, event: EventModel)
}