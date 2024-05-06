package ie.setu.volunteerhub.ui.about

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import ie.setu.volunteerhub.R
import ie.setu.volunteerhub.databinding.FragmentAboutBinding
import ie.setu.volunteerhub.firebase.FirebaseImageManager
import ie.setu.volunteerhub.ui.auth.LoggedInViewModel
import timber.log.Timber


class AboutFragment : Fragment() {

    private var _fragBinding: FragmentAboutBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var aboutViewModel: AboutViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentAboutBinding.inflate(inflater, container, false)
        val root = fragBinding.root


        aboutViewModel = ViewModelProvider(this)[AboutViewModel::class.java]
        aboutViewModel.observableData.observe(viewLifecycleOwner) { render() }
        val button = fragBinding.editButton
        button.setOnClickListener {
            // Navigate to the second fragment
            findNavController().navigate(R.id.action_aboutFragment_to_editProfileFragment)
        }
        aboutViewModel.liveFirebaseUser.value?.let { imageUpdate(it) }

        return root
    }

    private fun render() {
        fragBinding.uservm = aboutViewModel
        Timber.i("Retrofit fragBinding.uservm == ${fragBinding.uservm}")
    }

    override fun onResume() {
        super.onResume()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                aboutViewModel.liveFirebaseUser.value = firebaseUser
                aboutViewModel.load()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    private fun imageUpdate(currentUser: FirebaseUser){
        Timber.i("Starting image")
        FirebaseImageManager.imageUri.observe(viewLifecycleOwner) { result ->
            if (result == Uri.EMPTY) {
                Timber.i("DX NO Existing imageUri")
                if (currentUser.photoUrl != null) {
                    //if you're a google user
                    FirebaseImageManager.updateUserImage(
                        currentUser.uid,
                        currentUser.photoUrl,
                        fragBinding.profileImg,
                        true
                    )
                } else {
                    Timber.i("DX Loading Existing Default imageUri")
                    FirebaseImageManager.updateDefaultImage(
                        currentUser.uid,
                        R.drawable.ic_launcher_homer,
                        fragBinding.profileImg
                    )
                }
            } else // load existing image from firebase
            {
                Timber.i("DX Loading Existing imageUri")
                FirebaseImageManager.updateUserImage(
                    currentUser.uid,
                    FirebaseImageManager.imageUri.value,
                    fragBinding.profileImg, true
                )
            }
        }
    }


}