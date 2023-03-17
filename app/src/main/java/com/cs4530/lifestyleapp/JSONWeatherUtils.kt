package com.cs4530.lifestyleapp

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type


object JSONWeatherUtils : JsonDeserializer<WeatherData> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): WeatherData {
        val weatherData = WeatherData()
        val jsonObject = json!!.asJsonObject

        val jsonMain = jsonObject.get("main")
        val weatherInfo = jsonMain.asJsonObject
        weatherData.temperature = weatherInfo.get("temp").asDouble
        weatherData.tempHigh = weatherInfo.get("temp_max").asDouble
        weatherData.tempLow = weatherInfo.get("temp_min").asDouble
        weatherData.humidity = weatherInfo.get("humidity").asDouble

        return weatherData
    }
}