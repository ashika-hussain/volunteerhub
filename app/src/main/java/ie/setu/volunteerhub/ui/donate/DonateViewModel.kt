package ie.setu.volunteerhub.ui.donate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.volunteerhub.firebase.FirebaseDBManager
import ie.setu.volunteerhub.models.DonationModel

class DonateViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()
    val observableStatus: LiveData<Boolean>
        get() = status
    fun addDonation(
        firebaseUser: MutableLiveData<FirebaseUser>,
        donation: DonationModel) {
        status.value = try {
            //DonationManager.create(donation)
            FirebaseDBManager.create(firebaseUser,donation)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }


}