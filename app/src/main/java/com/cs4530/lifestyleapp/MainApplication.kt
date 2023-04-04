package com.cs4530.lifestyleapp

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {
    //Get a global scope for all coroutines
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Inject scope and application context into singleton database
    val database by lazy{ MainRoomDatabase.getDatabase(this,applicationScope)}

    //TODO: Add repository to application

    // Inject database dao and scope into singleton repository
    // maintain a single global scope used for all coroutine operations in the repository and db.
    //val repository by lazy{ MainRepository.getInstance(database.MainDao(),applicationScope)}
}