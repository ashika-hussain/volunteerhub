package ie.setu.volunteerhub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.volunteerhub.R
import ie.setu.volunteerhub.databinding.CardDonationBinding
import ie.setu.volunteerhub.models.DonationModel
import ie.setu.volunteerhub.models.DonationStore

interface DonationClickListener {
    fun onDonationClick(donation: DonationModel)
}

class DonationAdapter(private var donations: List<DonationModel>, private val listener: DonationClickListener)
    : RecyclerView.Adapter<DonationAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardDonationBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val donation = donations[holder.adapterPosition]
        holder.bind(donation, listener )
    }

    override fun getItemCount(): Int = donations.size

    inner class MainHolder(val binding : CardDonationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(donation: DonationModel, listener: DonationClickListener) {
            binding.donation = donation
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
            //Include this call to force the bindings to happen immediately
            binding.root.setOnClickListener { listener.onDonationClick(donation) }

            binding.executePendingBindings()

        }
    }
}