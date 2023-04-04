package com.cs4530.lifestyleapp

import com.google.gson.annotations.SerializedName

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherData(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = true)
    var id: Int,

    @field:ColumnInfo(name = "current_temperature")
    @SerializedName("temp")
    var temperature: Int,

    @field:ColumnInfo(name = "max_temperature")
    @SerializedName("temp_max")
    var tempHigh: Int,

    @field:ColumnInfo(name = "min_temperature")
    @SerializedName("temp_min")
    var tempLow: Int,

    @field:ColumnInfo(name = "humidity")
    var humidity: Double,
)