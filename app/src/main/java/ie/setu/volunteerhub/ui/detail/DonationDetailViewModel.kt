package ie.setu.volunteerhub.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.volunteerhub.models.DonationManager
import ie.setu.volunteerhub.models.DonationModel

class DonationDetailViewModel : ViewModel() {
    private val donation = MutableLiveData<DonationModel>()

    val observableDonation: LiveData<DonationModel>
        get() = donation

    fun getDonation(id: Long) {
        donation.value = DonationManager.findById(id)
    }
}