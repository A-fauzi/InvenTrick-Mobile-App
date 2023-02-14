package com.example.warehouseproject.core.helper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.warehouseproject.core.utils.DataUser.EMAIL
import com.example.warehouseproject.core.utils.DataUser.PHOTO_URI
import com.example.warehouseproject.core.utils.DataUser.USERNAME

object SavedPreferenceUser {

    private fun getSharedPreference(ctx: Context?): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    private fun editor(context: Context, const: String, string: String) {
        getSharedPreference(
            context
        )?.edit()?.putString(const, string)?.apply()
    }

    fun getEmail(context: Context) = getSharedPreference(
        context
    )?.getString(EMAIL, "")

    fun setEmail(context: Context, email: String) {
        editor(
            context,
            EMAIL,
            email
        )
    }


    fun getUsername(context: Context) = getSharedPreference(
        context
    )?.getString(USERNAME, "")

    fun setUsername(context: Context, username: String) {
        editor(
            context,
            USERNAME,
            username
        )
    }

    fun getPhoto(context: Context) = getSharedPreference(context)?.getString(PHOTO_URI, "")
    fun setPhoto(context: Context, photo: String) {
        editor(context, PHOTO_URI, photo)
    }

}