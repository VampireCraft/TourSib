package dt.prod.patternvm.core.ui

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    abstract fun showLoading()
    abstract fun hideLoading()
    abstract fun showError(error: String)
}