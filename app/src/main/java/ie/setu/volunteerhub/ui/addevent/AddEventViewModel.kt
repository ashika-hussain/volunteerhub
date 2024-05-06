package ie.setu.volunteerhub.ui.addevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.volunteerhub.firebase.EventManager
import ie.setu.volunteerhub.firebase.FirebaseDBManager
import ie.setu.volunteerhub.firebase.UserManager
import ie.setu.volunteerhub.models.DonationModel
import ie.setu.volunteerhub.models.EventModel
import ie.setu.volunteerhub.models.UserModel
import timber.log.Timber

class AddEventViewModel : ViewModel() {


    fun addEvent(userid: String,
        event: EventModel
    ) {
      try {
            EventManager.create(userid,event)

        } catch (_: IllegalArgumentException) {
        }
    }
    fun updateUser(userid: String, user: UserModel) {
        try {
            //DonationManager.update(email, id, donation)
            UserManager.update(userid, user)
            Timber.i("Detail update() Success : $user")
        } catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }

}