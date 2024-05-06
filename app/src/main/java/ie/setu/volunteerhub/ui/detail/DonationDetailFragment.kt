package ie.setu.volunteerhub.ui.detail

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.setu.volunteerhub.R
import ie.setu.volunteerhub.databinding.FragmentDonationDetailBinding
import ie.setu.volunteerhub.ui.auth.LoggedInViewModel
import ie.wit.donationx.ui.report.ReportViewModel
import timber.log.Timber

class DonationDetailFragment : Fragment() {

    private lateinit var detailViewModel: DonationDetailViewModel
    private val args by navArgs<DonationDetailFragmentArgs>()
    private var _fragBinding: FragmentDonationDetailBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val reportViewModel : ReportViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentDonationDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this)[DonationDetailViewModel::class.java]
        detailViewModel.observableDonation.observe(viewLifecycleOwner, Observer { render() })

        fragBinding.editDonationButton.setOnClickListener {
            detailViewModel.updateDonation(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.donationId, fragBinding.donationvm?.observableDonation!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteDonationButton.setOnClickListener {
            reportViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                detailViewModel.observableDonation.value?.uid!!)
            findNavController().navigateUp()
        }

        return root
    }

    private fun render() {
        fragBinding.editMessage.setText("A Message")
        fragBinding.editUpvotes.setText("0")
        fragBinding.donationvm = detailViewModel
        Timber.i("Retrofit fragBinding.donationvm == $fragBinding.donationvm")
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getDonation(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.donationId)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}