package ie.setu.volunteerhub.main

import android.app.Application
import ie.setu.volunteerhub.models.DonationStore
import timber.log.Timber

class VolunteerHubApp : Application() {

   //lateinit var donationsStore: DonationStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //donationsStore = DonationMemStore()
        Timber.i("VolunteerHub Application Started")
    }
}