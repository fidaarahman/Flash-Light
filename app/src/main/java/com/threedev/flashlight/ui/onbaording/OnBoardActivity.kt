package com.threedev.flashlight.ui.onbaording

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.threedev.flashlight.MainActivity
import com.threedev.flashlight.R
import com.threedev.flashlight.databinding.ActivityOnBoardBinding

class OnBoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}
