package com.example.weatherapplication.strorage

import android.content.Context
import android.widget.Toast


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SharedPrefManager private constructor(private val mCtx: Context) {





    val citys:MutableSet<String>
    get()
    {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet("cit",citys) as MutableSet<String>

    }



    fun addCitys(city:String){
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editer = sharedPreferences.edit()
        citys.add(city)
        editer.putStringSet("cit",citys)
        editer.apply()
}



    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
//
    companion object {
        val SHARED_PREF_NAME = "my_shared_preff"
        private val SHARED_PREF_ADVER = "my_shared_adver"

        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

}