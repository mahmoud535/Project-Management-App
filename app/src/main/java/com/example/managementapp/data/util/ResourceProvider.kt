package com.example.managementapp.data.util

import android.content.Context
import android.content.res.Configuration
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext private val context: Context) {

    // Get a string resource by its ID
    fun getString(resId: Int): String {
        return context.getString(resId)
    }

    // Get a formatted string resource
    fun getString(resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }

    // Get a string array resource
    fun getStringArray(resId: Int): Array<String> {
        return context.resources.getStringArray(resId)
    }

    // Expose the context if needed
    fun getContext(): Context {
        return context
    }
}