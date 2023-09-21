package com.base.presentation.core.base

import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import com.base.presentation.core.locale.SystemUtil

abstract class BaseActivity<VB : ViewBinding>(
    private val inflater: ActivityBindingInflater<VB>,
) : AppCompatActivity(), ViewInterface {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding
            ?: throw IllegalStateException("Cannot access view after view destroyed or before view creation")

    protected open val hasWindowFocusChanged: Boolean = false
    protected open val hasWindowInsets: Boolean = false

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(SystemUtil.getInstance().setLocale(newBase))
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && hasWindowFocusChanged) {
            fullScreenImmersive(window)
        }
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        super.applyOverrideConfiguration(baseContext.resources.configuration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        //window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) //Set transparent status bar
        super.onCreate(savedInstanceState)
        adjustFontScale(this, resources.configuration)
        _binding = inflater.invoke(layoutInflater)
        setContentView(requireNotNull(_binding).root)

        //changeStatusBarContrastStyle(true)

        initView(savedInstanceState)
        viewListener()
        dataObservable()
    }

    @Suppress("DEPRECATION")
    override fun onResume() {
        super.onResume()

        if (hasWindowInsets) {
            val windowInsetsController = if (Build.VERSION.SDK_INT >= 30) {
                ViewCompat.getWindowInsetsController(window.decorView)
            } else {
                WindowInsetsControllerCompat(window, window.decorView)
            }

            if (windowInsetsController == null) {
                return
            }

            windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
            windowInsetsController.hide(WindowInsetsCompat.Type.systemGestures())

            window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility == 0) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        @Suppress("DEPRECATION") val windowInsetsController1 = if (Build.VERSION.SDK_INT >= 30) {
                            ViewCompat.getWindowInsetsController(window.decorView)
                        } else {
                            WindowInsetsControllerCompat(window, window.decorView)
                        }

                        windowInsetsController1?.hide(WindowInsetsCompat.Type.navigationBars())
                        windowInsetsController1?.hide(WindowInsetsCompat.Type.systemGestures())
                    }, 3000)
                }
            }
        }
    }

    /**
     * Close SoftKeyboard when user click out of EditText
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun initView(savedInstanceState: Bundle?) {}

    override fun viewListener() {}

    override fun dataObservable() {}

    fun isLoading(isShow: Boolean) {}

    private fun changeStatusBarContrastStyle(lightIcons: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!lightIcons) {
                // Sets the color of icons to black (for the light status bar)
                // icon color -> black
                window.decorView.getWindowInsetsController()?.setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS)
            } else {
                // Clears the appearance (i.e. turns icon color to white for dark status bar)
                // icon color -> white
                window.decorView.getWindowInsetsController()?.setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS)
            }
        } else {
            // For API 30 and below
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val decorView = window.decorView
                if (!lightIcons) {
                    // Sets the color of icons to black (for the light status bar)
                    // icon color -> black
                    decorView.systemUiVisibility = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    // Clears the appearance (i.e. turns icon color to white for dark status bar)
                    // icon color -> white
                    decorView.systemUiVisibility = decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
            }
        }
    }

    private fun fullScreenImmersive(windows: Window?) {
        if (windows != null) {
            fullScreenImmersive(windows.decorView)
        }
    }

    private fun fullScreenImmersive(view: View) {
        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        view.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun adjustFontScale(context: Context, configuration: Configuration) {
        if (configuration.fontScale != 1f) {
            configuration.fontScale = 1f
            val metrics = context.resources.displayMetrics
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            context.resources.updateConfiguration(configuration, metrics)
        }
    }
}
