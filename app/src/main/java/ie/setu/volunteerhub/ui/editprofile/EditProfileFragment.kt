package ie.setu.volunteerhub.ui.editprofile

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
import com.google.firebase.auth.FirebaseUser
import ie.setu.volunteerhub.databinding.FragmentEditProfileBinding
import ie.setu.volunteerhub.ui.auth.LoggedInViewModel
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {

    private var _fragBinding: FragmentEditProfileBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var editProfileViewModel: EditProfileViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        editProfileViewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]

        editProfileViewModel.profiledata.observe(viewLifecycleOwner) { render() }

        fragBinding.saveButton.setOnClickListener {
            val userId = loggedInViewModel.liveFirebaseUser.value?.uid
            val userData = fragBinding.uservm?.profiledata?.value
            Timber.i("${fragBinding.uservm?.profiledata?.value}")
            if (userId != null && userData != null) {
                editProfileViewModel.updateUser(userId, userData)
                findNavController().navigateUp()
            } else {
                Timber.i("Empty value")
            }
        }

        return root
    }

    private fun render() {
        fragBinding.uservm = editProfileViewModel
        Timber.i("Retrofit fragBinding.uservm == ${fragBinding.uservm}")
    }

    override fun onResume() {
        super.onResume()
        editProfileViewModel.getUser(loggedInViewModel.liveFirebaseUser.value?.uid!!)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}