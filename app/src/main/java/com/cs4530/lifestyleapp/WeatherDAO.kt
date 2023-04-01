package com.cs4530.lifestyleapp

import androidx.room.*


interface WeatherDao {
    // Insert ignore
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weatherTable: WeatherData)

    // Delete all
    @Query("DELETE FROM weather_table")
    suspend fun deleteAll()

}