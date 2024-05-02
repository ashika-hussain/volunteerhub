package ie.setu.volunteerhub.ui.detail

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import ie.setu.volunteerhub.R
import ie.setu.volunteerhub.databinding.FragmentDonationDetailBinding

class DonationDetailFragment : Fragment() {

    companion object {
        fun newInstance() = DonationDetailFragment()
    }

    private val args by navArgs<DonationDetailFragmentArgs>()
    private lateinit var detailViewModel: DonationDetailViewModel
    private var _fragBinding: FragmentDonationDetailBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentDonationDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this).get(DonationDetailViewModel::class.java)
        detailViewModel.observableDonation.observe(viewLifecycleOwner, Observer { render() })
        return root
    }

    private fun render(/*donation: DonationModel*/) {
        // fragBinding.editAmount.setText(donation.amount.toString())
        // fragBinding.editPaymenttype.text = donation.paymentmethod
        fragBinding.editMessage.setText("A Message")
        fragBinding.editUpvotes.setText("0")
        fragBinding.donationvm = detailViewModel
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getDonation(args.donationId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}