package dt.prod.patternvm.autorization.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import dt.prod.patternvm.core.ui.BaseFragment
import dt.prod.patternvm.core.ui.MainActivity
import dt.prod.patternvm.databinding.FragmentLogInBinding
import dt.prod.patternvm.R
import dt.prod.patternvm.core.network.TokenRepository

@AndroidEntryPoint
class FragmentLogIn : BaseFragment() {
    private lateinit var binding: FragmentLogInBinding
    lateinit var animation: Animation
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEnter.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.btn_click))
            if (isLoginValid())
                //true auth
                openMainWindow()
            else
                showError("Пустой или не верный код доступа")
        }
    }

    private fun isLoginValid(): Boolean {
        val login = binding.etLogin.text.toString()
        if (login == "01" || login == "02" || login == "03")
            return true
        return login.isNotEmpty()
    }

    private fun openMainWindow() {
        TokenRepository.saveTokens(binding.etLogin.text.toString())
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finishAffinity()
    }

    override fun showLoading() {
        //binding.pbLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        //binding.pbLoading.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(requireActivity(), error, Toast.LENGTH_LONG).show()
    }
}