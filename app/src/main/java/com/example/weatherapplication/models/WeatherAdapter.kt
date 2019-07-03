package com.example.weatherapplication.models

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface.BOLD
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.set
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinretro.api.RetrofitClient
import com.example.weatherapplication.R
import kotlinx.android.synthetic.main.layout_weather.view.*

class WeatherAdapter (val weathers: MutableList<Weather>,var j:Int):
RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>(){




    override fun getItemCount(): Int {
        return weathers.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            WeatherViewHolder {


        return WeatherViewHolder(

            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_weather, parent, false)

        )



    }





    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {


        val winddir = arrayOf("юг", "север", "юго-восток", "восток", "северо-восток", "северо-запад", "запад", "юго-запад")

        val weather = weathers[position]

        if(j==0 || weather.name==""){
            holder.view.cityname.text = ""
            holder.view.temperature.text = ""
            holder.view.windspeed.text = ""
            holder.view.deggre.text = ""
        }
        else {

            holder.view.cityname.text = weather.name
            val temp = SpannableString("Температура: " + (weather.main.temp - 273.15).toInt().toString() + " °C")
            holder.view.temperature.text = getblack(temp, "Температура: ")
            val speed = SpannableString("Скорость ветра: " + weather.wind.speed.toString())
            holder.view.windspeed.text = getblack(speed, "Скорость ветра: ")
            val deg = SpannableString("Направление ветра: " + winddir[checkdir(weather.wind.deg).toInt()])
            holder.view.deggre.text = getblack(deg, "Направление ветра: ")
        }








    }







    fun checkdir(a: Double): Double {
        if(a>157.5 && a<202.5)
            return 0.0
        //юг

        if(a>337.5 || a<22.5)
            return 1.0
        //север
        if(a>=112.5 && a<=157.5)
            return 2.0
        //юг восток
        if(a>=67.5 && a<112.5)
            return 3.0
        //восток
        if(a>=22.5 && a<67.5)
            return 4.0
        //север восток
        if(a>292.5 && a<337.5)
            return 5.0
        //север запад
        if(a>247.5 && a<292.5)
            return 6.0
        //запад
        if(a>202.5 && a<247.5)
            return 7.0
        //юг запад
        return -1.0
    }

    fun getblack(a: Spannable,b:String): Spannable {
//          val span = SpannableString("Температура: "+ (weather[position].main.temp-273.15).toInt().toString() +" °C")
//        span.setSpan( ForegroundColorSpan(Color.BLACK),13,span.length,1)
        a.setSpan(
            ForegroundColorSpan(Color.BLACK),
            b.length,a.length,1
        )
        return a

    }

    class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val view = view



    }
}




