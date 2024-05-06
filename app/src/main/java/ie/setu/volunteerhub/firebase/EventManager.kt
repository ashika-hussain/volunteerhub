package ie.setu.volunteerhub.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.setu.volunteerhub.models.DonationModel
import ie.setu.volunteerhub.models.EventModel
import ie.setu.volunteerhub.models.EventStore
import timber.log.Timber

object EventManager : EventStore{
    override fun findAll(events: MutableLiveData<List<EventModel>>) {
        FirebaseDBManager.database.child("events")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Donation error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<EventModel>()
                    val children = snapshot.children
                    children.forEach {
                        val event = it.getValue(EventModel::class.java)
                        localList.add(event!!)
                    }
                    FirebaseDBManager.database.child("events")
                        .removeEventListener(this)

                    events.value = localList
                    Timber.i("localList${events.value}")
                }
            })
    }

    override fun create(userid: String, event: EventModel) {
        Timber.i("Firebase DB Reference : ${FirebaseDBManager.database}")

        val uid = userid
        val key = FirebaseDBManager.database.child("events").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        event.eventid = key
        val eventValues = event.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/events/$key"] = eventValues
        childAdd["/user-events/$uid/$key"] = eventValues

        FirebaseDBManager.database.updateChildren(childAdd)
    }

    override fun delete(userid: String, eventid: String) {
        TODO("Not yet implemented")
    }

    override fun updateevent(userid: String, eventid: String, event: EventModel) {
        TODO("Not yet implemented")
    }

}