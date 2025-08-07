package com.threedev.flashlight.ui.onbaording

<<<<<<< HEAD
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
=======
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
import androidx.viewpager.widget.ViewPager
import com.threedev.flashlight.MainActivity
import com.threedev.flashlight.R
import com.threedev.flashlight.databinding.ActivityOnBoardBinding
<<<<<<< HEAD
import com.threedev.flashlight.helper.AppController
=======
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889

class OnBoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardBinding.inflate(layoutInflater)
<<<<<<< HEAD
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.clRoot) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
=======
        setContentView(binding.root)

>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
        hideSystemUI()

        val layouts = intArrayOf(
            R.layout.onboarding_screen1,
            R.layout.onboarding_screen2,
            R.layout.onboardin_screen3
        )

        val adapter = OnboardAdapter(this, layouts)
        binding.viewPager.adapter = adapter
        binding.btnGetStarted.text = getString(R.string.next)

        // Background Change & Button Text Update
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                val background = when (position) {
                    0 -> R.drawable.onboard1_background
                    1 -> R.drawable.onboard1_background
                    2 -> R.drawable.onboard_screen3_background
                    else -> R.drawable.onboard1_background
                }
                binding.clRoot.setBackgroundResource(background)

                if (position == layouts.size - 1) {
                    binding.btnGetStarted.text = getString(R.string.get_started)
                } else {
                    binding.btnGetStarted.text = getString(R.string.next)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        // "Next" Button Click Listener
        binding.btnGetStarted.setOnClickListener {
            val current = binding.viewPager.currentItem
            val nextItem = current + 1

            if (nextItem < layouts.size) {
                binding.viewPager.currentItem = nextItem
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // "Skip" Button Click Listener
        binding.btnSkip.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }
<<<<<<< HEAD
    override fun attachBaseContext(newBase: Context) {
        val context = AppController.setAppLocale(newBase)
        super.attachBaseContext(context)
    }

=======
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
}
