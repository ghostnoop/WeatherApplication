package com.example.weatherapplication


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinretro.api.RetrofitClient
import com.example.weatherapplication.models.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        refreshlay.setOnRefreshListener {
            getcitys()
        }




    }

    lateinit var delItemt: MenuItem
    lateinit var addItem: MenuItem
    lateinit var denyItem: MenuItem


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        delItemt = menu.findItem(R.id.action_del)
        addItem = menu.findItem(R.id.action_add)
        denyItem = menu.findItem(R.id.action_deny)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_del -> {
                delItemt.isVisible = false
                addItem.isVisible = false
                denyItem.isVisible = true
                actionCity(1)

                true
            }
            R.id.action_add -> {
                startActivity(Intent(this@MainActivity, AdderActivity::class.java))

                true
            }
            R.id.action_deny -> {
                actionCity(2)
                delItemt.isVisible = true
                addItem.isVisible = true
                denyItem.isVisible = false

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    var weath: MutableList<Weather> = mutableListOf()

    private fun fetchWeather(citys: List<Citynames>) {
        val weather: MutableList<Weather> = mutableListOf()
        weath = weather



        for (i in 0..citys.size - 1) {
            val url =
                "http://api.openweathermap.org/data/2.5/weather?q=" + citys[i].cityname + "&APPID=692af1539a2faebd8369fbe3d00c4fb6"

            val request = Request.Builder().url(url).build()
            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call?, response: Response?) {
                    Log.e("size ", i.toString())


                    val body = response?.body()?.string()
                    println(body)

                    val gson = GsonBuilder().create()

                    val homeFeed = gson.fromJson(body, Weather::class.java)
                    if (homeFeed.cod.equals(200)) {
                        weather.add(homeFeed)
                    }
                    runOnUiThread {
                        refreshlay.isRefreshing = false

                        if (weather.size > 0)
                            showeather(weather,1)

                    }

                }

                override fun onFailure(call: Call?, e: IOException?) {
                    println("Failed to execute request")
                    msg("error")

                }
            })
        }


    }


    fun showeather(weather: MutableList<Weather>,j:Int) {
        recyclerViewWeather.layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewWeather?.adapter = WeatherAdapter(weather,j)
        actionCity(1)
    }


    fun actionCity(did: Int) {
        recyclerViewWeather.layoutManager = LinearLayoutManager(applicationContext)
        if (did.equals(0)) {
            recyclerViewWeather.addOnItemClickListener(object : OnItemClickListener {
                override fun onItemClicked(position: Int, view: View) {

                }
            })
        }
        if (did.equals(1)) {
            recyclerViewWeather.addOnItemClickListener(object : OnItemClickListener {
                override fun onItemClicked(position: Int, view: View) {
                    msg(weath[position].name)
                    delcity(position)

                }
            })
        } else
            recyclerViewWeather.clearOnChildAttachStateChangeListeners()


    }

    private fun delcity(position: Int) {
        RetrofitClient.instance.deleteCity(weath[position].name)
            .enqueue(object : retrofit2.Callback<DefaultResponse> {
                override fun onFailure(call: retrofit2.Call<DefaultResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: retrofit2.Call<DefaultResponse>,
                    response: retrofit2.Response<DefaultResponse>
                ) {
                    msg(response.body().toString())
                }
            })
    }

    private fun getcitys() {

        RetrofitClient.instance.getCitys()
            .enqueue(object : retrofit2.Callback<List<Citynames>> {
                override fun onFailure(call: retrofit2.Call<List<Citynames>>, t: Throwable) {
                    msg("eror")
                }

                override fun onResponse(
                    call: retrofit2.Call<List<Citynames>>,
                    response: retrofit2.Response<List<Citynames>>
                ) {
                    msg(response.body().toString())
                    if (response.body().toString() == "[]") {
                        refreshlay.isRefreshing = false
                        showeather(weath, 0)
                    }
                    else {
                        response.body()?.let {
                            fetchWeather(it)
                            msg(it.size.toString())

                        }

                    }
                }
            })
    }

    override fun onStart() {
        super.onStart()

        getcitys()

    }






    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }

    fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener({
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                })
            }

            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)

            }


        })

    }

    fun msg(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }


}
