package ie.setu.volunteerhub.ui.report

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.setu.volunteerhub.R
import ie.setu.volunteerhub.adapters.DonationAdapter
import ie.setu.volunteerhub.adapters.DonationClickListener
import ie.setu.volunteerhub.main.VolunteerHubApp
import ie.setu.volunteerhub.databinding.FragmentReportBinding
import ie.setu.volunteerhub.models.DonationModel
import ie.wit.donationx.ui.report.ReportViewModel

class ReportFragment<FloatingActionButton> : Fragment() , DonationClickListener {

    lateinit var app: VolunteerHubApp
    private var _fragBinding: FragmentReportBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var reportViewModel: ReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentReportBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        reportViewModel = ViewModelProvider(this).get(ReportViewModel::class.java)
        reportViewModel.observableDonationsList.observe(viewLifecycleOwner, Observer {
                donations ->
            donations?.let { render(donations) }
        })

        val fab: com.google.android.material.floatingactionbutton.FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = ReportFragmentDirections.actionReportFragmentToDonateFragment()
            findNavController().navigate(action)
        }
        return root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_report, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }     }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(donationsList: List<DonationModel>) {
        fragBinding.recyclerView.adapter = DonationAdapter(donationsList,this)
        if (donationsList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
           fragBinding.donationsNotFound.visibility = View.VISIBLE

        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.donationsNotFound.visibility = View.GONE
        }
    }

    override fun onDonationClick(donation: DonationModel) {
        val action = ReportFragmentDirections.actionReportFragmentToDonationDetailFragment(donation.id)
        findNavController().navigate(action)
    }
    override fun onResume() {
        super.onResume()
        reportViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}