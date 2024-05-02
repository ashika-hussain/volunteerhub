package ie.setu.volunteerhub.ui.donate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.volunteerhub.models.DonationManager
import ie.setu.volunteerhub.models.DonationModel

class DonateViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()
    val observableStatus: LiveData<Boolean>
        get() = status
    fun addDonation(donation: DonationModel) {
        status.value = try {
            DonationManager.create(donation)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }


}