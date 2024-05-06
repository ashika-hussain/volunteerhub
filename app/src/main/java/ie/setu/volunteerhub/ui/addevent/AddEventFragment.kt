package ie.setu.volunteerhub.ui.addevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import ie.setu.volunteerhub.databinding.FragmentAddEventBinding
import ie.setu.volunteerhub.models.EventModel
import ie.setu.volunteerhub.ui.auth.LoggedInViewModel
import timber.log.Timber
import java.util.Calendar


/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEventFragment : Fragment() {

    private var _fragBinding: FragmentAddEventBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var addEventViewModel: AddEventViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    private var datePicker: DatePicker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentAddEventBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        datePicker = fragBinding.datePicker

        var selectedDate = ""

        addEventViewModel = ViewModelProvider(this)[AddEventViewModel::class.java]
        datePicker?.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            // Format the selected date into a string
            selectedDate = formatDate(year, monthOfYear, dayOfMonth)
            // Do something with the selected date
            Timber.i("SelectedDate", selectedDate)
        }
        fragBinding.saveButton.setOnClickListener {
            val userId = loggedInViewModel.liveFirebaseUser.value?.uid
            Timber.i(userId)
            if (userId != null) {
                addEventViewModel.addEvent(userId,
                    EventModel(
                        name= fragBinding.editName.text.toString(),
                        location = fragBinding.Location.text.toString(),
                        date = selectedDate,
                        details = fragBinding.Details.text.toString()
                    )
                    )
                findNavController().navigateUp()
            } else {
                Timber.e("Empty value")
            }
        }

        return root
    }


    override fun onResume() {
        super.onResume()
       // addEventViewModel.getUser(loggedInViewModel.liveFirebaseUser.value?.uid!!)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
    private fun formatDate(year: Int, month: Int, day: Int): String {
        return "$year-${month + 1}-$day" // Adjust month + 1 because months are 0-indexed
    }

}