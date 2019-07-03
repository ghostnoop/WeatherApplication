package com.example.weatherapplication.models

data class Weather(val name: String ,val main: Main, val wind: Wind,val cod:Int)
data class Main(val temp: Double)
data class Wind(val speed: Double, val deg: Double)
