package ie.setu.volunteerhub.ui.addevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
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
    private val fragBinding get() = _fragBinding!!
    private lateinit var addEventViewModel: AddEventViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentAddEventBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        addEventViewModel = ViewModelProvider(this)[AddEventViewModel::class.java]

        var selectedDate = ""
        val simpleDatePicker = fragBinding.datePicker as DatePicker
        val today = Calendar.getInstance()
        simpleDatePicker.minDate = today.timeInMillis
        simpleDatePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            val month = month + 1
            selectedDate = "$day/$month/$year"
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


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}