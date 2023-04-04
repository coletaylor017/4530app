package com.cs4530.lifestyleapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Placeholder for ViewModel of main activity. Will be fully implemented after Repository and Room DB are finished.
class MainViewModel(repository: MainRepository) :  ViewModel(){
    // Connect a live data object to the current bit of weather info
    private val weatherData: LiveData<WeatherData> = repository.weatherData

    private var mRepository: MainRepository = repository

    fun setLocation(location: String) {
        // Simply pass the location to the repository
        mRepository.setLocation(location)
    }

    // Returns the data contained in the live data object
    val data: LiveData<WeatherData>
        get() = weatherData
}

// This factory class allows us to define custom constructors for the view model
class ViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}