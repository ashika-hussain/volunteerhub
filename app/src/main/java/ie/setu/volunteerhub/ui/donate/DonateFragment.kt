package ie.setu.volunteerhub.ui.donate

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import ie.setu.volunteerhub.R
import ie.setu.volunteerhub.databinding.FragmentDonateBinding
import ie.setu.volunteerhub.main.VolunteerHubApp
import ie.setu.volunteerhub.models.DonationModel
import ie.setu.volunteerhub.ui.auth.LoggedInViewModel
import ie.wit.donationx.ui.report.ReportViewModel
import timber.log.Timber
import java.sql.Time

class DonateFragment : Fragment() {


    var totalDonated = 0
    private var _fragBinding: FragmentDonateBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var donateViewModel: DonateViewModel
    private val reportViewModel: ReportViewModel by activityViewModels()
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentDonateBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        donateViewModel = ViewModelProvider(this).get(DonateViewModel::class.java)
        donateViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let { render(status) }
        })

        fragBinding.progressBar.max = 10000
        setButtonListener(fragBinding)

        return root;
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }

            false -> Toast.makeText(context, getString(R.string.donationError), Toast.LENGTH_LONG)
                .show()
        }
    }

    fun setButtonListener(layout: FragmentDonateBinding) {
        layout.donateButton.setOnClickListener {
            val amount = if (layout.paymentAmount.text.isNotEmpty())
                layout.paymentAmount.text.toString().toInt() else 0
            if (totalDonated >= layout.progressBar.max)
                Toast.makeText(context, "Donate Amount Exceeded!", Toast.LENGTH_LONG).show()
            else {
                val paymentmethod =
                    if (layout.paymentMethod.checkedRadioButtonId == R.id.Direct) "Direct" else "Paypal"
                totalDonated += amount
                layout.totalSoFar.text = String.format(getString(R.string.totalSoFar), totalDonated)
                layout.progressBar.progress = totalDonated
                donateViewModel.addDonation(
                    loggedInViewModel.liveFirebaseUser,
                    DonationModel(
                        paymentmethod = paymentmethod, amount = amount,
                        email = loggedInViewModel.liveFirebaseUser.value?.email!!
                    )
                )
            }
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_donate, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(
                    menuItem,
                    requireView().findNavController()
                )
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        val donationsList = reportViewModel.observableDonationsList.value
        if (donationsList != null) {
            totalDonated = donationsList.sumOf { it.amount }
            fragBinding.progressBar.progress = totalDonated
            fragBinding.totalSoFar.text = String.format(getString(R.string.totalSoFar), totalDonated)
        } else {
            // Handle the case where donationsList is null
            // For example, show a default value or log a message
            Timber.i("Fragment", "Donations list is null")
        }
    }
}