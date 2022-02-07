package dt.prod.patternvm.listProblem.ui.adminProfile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
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
import dt.prod.patternvm.core.model.Status
import java.lang.Exception

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
        Log.e("time milis", eventCalendar.timeInMillis.toString())
    }

    private fun getEventFromArguments() {
        event = arguments?.getSerializable("listItemModel") as ListItemModel
    }

    private fun fillEventInfoFields() {
        binding.tvTitle.text = event.name
        binding.tvDescription.text = event.description
        binding.tvNumber.text = event.tags
        binding.tvTimeCreate.text = "Время создания: " + event.timeCreate
        binding.tvTypeUser.text = when(event.adress){
            "101" -> "Горничные"
            "102" -> "Хоз участок"
            else -> "Хто ты?"
        }

        val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC+3"))

        if (event.timeOver != "0000-00-00 00:00:00" && event.timeOver.isNotEmpty()){
            binding.tvAlert2.text = "Завершено"
            binding.tvAlert2.background = requireActivity().getDrawable(R.drawable.decoration_rounded_yellow_filled_tag)
        } else
            if (event.timeRemove != "0000-00-00 00:00:00" && event.timeRemove.isNotEmpty())
                if (dateInMillis(event.timeRemove) < calendar.timeInMillis) {
                    binding.tvAlert2.text = "Просрочено"
                    binding.tvAlert2.background =
                        requireActivity().getDrawable(R.drawable.decoration_rounded_red_filled)
                } else {
                    if (event.timeAccept != "0000-00-00 00:00:00" && event.timeAccept.isNotEmpty()) {
                        binding.tvAlert2.text = "Выполняется"
                        binding.tvAlert2.background =
                            requireActivity().getDrawable(R.drawable.decoration_rounded_blue_filled)
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
                } else {
                    binding.tvAlert2.text = "Не принято"
                    binding.tvAlert2.background =
                        requireActivity().getDrawable(R.drawable.decoration_rounded_orange_filled)
                }
            }
        
        if (event.timeRemove != "0000-00-00 00:00:00"){
            val strTime = event.timeRemove.split(" ").toTypedArray()
            binding.tvDate2.text = strTime[0]
            try {
            binding.tvTime.text = strTime[1]
            } catch (e: Exception){}
            colorTime()
        }
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

    private fun colorTime(){
        binding.tvDate2.setTextColor(resources.getColor(R.color.bright_turquoise))
        binding.tvTime.setTextColor(resources.getColor(R.color.bright_turquoise))
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

    private fun configureSaveBtn() {
        if (event.timeRemove == "0000-00-00 00:00:00") {
            binding.btnSave.text = "Принять к исполнению"
            binding.btnSave.setOnClickListener {
                binding.btnSave.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.btn_click
                    )
                )
                if (binding.tvDate2.text.toString() != "гггг.мм.дд" && binding.tvTime.text.toString() != "00:00") {
                    plansViewModel.listItemModel = event
                    plansViewModel.listItemModel.timeRemove =
                        binding.tvDate2.text.toString() + " " + binding.tvTime.text
                    plansViewModel.listItemModel.timeAccept =
                        SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(
                            Calendar.getInstance().time
                        )
                    plansViewModel.editProblem()
                } else {
                    Toast.makeText(requireActivity(), "Укажите дату и время", Toast.LENGTH_LONG)
                        .show()
                }
            }
        } else {
            binding.btnSave.text = "Завершить"
            binding.btnSave.setOnClickListener {
                binding.btnSave.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.btn_click
                    )
                )
                plansViewModel.listItemModel = event
                plansViewModel.listItemModel.timeOver =
                    SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(
                        Calendar.getInstance().time
                    )
                plansViewModel.listItemModel.adress = when(plansViewModel.listItemModel.adress){
                    "101" -> "51"
                    "102" -> "52"
                    else -> "50"
                }
                plansViewModel.editProblem()

            }
        }
        plansViewModel.acceptedEvents.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    Log.d("myEvents", "LOADING")
                }

                Status.SUCCESS -> {
                    Log.d("myEvents", "SUCCESS" + it.data)
                    Toast.makeText(
                        requireActivity(),
                        "Успешно завершено",
                        Toast.LENGTH_LONG
                    ).show()
                    plansViewModel.getListProblem()
                }
                Status.ERROR -> {
                    Log.d("myEvents", "ERROR" + it.error)
                }
            }
        })
    }

    private fun configureBackBtn() {
        binding.ivBtnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}