package com.cs4530.lifestyleapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
class UserTable {
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = true)
    val id : Int = 0
}
