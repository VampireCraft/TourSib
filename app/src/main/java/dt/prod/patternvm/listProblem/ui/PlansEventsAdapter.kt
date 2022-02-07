package dt.prod.patternvm.listProblem.ui

import android.app.Activity
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
import dt.prod.patternvm.listProblem.ui.observerProfile.FragmentObserverProfileInside
import java.util.*

class PlansEventsAdapter(
    private val activity: Activity,
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
        holder.tvUser.text = when(events[position].adress){
            "101" -> "Горничные"
            "102" -> "Хоз участок"
            else -> "Хто ты?"
        }

        val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC+3"))

        if (events[position].timeOver != "0000-00-00 00:00:00" && events[position].timeOver.isNotEmpty()){
            holder.tvAlert.text = "Завершено"
            holder.tvAlert.background = activity.getDrawable(R.drawable.decoration_rounded_yellow_filled_tag)
        } else
            if (events[position].timeRemove != "0000-00-00 00:00:00" && events[position].timeRemove.isNotEmpty())
                if (dateInMillis(events[position].timeRemove) < calendar.timeInMillis) {
                    holder.tvAlert.text = "Просрочено"
                    holder.tvAlert.background =
                        activity.getDrawable(R.drawable.decoration_rounded_red_filled)
                } else {
                    if (events[position].timeAccept != "0000-00-00 00:00:00" && events[position].timeAccept.isNotEmpty()) {
                        holder.tvAlert.text = "Выполняется"
                        holder.tvAlert.background =
                            activity.getDrawable(R.drawable.decoration_rounded_blue_filled)
                    } else {
                        holder.tvAlert.text = "Не принято"
                        holder.tvAlert.background =
                            activity.getDrawable(R.drawable.decoration_rounded_orange_filled)
                    }
                } else {
                if (events[position].timeAccept != "0000-00-00 00:00:00" && events[position].timeAccept.isNotEmpty()) {
                    holder.tvAlert.text = "Выполняется"
                    holder.tvAlert.background =
                        activity.getDrawable(R.drawable.decoration_rounded_blue_filled)
                } else {
                    holder.tvAlert.text = "Не принято"
                    holder.tvAlert.background =
                        activity.getDrawable(R.drawable.decoration_rounded_orange_filled)
                }
            }

        setOnEventClick(holder.clRoot, isMyEvent, events[position])
    }

    private fun dateInMillis(date: String): Long {
        val calendar: Calendar = Calendar.getInstance()
        val strAllTime = date.split(" ").toTypedArray()
        val strDate = strAllTime[0].split("-").toTypedArray()
        val strTime = strAllTime[1].split(":").toTypedArray()
        calendar.set(
            strDate[0].toInt(),
            strDate[1].toInt()-1,
            strDate[2].toInt(),
            strTime[0].toInt(),
            strTime[1].toInt(),
            strTime[2].toInt()
        )
        return calendar.timeInMillis
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
            1 -> FragmentObserverProfileInside.createInstance(event)
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
        val tvUser: TextView = binding.tvUser
        val tvAlert: TextView = binding.tvAlert
        val clRoot: ConstraintLayout = binding.clRoot
    }
}