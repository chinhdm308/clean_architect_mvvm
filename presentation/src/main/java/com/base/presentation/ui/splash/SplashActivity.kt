package com.base.presentation.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.base.presentation.core.base.BaseActivity
import com.base.presentation.databinding.ActivitySplashBinding
import com.base.presentation.ui.main.MainActivity
import com.base.presentation.utils.AppConstants

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override val hasWindowInsets: Boolean
        get() = true

    private var handler: Handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable = Runnable {
        navigateToMainScreen()
    }

    /**
     * In this method we are applying delay of SPLASH_DISPLAY_LENGTH
     */
    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, AppConstants.SPLASH_DISPLAY_TIME)
    }

    /**
     * This method is called when activity is destroyed.
     * In this, callbacks of handler removed.
     */
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}