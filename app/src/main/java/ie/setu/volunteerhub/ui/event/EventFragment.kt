package ie.setu.volunteerhub.ui.event

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentEventBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        loader = createLoader(requireActivity())

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val button = fragBinding.addevent
        button.setOnClickListener {
            // Navigate to the second fragment
            findNavController().navigate(R.id.action_eventFragment_to_addeventFragment)
        }

        eventviewModel.events.observe(viewLifecycleOwner, Observer {
                events ->
            events?.let {
                render(events as ArrayList<EventModel>)
                hideLoader(loader)
            }
        })

    return root
    }

    private fun render(eventlist: ArrayList<EventModel>) {
        fragBinding.recyclerView.adapter = EventAdapter(eventlist)
        if (eventlist.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }


}