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
import dt.prod.patternvm.listProblem.ui.observerProfile.FragmentObserverProfile

class FragmentList : Fragment() {
    companion object {
        const val EVENT_ADMIN_PROFILE = 0
        const val EVENT_OBSERVER_PROFILE = 1
    }

    private lateinit var binding: FragmentPlansBinding
    private lateinit var viewModel: PlansViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlansBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[PlansViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViewPager()
        when (TokenRepository.accessToken){
            "02" -> binding.vpUserEvents.currentItem = EVENT_ADMIN_PROFILE
            "03" -> binding.vpUserEvents.currentItem = EVENT_OBSERVER_PROFILE
        }
    }

    private fun configureViewPager() {
        val adapter = ViewPagerAdapter(requireActivity())
        adapter.addFrag(FragmentAdminProfile())
        adapter.addFrag(FragmentObserverProfile())
        binding.vpUserEvents.isUserInputEnabled = false
        binding.vpUserEvents.adapter = adapter
        binding.vpUserEvents.adapter?.notifyDataSetChanged()
    }
}