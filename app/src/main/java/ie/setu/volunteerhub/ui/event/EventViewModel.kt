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
import timber.log.Timber

class EventViewModel : ViewModel() {

    private val event = MutableLiveData<List<EventModel>>()
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    val events: LiveData<List<EventModel>>
        get() = event

    init { load() }

    fun load() {
        try {
            //DonationManager.findAll(liveFirebaseUser.value?.email!!, donationsList)
            EventManager.findAll(event)
            Timber.i("Events : ${event.value?.size}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }


}