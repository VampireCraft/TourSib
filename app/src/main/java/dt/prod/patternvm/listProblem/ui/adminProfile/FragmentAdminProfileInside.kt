package dt.prod.patternvm.listProblem.ui.adminProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import dt.prod.patternvm.core.ui.pickers.DatePickerFragment
import dt.prod.patternvm.core.ui.pickers.PickListener
import dt.prod.patternvm.core.ui.pickers.TimePickerFragment
import dt.prod.patternvm.databinding.FragmentInsideListProblemBinding
import dt.prod.patternvm.listProblem.models.ListItemModel
import dt.prod.patternvm.listProblem.models.PlansViewModel
import java.text.SimpleDateFormat
import java.util.*

class FragmentAdminProfileInside : Fragment() {
    companion object {
        fun createInstance(event: ListItemModel): FragmentAdminProfileInside {
            return FragmentAdminProfileInside().apply {
                arguments = Bundle().apply {
                    putSerializable("listItemModel", event)
                }
            }
        }
    }

    lateinit var event: ListItemModel
    private lateinit var binding: FragmentInsideListProblemBinding
    private lateinit var plansViewModel: PlansViewModel

    private var eventDate: Long = 0
    private var eventTime: Long = 0
    private var eventCalendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInsideListProblemBinding.inflate(inflater, container, false)
        plansViewModel = ViewModelProvider(requireActivity())[PlansViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getEventFromArguments()
        configureBackBtn()
        configureBackgroundImage()
        fillEventInfoFields()
        observeOnTextFields()
    }

    private fun configureBackgroundImage() {
        if (event.photo.isNotEmpty())
            loadPhotoFromUrl(binding.ivBackground, event.photo)
    }

    private fun observeOnTextFields() {
        binding.tvDate2.setOnClickListener {
            DatePickerFragment(Calendar.getInstance().timeInMillis, object : PickListener {
                override fun onPicked(calendar: Calendar) {
                    setDateFromCalendar(calendar)
                }
            }).show(parentFragmentManager, FragmentAdminProfileInside::class.simpleName)
        }
        binding.tvTime.setOnClickListener {
            TimePickerFragment(object : PickListener {
                override fun onPicked(calendar: Calendar) {
                    setTimeFromCalendar(calendar)
                }
            }).show(parentFragmentManager, FragmentAdminProfileInside::class.simpleName)
        }
    }

    private fun setDateFromCalendar(calendar: Calendar) {
        eventDate = calendar.timeInMillis
        eventCalendar.set(Calendar.DATE, calendar.get(Calendar.DATE))
        eventCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
        eventCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
        binding.tvDate2.text =
            "${calendar.get(Calendar.DATE)}.${calendar.get(Calendar.MONTH) + 1}.${
                calendar.get(Calendar.YEAR)
            }"
    }

    private fun setTimeFromCalendar(calendar: Calendar) {
        eventTime = calendar.timeInMillis
        eventCalendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY))
        eventCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE))
        binding.tvTime.text =
            "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE) + 1}"
    }

    private fun getEventFromArguments() {
        event = arguments?.getSerializable("listItemModel") as ListItemModel
    }

    private fun fillEventInfoFields() {
        binding.tvTitle.text = event.name
        binding.tvDescription.text = event.description
        binding.tvNumber.text = event.tags
        binding.tvTimeCreate.text = event.timeCreate
    }

    private fun getTimeFromEvent(seconds: Long?): String {
        val date = Date()
        date.time = seconds?.times(1000) ?: 0
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(date)
    }

    private fun getDateFromEvent(seconds: Long?): String {
        val date = Date()
        date.time = seconds?.times(1000) ?: 0
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    private fun loadPhotoFromUrl(imageView: ImageView, photoUrl: String? = "") {
        Glide
            .with(requireContext())
            .load(photoUrl)
            .into(imageView)
    }

    private fun configureBackBtn() {
        binding.ivBtnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}