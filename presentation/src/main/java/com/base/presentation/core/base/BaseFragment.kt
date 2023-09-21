package com.base.presentation.core.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.base.presentation.utils.extentions.popBackStack
import com.bumptech.glide.Glide

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: FragmentBindingInflater<VB>
) : Fragment(), ViewInterface {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding
            ?: throw IllegalStateException("Cannot access view after view destroyed or before view creation")

    open val isHandleBackPress = true

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            this@BaseFragment.handleOnBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(savedInstanceState)
        viewListener()
        dataObservable()
    }

    override fun onStart() {
        Glide.with(this).onStart()
        super.onStart()
    }

    override fun onStop() {
        Glide.with(this).onStop()
        super.onStop()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Glide.with(this).onConfigurationChanged(newConfig)
        super.onConfigurationChanged(newConfig)
    }

    override fun onDestroyView() {
        _binding = null
        try {
            Glide.with(this).onDestroy()
        } catch (_: Exception) {
        }
        super.onDestroyView()
    }

    override fun viewListener() {}

    override fun initView(savedInstanceState: Bundle?) {}

    override fun dataObservable() {}

    open fun handleOnBackPressed() {
        requireActivity().popBackStack()
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun isLoading(isShow: Boolean) {
        if (activity != null && activity is BaseActivity<*>) {
            (activity as BaseActivity<*>?)!!.isLoading(isShow)
        }
    }
}
