package ie.setu.volunteerhub.ui.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.volunteerhub.firebase.FirebaseDBManager
import ie.setu.volunteerhub.firebase.UserManager
import ie.setu.volunteerhub.models.DonationModel
import ie.setu.volunteerhub.models.UserModel
import timber.log.Timber

class AboutViewModel : ViewModel() {

    private val user = MutableLiveData<UserModel>()
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var observableData: LiveData<UserModel>
        get() = user
        set(value) {
            user.value = value.value
        }

    init { load() }

    fun load() {
        try {
            //DonationManager.findAll(liveFirebaseUser.value?.email!!, donationsList)
            liveFirebaseUser.value?.let { UserManager.findById(it.uid,user) }
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }
    fun updateProfile(userid: String, userModel: UserModel){

      try {
          //DonationManager.update(email, id, donation)
          UserManager.update(userid, userModel)
          Timber.i("Detail update() Success : ${userModel.name}")
      } catch (e: Exception) {
          Timber.i("Detail update() Error : $e.message")
      }
  }
}