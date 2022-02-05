package dt.prod.patternvm.createProblem.ui

import android.graphics.*
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import dt.prod.patternvm.core.ui.BaseFragment
import dt.prod.patternvm.createProblem.models.CreateProblemViewModel
import dt.prod.patternvm.R
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
        viewModel = ViewModelProvider(requireActivity()).get(CreateProblemViewModel::class.java)
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
            //create send request

        }
    }

    private fun configureBackBtn() {
        binding.ivBtnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun showLoading() {
//        binding.llPickedColor.apply {
//            startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.loading))
//        }
    }

    override fun hideLoading() {
        //binding.llPickedColor.clearAnimation()
    }

    override fun showError(error: String) {
        //todo
    }
}