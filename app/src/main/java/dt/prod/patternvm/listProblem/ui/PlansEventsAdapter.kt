package dt.prod.patternvm.listProblem.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import dt.prod.patternvm.listProblem.models.ListItemModel
import dt.prod.patternvm.R
import dt.prod.patternvm.databinding.ItemListProblemBinding
import dt.prod.patternvm.listProblem.ui.adminProfile.FragmentAdminProfileInside

class PlansEventsAdapter(
    private val fragmentManager: FragmentManager,
    val events: List<ListItemModel>,
    val isMyEvent: Int
) :
    RecyclerView.Adapter<PlansEventsAdapter.PlansEventViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlansEventViewHolder {
        return PlansEventViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_problem, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlansEventViewHolder, position: Int) {
        holder.tvName.text = events[position].name
        holder.tvNumber2.text = events[position].tags
        holder.tvTime.text = events[position].timeCreate

        setOnEventClick(holder.clRoot, isMyEvent, events[position])
    }

    private fun setOnEventClick(
        holdersRoot: ConstraintLayout,
        isMyEvent: Int,
        event: ListItemModel
    ) {
        holdersRoot.setOnClickListener {
            openEventInside(isMyEvent, event)
        }
    }

    private fun openEventInside(isMyEvent: Int, event: ListItemModel) {
        val fragment: Fragment = when (isMyEvent){
            0 -> FragmentAdminProfileInside.createInstance(event)
           // 1 -> FragmentEventWhereIGoInside.createInstance(event)
            //2 -> FragmentMyHistoryInside.createInstance(event)
            //3 -> FragmentHistoryWhereIGoInside.createInstance(event)
            else -> FragmentAdminProfileInside.createInstance(event) //element 0
        }

        fragmentManager
            .beginTransaction()
            .replace(R.id.flEventsRoot, fragment)
            .addToBackStack(fragment::class.simpleName)
            .commit()
    }

    override fun getItemCount(): Int {
        return events.size
    }

    inner class PlansEventViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding = ItemListProblemBinding.bind(itemView)
        val tvNumber2: TextView = binding.tvNumber2
        val tvName: TextView = binding.tvName
        val tvTime: TextView = binding.tvTime
        val clRoot: ConstraintLayout = binding.clRoot
    }
}