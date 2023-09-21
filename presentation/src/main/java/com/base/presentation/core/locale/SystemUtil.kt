package com.base.presentation.core.locale

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration

import java.util.Locale


class SystemUtil {

    private val LANG_PREF = "lang_pref"
    private var myLocale: Locale? = null

    // Lưu ngôn ngữ đã cài đặt
    fun saveLocale(context: Context, lang: String?) {
        setPreLanguage(context, lang)
    }

    // Load lại ngôn ngữ đã lưu và thay đổi chúng
    fun setLocale(context: Context): Context {
        val language = getPreLanguage(context)
        if (language == null || language == "") {
            val config = Configuration()
            val locale = Locale.getDefault()
            Locale.setDefault(locale)
            config.setLocale(locale)
            config.setLayoutDirection(locale)
            return context.createConfigurationContext(config)
        } else {
            return changeLang(language, context)
        }
    }

    // method phục vụ cho việc thay đổi ngôn ngữ.
    private fun changeLang(lang: String, context: Context): Context {
        val configuration = context.resources.configuration
        val locale = Locale(lang)
        Locale.setDefault(locale)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun getPreLanguage(mContext: Context): String? {
        val preferences: SharedPreferences = mContext.getSharedPreferences(LANG_PREF, Context.MODE_PRIVATE)
        return preferences.getString("KEY_LANGUAGE", "")
    }

    private fun setPreLanguage(context: Context, language: String?) {
        if (language == null || language == "") return

        val preferences: SharedPreferences = context.getSharedPreferences(LANG_PREF, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("KEY_LANGUAGE", language)
        editor.apply()
    }

    companion object {
        @JvmStatic
        private var mInstance: SystemUtil? = null

        @JvmStatic
        fun getInstance(): SystemUtil {
            if (mInstance == null) mInstance = SystemUtil()
            return mInstance!!
        }
    }
}