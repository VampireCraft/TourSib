package dt.prod.patternvm.listProblem.ui.observerProfile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dt.prod.patternvm.R
import dt.prod.patternvm.autorization.ui.AuthActivity
import dt.prod.patternvm.core.domain.DialogAction
import dt.prod.patternvm.databinding.FragmentListProblemBinding
import dt.prod.patternvm.listProblem.models.PlansViewModel
import dt.prod.patternvm.core.model.Status
import dt.prod.patternvm.core.network.TokenRepository
import dt.prod.patternvm.core.ui.TwoButtonsDialog
import dt.prod.patternvm.listProblem.models.ListItemModel
import dt.prod.patternvm.listProblem.ui.PlansEventsAdapter

class FragmentObserverProfile : Fragment() {
    private lateinit var binding: FragmentListProblemBinding
    private lateinit var viewModel: PlansViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListProblemBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[PlansViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getListProblem()
        observeOnMyEvents()
        refreshOnSwipe()
    }

    private fun refreshOnSwipe() {
        binding.swipeRefreshLayout.setDistanceToTriggerSync(300)
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getListProblem()
        }
    }

    private fun observeOnMyEvents() {

        binding.ivExit2.visibility = VISIBLE
        binding.tvExit2.visibility = VISIBLE

        binding.ivExit2.setOnClickListener{
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.btn_click))
            TwoButtonsDialog(
                positiveBtnText = "Выйти",
                negativeTextBtn = "Остаться",
                title = "Подтверждение выхода из профиля",
                action = object : DialogAction {
                    override fun onPositiveBtnClicked() {
                        TokenRepository.deleteTokens()
                        val intent = Intent(requireContext(), AuthActivity::class.java)
                        requireActivity().startActivity(intent)
                        requireActivity().finish()
                    }

                    override fun onNegativeBtnClicked() {

                    }

                }
            ).show(parentFragmentManager, TwoButtonsDialog::class.simpleName)
        }

        viewModel.myEvents.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    Log.d("myEvents", "LOADING")
                }

                Status.SUCCESS -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                    Log.d("myEvents", "SUCCESS"+it.data)
                    it.data?.let { events ->
                        configureMyEvents(events)
                    }
                }
                Status.ERROR -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                    Log.d("myEvents", "ERROR"+it.error)
                }
            }
        })
    }

    private fun configureMyEvents(events: List<ListItemModel>) {
        if (!events.isNullOrEmpty()) {
            binding.rvEvents.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvEvents.adapter =
                PlansEventsAdapter(requireActivity(),parentFragmentManager, events, isMyEvent = 1)
        } else {
            binding.tvEmptyEvents.visibility = View.VISIBLE
            binding.rvEvents.visibility = View.GONE
        }
    }
}