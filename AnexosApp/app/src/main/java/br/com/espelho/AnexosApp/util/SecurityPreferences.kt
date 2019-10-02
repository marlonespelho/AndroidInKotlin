package br.com.espelho.AnexosApp.util

import android.content.Context
import android.content.SharedPreferences

class SecurityPreferences(context: Context) {

    private val mSharedPreferences:SharedPreferences = context.getSharedPreferences("anexos", Context.MODE_PRIVATE)

    fun storeSting(key: String, value:String){
        mSharedPreferences.edit().putString(key, value).apply()
    }

    fun getStoreString(key:String) : String?{
        return mSharedPreferences.getString(key,"")
    }

    fun removeStoreString(key:String){
        mSharedPreferences.edit().remove(key).apply()
    }

}