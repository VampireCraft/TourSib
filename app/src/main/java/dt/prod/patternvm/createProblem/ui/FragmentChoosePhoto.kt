package dt.prod.patternvm.createProblem.ui

import android.graphics.*
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import dt.prod.patternvm.core.ui.BaseFragment
import dt.prod.patternvm.createProblem.models.CreateProblemViewModel
import dt.prod.patternvm.R
import dt.prod.patternvm.core.ui.TagView
import dt.prod.patternvm.databinding.FragmentCreateDescriptionBinding
import java.io.ByteArrayOutputStream

private const val ARG_TYPE_CREATE_EVENT = "ARG_TYPE_CREATE_EVENT"
private const val ARG_TYPE_PHOTO_PATH = "ARG_TYPE_PHOTO_PATH"

class FragmentChooseColor : BaseFragment() {
    private lateinit var binding: FragmentCreateDescriptionBinding
    private lateinit var viewModel: CreateProblemViewModel
    private lateinit var typeCreateEvent: CreateProblemTypeEnum
    private lateinit var photoPath: String

    fun newInstance(typeCreateEvent: CreateProblemTypeEnum, absolutePath: String): FragmentChooseColor {
        val fragment = FragmentChooseColor()
        val args = Bundle()
        args.putSerializable(ARG_TYPE_CREATE_EVENT, typeCreateEvent)
        args.putString(ARG_TYPE_PHOTO_PATH, absolutePath)
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        typeCreateEvent = arguments?.getSerializable(ARG_TYPE_CREATE_EVENT) as CreateProblemTypeEnum
        photoPath = arguments?.getString(ARG_TYPE_PHOTO_PATH).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateDescriptionBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[CreateProblemViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureBtnNext()
        configureBackBtn()

        when (typeCreateEvent) {
            CreateProblemTypeEnum.EVENT_CAMERA -> {
                viewModel.eventCreationRequestPhoto.name = getRandomString(10)
                viewModel.eventCreationRequestPhoto.photo = prepareFilePart(photoPath)
                viewModel.sendPhoto()
            }
            CreateProblemTypeEnum.EVENT_GALLERY -> {
                viewModel.eventCreationRequestPhoto.name = getRandomString(10)
                viewModel.eventCreationRequestPhoto.photo = prepareFilePart(photoPath)
                viewModel.sendPhoto()
            }
        }


        viewModel.sendPhotoResponse.observe(viewLifecycleOwner, {
            viewModel.eventCreationRequest.photo = it.data?.url.toString()
            Log.e("RequestError", it.error.toString())
            Log.d("Request",it.data?.url.toString())
        })
        observeOnTextFields()
    }

    private fun observeOnTextFields() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    viewModel.eventCreationRequest.name = s.toString()
                } catch (e: Exception) {
                    showError("Неверный формат данных")
                }
            }

        })

        binding.etDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    viewModel.eventCreationRequest.description = s.toString()
                } catch (e: Exception) {
                    showError("Неверный формат данных")
                }
            }
        })


        binding.atvTags.setOnClickListener{
            showBottomSheetDialog()
        }
    }

    private fun showBottomSheetDialog(){
        val bottomSheetDialog = BottomSheetDialog(requireContext(),R.style.SheetDialog)
        bottomSheetDialog.setContentView(R.layout.bottom_dialog_tags)

        val tags = bottomSheetDialog.findViewById<Flow>(R.id.tags)
        val clTags = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.clTags)

        val listTag: List<String> = arrayListOf("1","1.1","1.2","2","2.1","3","3.1","3.2","3.3","3.4")
        var i = 1
        for (tag in listTag){
            val tagView = TagView(requireContext(), tag, cancelTag = object : TagClickListener {
                override fun onTagClosed(tagId:String) {
                    viewModel.eventCreationRequest.tags = tagId
                    binding.atvTags.text = tag
                    bottomSheetDialog.dismiss()
                }
            })
            tagView.id = i++
            clTags?.addView(tagView)
            tags?.addView(tagView)
        }
        bottomSheetDialog.show()
    }

    private fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun prepareFilePart(imageUri: String): String {

        val baos = ByteArrayOutputStream()
        BitmapFactory.decodeFile(imageUri).compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)

    }

    private fun configureBtnNext() {
        binding.btnNext.setOnClickListener {
            binding.btnNext.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.btn_click))
            showLoading()
            //create send request
            viewModel.createEvent()
            viewModel.createEventResponse.observe(viewLifecycleOwner, {
                if (!it.data.isNullOrEmpty()) {
                    hideLoading()
                    Toast.makeText(requireActivity(), "Успешно отправлено", Toast.LENGTH_LONG).show()
                    binding.etName.text = null
                    binding.etDescription.text = null
                    binding.atvTags.text = null
                    binding.ivBtnBack.callOnClick()
                } else if (!it.error.isNullOrEmpty()){
                    hideLoading()
                    Toast.makeText(requireActivity(), "Ошибка отправки: "+it.error.toString(), Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun configureBackBtn() {
        binding.ivBtnBack.setOnClickListener {
            hideLoading()
            parentFragmentManager.popBackStack()
        }
    }

    override fun showLoading() {
        binding.clLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.clLoading.visibility = View.GONE
    }

    override fun showError(error: String) {
        //todo
    }
}