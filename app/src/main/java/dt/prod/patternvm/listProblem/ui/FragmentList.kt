package dt.prod.patternvm.listProblem.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dt.prod.patternvm.core.network.TokenRepository
import dt.prod.patternvm.databinding.FragmentPlansBinding
import dt.prod.patternvm.core.ui.ViewPagerAdapter
import dt.prod.patternvm.listProblem.models.PlansViewModel
import dt.prod.patternvm.listProblem.ui.adminProfile.FragmentAdminProfile

class FragmentList : Fragment() {
    companion object {
        const val EVENT_ADMIN_PROFILE = 0
        const val EVENT_CREATED_BY_ME = 1
    }

    private lateinit var binding: FragmentPlansBinding
    private lateinit var viewModel: PlansViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlansBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(PlansViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViewPager()
        when (TokenRepository.accessToken){
            "02" -> binding.vpUserEvents.currentItem = EVENT_ADMIN_PROFILE
            "03" -> binding.vpUserEvents.currentItem = 1
        }
    }

//    private fun configureNavigation() {
//        binding.btnWhereIGo.setOnClickListener {
//            binding.vpUserEvents.setCurrentItem(EVENT_WHERE_I_GO, true)
//            it.background = ContextCompat.getDrawable(
//                requireContext(),
//                R.drawable.decoration_rounded_yellow_filled
//            )
//            binding.btnCreatedByMe.background = ContextCompat.getDrawable(
//                requireContext(),
//                R.drawable.decoration_rounded_yellow_border
//            )
//            binding.btnWhereIGo.setTextColor(ContextCompat.getColor(it.context, R.color.main))
//            binding.btnCreatedByMe.setTextColor(
//                ContextCompat.getColor(
//                    it.context,
//                    R.color.secondary
//                )
//            )
//        }
//
//        binding.btnCreatedByMe.setOnClickListener {
//            binding.vpUserEvents.setCurrentItem(EVENT_CREATED_BY_ME, true)
//            it.background = ContextCompat.getDrawable(
//                requireContext(),
//                R.drawable.decoration_rounded_yellow_filled
//            )
//            binding.btnWhereIGo.background = ContextCompat.getDrawable(
//                requireContext(),
//                R.drawable.decoration_rounded_yellow_border
//            )
//            binding.btnCreatedByMe.setTextColor(ContextCompat.getColor(it.context, R.color.main))
//            binding.btnWhereIGo.setTextColor(ContextCompat.getColor(it.context, R.color.secondary))
//        }
//    }

    private fun configureViewPager() {
        val adapter = ViewPagerAdapter(requireActivity())
        adapter.addFrag(FragmentAdminProfile())
        //adapter.addFrag(FragmentEventsWhereIGo())
        binding.vpUserEvents.isUserInputEnabled = false
        binding.vpUserEvents.adapter = adapter
        binding.vpUserEvents.adapter?.notifyDataSetChanged()
    }
}