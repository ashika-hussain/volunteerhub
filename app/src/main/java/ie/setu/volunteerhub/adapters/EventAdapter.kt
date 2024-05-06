package ie.setu.volunteerhub.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ie.setu.volunteerhub.R
import ie.setu.volunteerhub.models.EventModel
import timber.log.Timber

class EventAdapter(private var events: List<EventModel>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.event_name)
        val locationTextView: TextView = itemView.findViewById(R.id.event_location)
        val detailsTextView: TextView = itemView.findViewById(R.id.event_details)
        val dateTextView: TextView = itemView.findViewById(R.id.event_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        Timber.i("In adapter $events")
        val currentEvent = events[position]
        holder.nameTextView.text = currentEvent.name
        holder.detailsTextView.text = currentEvent.details
        holder.locationTextView.text = currentEvent.location
        holder.dateTextView.text=currentEvent.date
    }

    fun updateEvents(filteredEvents: List<EventModel>) {
        events = filteredEvents
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return events.size
    }
}
