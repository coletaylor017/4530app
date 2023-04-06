package com.cs4530.lifestyleapp

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type


object JSONWeatherUtils : JsonDeserializer<WeatherTable> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): WeatherTable {
        val weatherData = WeatherTable()
        val jsonObject = json!!.asJsonObject

        val jsonMain = jsonObject.get("main")
        val weatherInfo = jsonMain.asJsonObject
        weatherData.temperature = weatherInfo.get("temp").asInt
        weatherData.tempHigh = weatherInfo.get("temp_max").asInt
        weatherData.tempLow = weatherInfo.get("temp_min").asInt
        weatherData.humidity = weatherInfo.get("humidity").asDouble

        return weatherData
    }
}