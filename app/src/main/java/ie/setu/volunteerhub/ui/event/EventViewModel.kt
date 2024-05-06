package ie.setu.volunteerhub.ui.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.volunteerhub.firebase.EventManager
import ie.setu.volunteerhub.firebase.FirebaseDBManager
import ie.setu.volunteerhub.models.DonationModel
import ie.setu.volunteerhub.models.EventModel
import ie.setu.volunteerhub.models.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class EventViewModel : ViewModel() {

    private val eventList = MutableLiveData<List<EventModel>>()
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    val events: LiveData<List<EventModel>>
        get() = eventList

    init { load() }

    fun load() {
        // Fetch events from Firebase
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Call Firebase operation asynchronously
                EventManager.findAll(eventList)
                // Update eventList LiveData on the main thread
                Timber.i("Events loaded: ${eventList.value}")
            } catch (e: Exception) {
                Timber.e("Error loading events: ${e.message}")
            }
        }
    }


}