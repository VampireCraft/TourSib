package dt.prod.patternvm.core.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import dt.prod.patternvm.autorization.ui.AuthActivity
import dt.prod.patternvm.core.domain.ViewPagerNavigator
import dt.prod.patternvm.core.network.TokenRepository
import dt.prod.patternvm.createProblem.ui.FragmentCreateProblem
import dt.prod.patternvm.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ViewPagerNavigator {
    private lateinit var binding: ActivityMainBinding

    companion object {
        //const val vpEventsId = 0
        //const val vpPlansId = 1
        const val vpCreateEventId = 0
        //const val vpSearchId = 3
        //const val vpProfileId = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        configureViewPager()
        if (TokenRepository.accessToken.isEmpty())
            openAuthorization()
        else
            if (TokenRepository.accessToken == "010")
                openCreateEventScreen()
            else
                if (TokenRepository.accessToken == "013")
                    openEventsScreen()
    }

    private fun openAuthorization() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun configureViewPager() {
        val adapter = ViewPagerAdapter(this)
       // adapter.addFrag(FragmentEvents())
        //adapter.addFrag(FragmentPlans())
        adapter.addFrag(FragmentCreateProblem())
        //adapter.addFrag(FragmentSearch())
        //adapter.addFrag(FragmentProfile())
        binding.vpMain.isUserInputEnabled = false
        binding.vpMain.adapter = adapter
        binding.vpMain.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        val countFrag = supportFragmentManager.backStackEntryCount
        val countChild = 0

        if (countChild > 0) {
            for (str in supportFragmentManager.fragments) {
                if (str.childFragmentManager.backStackEntryCount >= 0) {
                    str.childFragmentManager.popBackStack()
                }
            }
        } else if (countFrag > 0) {
            supportFragmentManager.popBackStack()
        }
    }

    override fun openCreateEventScreen() {
        binding.vpMain.currentItem = 0

    }

    override fun openEventsScreen() {
        binding.vpMain.currentItem = 1

    }
}