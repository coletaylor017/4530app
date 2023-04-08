package com.cs4530.lifestyleapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table")
data class UserTable(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = true)
    val id : Int = 0,

    @field:ColumnInfo(name = "first_name")
    var firstName : String? = "",

    @field:ColumnInfo(name = "last_name")
    var lastName : String? = "",

    @field:ColumnInfo(name = "age")
    var age: Int? = 0,

    @field:ColumnInfo(name = "city")
    var city: Int? = 0,

    @field:ColumnInfo(name = "country")
    var country: String? = null,

    @field:ColumnInfo(name = "height_feet")
    var heightFeet: Int? = 0,

    @field:ColumnInfo(name = "height_inches")
    var heightInches: Int? = 0,

    @field:ColumnInfo(name = "weight")
    var weight: Int? = 0,

    @field:ColumnInfo(name = "sex")
    var sex: String? = null,

    @field:ColumnInfo(name = "activity_level")
    var activityLevel: String? = null,

    @field:ColumnInfo(name = "bmr")
    var bmr: Int? = 0
)
