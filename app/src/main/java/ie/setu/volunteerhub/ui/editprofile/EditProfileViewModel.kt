package ie.setu.volunteerhub.ui.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.volunteerhub.firebase.FirebaseDBManager
import ie.setu.volunteerhub.firebase.UserManager
import ie.setu.volunteerhub.models.DonationModel
import ie.setu.volunteerhub.models.UserModel
import timber.log.Timber

class EditProfileViewModel : ViewModel() {

    private val user = MutableLiveData<UserModel>()

    var profiledata: LiveData<UserModel>
        get() = user
        set(value) {
            user.value = value.value
        }

    fun getUser(userid: String) {
        try {
            //DonationManager.findById(email, id, donation)
            UserManager.findById(userid, user)
            Timber.i(
                "Detail getUser() Success : ${
                    user.value.toString()
                }"
            )
        } catch (e: Exception) {
            Timber.i("Detail getUser() Error : $e.message")
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