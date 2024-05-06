package ie.setu.volunteerhub.ui.event

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import ie.setu.volunteerhub.R
import ie.setu.volunteerhub.adapters.DonationAdapter
import ie.setu.volunteerhub.adapters.EventAdapter
import ie.setu.volunteerhub.databinding.FragmentEditProfileBinding
import ie.setu.volunteerhub.databinding.FragmentEventBinding
import ie.setu.volunteerhub.databinding.FragmentReportBinding
import ie.setu.volunteerhub.models.DonationModel
import ie.setu.volunteerhub.models.EventModel
import ie.setu.volunteerhub.ui.auth.LoggedInViewModel
import ie.setu.volunteerhub.ui.editprofile.EditProfileViewModel
import ie.setu.volunteerhub.ui.report.ReportFragmentDirections
import ie.setu.volunteerhub.utils.createLoader
import ie.setu.volunteerhub.utils.hideLoader
import ie.setu.volunteerhub.utils.showLoader
import ie.wit.donationx.ui.report.ReportViewModel
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [EventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventFragment : Fragment() {

    private var _fragBinding: FragmentEventBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var loader : AlertDialog
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    private val eventviewModel: EventViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentEventBinding.inflate(inflater, container, false)
        val root = fragBinding.root

      //  eventviewModel = ViewModelProvider(this).get(EventViewModel::class.java)
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        loader = createLoader(requireActivity())
        eventviewModel.events.observe(viewLifecycleOwner, Observer {
                events ->
            events?.let {
                render(events as ArrayList<EventModel>)
                setupSearchView(events)
                hideLoader(loader)
            }
        })
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                eventviewModel.liveFirebaseUser.value = firebaseUser
                eventviewModel.load()
            }
        })



        val button = fragBinding.addevent
        button.setOnClickListener {
            // Navigate to the second fragment
            findNavController().navigate(R.id.action_eventFragment_to_addeventFragment)
        }

    return root
    }

    private fun render(eventlist: ArrayList<EventModel>) {
        Timber.i("Inside render$eventlist")
        fragBinding.recyclerView.adapter = EventAdapter(eventlist)
        if (eventlist.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.noEvent.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.noEvent.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader,"Downloading Events")

        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                eventviewModel.liveFirebaseUser.value = firebaseUser
                eventviewModel.load()
            }
        })

        hideLoader(loader)
    }

    private fun setupSearchView(eventlist: ArrayList<EventModel>) {
        fragBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    val filteredEvents = eventlist.filter { event ->
                        event.name.contains(newText, ignoreCase = true) ||
                                event.location.contains(newText, ignoreCase = true) ||
                                event.details.contains(newText, ignoreCase = true) ||
                                event.date.contains(newText, ignoreCase = true)
                    }.toMutableList()
                    render(java.util.ArrayList(filteredEvents))
                } else {
                    render(eventlist)
                }
                return true
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }


}