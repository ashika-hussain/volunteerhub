package ie.wit.donationx.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.volunteerhub.firebase.FirebaseDBManager
import ie.setu.volunteerhub.models.DonationModel
import timber.log.Timber


class ReportViewModel : ViewModel() {

    private val donationsList =
        MutableLiveData<List<DonationModel>>()

    val observableDonationsList: LiveData<List<DonationModel>>
        get() = donationsList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    var readOnly = MutableLiveData(false)

    init { load() }

    fun load() {
        try {
            //DonationManager.findAll(liveFirebaseUser.value?.email!!, donationsList)
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!,donationsList)
            Timber.i("Report Load Success : ${donationsList.value.toString()}")
            readOnly.value = false
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            //DonationManager.delete(userid,id)
            FirebaseDBManager.delete(userid,id)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(donationsList)
            Timber.i("Report LoadAll Success : ${donationsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }
}

