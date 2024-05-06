package ie.setu.volunteerhub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.volunteerhub.R
import ie.setu.volunteerhub.databinding.ItemEventBinding
import ie.setu.volunteerhub.models.DonationModel
import ie.setu.volunteerhub.models.EventModel
import ie.setu.volunteerhub.utils.customTransformation

class EventAdapter(private val events: List<EventModel>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.editName)
        val detailsTextView: TextView = itemView.findViewById(R.id.Details)
        val locationTextView: TextView = itemView.findViewById(R.id.Location)
        val dateTextView: TextView = itemView.findViewById(R.id.datePicker)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = events[position]
        holder.nameTextView.text = currentEvent.name
        holder.detailsTextView.text = currentEvent.details
        holder.locationTextView.text = currentEvent.location
        holder.dateTextView.text = currentEvent.date.toString()
    }

    override fun getItemCount(): Int {
        return events.size
    }
}