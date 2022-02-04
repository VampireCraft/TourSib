package dt.prod.patternvm.core.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dt.prod.patternvm.R
import dt.prod.patternvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}