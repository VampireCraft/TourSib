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
import dt.prod.patternvm.listProblem.ui.FragmentList
import dt.prod.patternvm.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ViewPagerNavigator {
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val vpCreateEventId = 0
        const val vpProfileId = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        configureViewPager()
        configureBottomNavigation()
        when (TokenRepository.accessToken){
            "01" -> openCreateEventScreen()
            "02" -> openCreateEventScreen()
            "03" -> openCreateEventScreen()
            else -> openAuthorization()
        }
    }

    override fun openAuthorization() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun configureViewPager() {
        val adapter = ViewPagerAdapter(this)
        adapter.addFrag(FragmentCreateProblem())
        adapter.addFrag(FragmentList())
        binding.vpMain.isUserInputEnabled = false
        binding.vpMain.adapter = adapter
        binding.vpMain.adapter?.notifyDataSetChanged()
    }

    private fun configureBottomNavigation() {
        binding.bnvMain.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.event -> binding.vpMain.currentItem = vpCreateEventId
                R.id.plans -> binding.vpMain.currentItem = vpProfileId
            }
            true
        }
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
        binding.vpMain.currentItem = vpCreateEventId
        binding.bnvMain.selectedItemId = R.id.event
    }

    override fun openEventsScreen() {
        binding.vpMain.currentItem = vpProfileId
        binding.bnvMain.selectedItemId = R.id.plans
    }
}