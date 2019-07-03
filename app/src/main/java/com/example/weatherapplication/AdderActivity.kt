package com.example.weatherapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import com.example.kotlinretro.api.RetrofitClient
import com.example.weatherapplication.models.DefaultResponse
import com.example.weatherapplication.models.Weather
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_adder.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class AdderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adder)


        adder_btn.setOnClickListener {
        checkcity()
        }
        city_edit.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.d("Android view component", "Enter button was pressed")
                checkcity()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

    }




    fun checkcity() {

        val city = city_edit.text.toString()
        if(city.isEmpty()){
            city_edit.error = "Пусто"
            city_edit.requestFocus()
            return
        }

        val url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=692af1539a2faebd8369fbe3d00c4fb6"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {


                val body = response?.body()?.string()
                val gson = GsonBuilder().create()

                val homeFeed = gson.fromJson(body, Weather::class.java)
                runOnUiThread {
                    if (homeFeed.cod.equals(200)) {

                        addcity(homeFeed.name)
                    } else {
                        city_edit.error = "Город не тот"
                        city_edit.requestFocus()
                        return@runOnUiThread
                    }

                }

            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute request")
                Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT).show()

            }
        })
    }
    fun addcity(city: String) {
        RetrofitClient.instance.createCity(city)
            .enqueue(object : retrofit2.Callback<DefaultResponse> {
                override fun onFailure(call: retrofit2.Call<DefaultResponse>, t: Throwable) {
                    msg(t.message.toString())
                }

                override fun onResponse(
                    call: retrofit2.Call<DefaultResponse>,
                    response: retrofit2.Response<DefaultResponse>
                ) {
                    msg(response.body()?.toString() + "\n" + "error")
                    if(response.body() == null)
                        city_edit.error = "Город существует"
                        city_edit.requestFocus()
                        return
                }

            })
    }
    fun msg(msg: String) {
//        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

}
