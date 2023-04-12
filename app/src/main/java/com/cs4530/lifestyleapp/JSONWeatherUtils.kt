package com.cs4530.lifestyleapp

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import kotlin.math.roundToInt


object JSONWeatherUtils : JsonDeserializer<WeatherTable> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): WeatherTable {
        val weatherData = WeatherTable()
        val jsonObject = json!!.asJsonObject

        // TODO: issue with get("weather") bc it's an array of objects, not an object
        // https://openweathermap.org/api/one-call-3
        //https://openweathermap.org/weather-conditions#How-to-get-icon-URL
        // val iconsInfo = jsonObject.get("weather").asJsonObject
        val weatherInfo = jsonObject.get("main").asJsonObject
        val currentTemp = getFahrenheit(weatherInfo.get("temp").asInt)
        val currentHighTemp = getFahrenheit(weatherInfo.get("temp_max").asInt)
        val currentLowTemp = getFahrenheit(weatherInfo.get("temp_min").asInt)

        weatherData.temperature = currentTemp
        weatherData.tempHigh = currentHighTemp
        weatherData.tempLow = currentLowTemp
        weatherData.humidity = weatherInfo.get("humidity").asInt
        // weatherData.icon = iconsInfo.get("icon").asString
        weatherData.icon = "TBD"

        return weatherData
    }

    private fun getFahrenheit(temp: Int) : Int? {
        return (((temp.minus(273.15))?.
        times(9)?.
        div(5))?.
        plus(32))?.
        roundToInt()
    }
}