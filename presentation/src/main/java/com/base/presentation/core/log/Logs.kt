package com.base.presentation.core.log

import android.util.Log

object Logs {
    fun d(tag: String, message: String) {
        log(Log.DEBUG, tag + tagColor(Log.DEBUG), message, trac)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        log(Log.ERROR, tag + tagColor(Log.ERROR), message, trac, throwable)
    }

    fun i(tag: String, message: String) {
        log(Log.INFO, tag + tagColor(Log.INFO), message, trac)
    }

    fun w(tag: String, message: String) {
        log(Log.WARN, tag, message, trac)
    }

    fun v(tag: String, message: String) {
        log(Log.VERBOSE, tag + tagColor(Log.WARN), message, trac)
    }

    private val trac: StackTraceElement
        get() {
            val stackTraceElements = Thread.currentThread().stackTrace
            return stackTraceElements[4]
        }

    private fun tagColor(priority: Int): String {
        return when (priority) {
            Log.VERBOSE -> "🛎️"
            Log.DEBUG -> "🛠️"
            Log.INFO -> "🟢"
            Log.WARN -> "⚠️"
            Log.ERROR -> "❌"
            else -> "🟡"
        }
    }

    private fun log(priority: Int, tag: String, message: String, callingMethod: StackTraceElement, throwable: Throwable? = null) {
        val fullMess = message + "| File: " + callingMethod.fileName + ", Method: " + callingMethod.methodName + ", line: " + callingMethod.lineNumber
        when (priority) {
            Log.VERBOSE -> Log.v(tag, fullMess)
            Log.DEBUG -> Log.d(tag, fullMess)
            Log.INFO -> Log.i(tag, fullMess)
            Log.WARN -> Log.w(tag, fullMess)
            Log.ERROR -> Log.e(tag, fullMess, throwable)
            else -> Log.d("⏱$tag⏱", fullMess)
        }
    }
}
