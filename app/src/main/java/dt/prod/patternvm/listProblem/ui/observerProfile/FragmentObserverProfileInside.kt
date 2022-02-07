package dt.prod.patternvm.listProblem.ui.observerProfile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import dt.prod.patternvm.R
import java.lang.Exception
import java.time.ZoneId

class FragmentObserverProfileInside : Fragment() {
    companion object {
        fun createInstance(event: ListItemModel): FragmentObserverProfileInside {
            return FragmentObserverProfileInside().apply {
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
        configureSaveBtn()
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
            }).show(parentFragmentManager, FragmentObserverProfileInside::class.simpleName)
        }
        binding.tvTime.setOnClickListener {
            TimePickerFragment(object : PickListener {
                override fun onPicked(calendar: Calendar) {
                    setTimeFromCalendar(calendar)
                }
            }).show(parentFragmentManager, FragmentObserverProfileInside::class.simpleName)
        }
    }

    private fun setDateFromCalendar(calendar: Calendar) {
        eventDate = calendar.timeInMillis
        eventCalendar.set(Calendar.DATE, calendar.get(Calendar.DATE))
        eventCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
        eventCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
        binding.tvDate2.text =
            "${calendar.get(Calendar.YEAR)}.${calendar.get(Calendar.MONTH) + 1}.${calendar.get(Calendar.DATE)}"
        colorTime()
    }

    private fun setTimeFromCalendar(calendar: Calendar) {
        eventTime = calendar.timeInMillis
        eventCalendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY))
        eventCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE))
        binding.tvTime.text =
            "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE) + 1}"
       colorTime()
    }

    private fun getEventFromArguments() {
        event = arguments?.getSerializable("listItemModel") as ListItemModel
    }

    private fun fillEventInfoFields() {
        binding.tvTitle.text = event.name
        binding.tvDescription.text = event.description
        binding.tvNumber.text = event.tags
        binding.tvTimeCreate.text = "Время создания: " + event.timeCreate
        binding.tvTimeAccept.text = "Время принятия: " + event.timeAccept
        binding.tvTimeRemove.text = "Конечный срок: " + event.timeRemove
        binding.tvTimeOver.text = "Время окончания: " + event.timeOver
        binding.tvTypeUser.text = when(event.adress){
            "101" -> "Горничные"
            "102" -> "Хоз участок"
            "51" -> "Горничные"
            "52" -> "Хоз участок"
            else -> "Хто ты?"
        }

        val calendar: Calendar = Calendar.getInstance()
        //Log.e("time in millis current", calendar.timeInMillis.toString())

        if (event.timeOver != "0000-00-00 00:00:00" && event.timeOver.isNotEmpty()){
            binding.tvAlert2.text = "Завершено"
            binding.tvAlert2.background = requireActivity().getDrawable(R.drawable.decoration_rounded_yellow_filled_tag)
            binding.tvTimeAccept.visibility = VISIBLE
            binding.tvTimeRemove.visibility = VISIBLE
            binding.tvTimeOver.visibility = VISIBLE
        } else
            if (event.timeRemove != "0000-00-00 00:00:00" && event.timeRemove.isNotEmpty())
                if (dateInMillis(event.timeRemove) < calendar.timeInMillis) {
                    binding.tvAlert2.text = "Просрочено"
                    binding.tvAlert2.background =
                        requireActivity().getDrawable(R.drawable.decoration_rounded_red_filled)
                    binding.tvTimeAccept.visibility = VISIBLE
                    binding.tvTimeRemove.visibility = VISIBLE
                } else {
                    if (event.timeAccept != "0000-00-00 00:00:00" && event.timeAccept.isNotEmpty()) {
                        binding.tvAlert2.text = "Выполняется"
                        binding.tvAlert2.background =
                            requireActivity().getDrawable(R.drawable.decoration_rounded_blue_filled)
                        binding.tvTimeAccept.visibility = VISIBLE
                        binding.tvTimeRemove.visibility = VISIBLE
                    } else {
                        binding.tvAlert2.text = "Не принято"
                        binding.tvAlert2.background =
                            requireActivity().getDrawable(R.drawable.decoration_rounded_orange_filled)
                    }
                } else {
                if (event.timeAccept != "0000-00-00 00:00:00" && event.timeAccept.isNotEmpty()) {
                    binding.tvAlert2.text = "Выполняется"
                    binding.tvAlert2.background =
                        requireActivity().getDrawable(R.drawable.decoration_rounded_blue_filled)
                    binding.tvTimeAccept.visibility = VISIBLE
                    binding.tvTimeRemove.visibility = VISIBLE
                } else {
                    binding.tvAlert2.text = "Не принято"
                    binding.tvAlert2.background =
                        requireActivity().getDrawable(R.drawable.decoration_rounded_orange_filled)
                }
            }

        colorTime()
    }

    private fun dateInMillis(date: String): Long {
        val calendar: Calendar = Calendar.getInstance()
        val strAllTime = date.split(" ").toTypedArray()
        val strDate = strAllTime[0].split("-").toTypedArray()
        val strTime = strAllTime[1].split(":").toTypedArray()
        calendar.set(Calendar.YEAR,strDate[0].toInt())
        calendar.set(Calendar.MONTH,strDate[1].toInt()-1)
        calendar.set(Calendar.DAY_OF_MONTH,strDate[2].toInt())
        calendar.set(Calendar.HOUR_OF_DAY,strTime[0].toInt())
        calendar.set(Calendar.MINUTE,strTime[1].toInt())
        calendar.set(Calendar.SECOND,strTime[2].toInt())

        //Log.e("date in millis remove", SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(calendar.time))
        //Log.e("time in millis remove", calendar.timeInMillis.toString())
        return calendar.timeInMillis
    }

    private fun colorTime(){
        binding.tvDate2.visibility = GONE
        binding.tvTime.visibility = GONE
        binding.tvDateTitle.visibility = GONE
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
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        return dateFormat.format(date)
    }

    private fun loadPhotoFromUrl(imageView: ImageView, photoUrl: String? = "") {
        Glide
            .with(requireContext())
            .load(photoUrl)
            .into(imageView)
    }

    private fun configureSaveBtn(){
        binding.btnSave.visibility = GONE
    }

    private fun configureBackBtn() {
        binding.ivBtnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}