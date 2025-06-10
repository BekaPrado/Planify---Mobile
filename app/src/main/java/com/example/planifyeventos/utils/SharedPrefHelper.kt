package com.example.planifyeventos.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPrefHelper {
    private const val PREF_NAME = "planify_prefs"
    private const val KEY_EMAIL = "email_usuario"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun salvarEmail(context: Context, email: String) {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(KEY_EMAIL, email)
            apply()
        }
    }

    fun obterEmail(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_EMAIL, null)
    }
    fun recuperarEmail(context: Context): String? {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_EMAIL, null)
    }

    fun deslogar(context: Context) {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove(KEY_EMAIL)
            apply()
        }
    }
}