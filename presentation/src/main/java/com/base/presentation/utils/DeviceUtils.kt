package com.base.presentation.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import com.base.presentation.utils.StringUtils.capitalize

object DeviceUtils {
    fun copyToClipBoard(context: Context, text: String?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Code Copied!", Toast.LENGTH_LONG).show()
    }

    val deviceName: String
        get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                capitalize(model)
            } else {
                capitalize(manufacturer) + " " + model
            }
        }

    val osVersion: String
        get() = Build.VERSION.RELEASE
}