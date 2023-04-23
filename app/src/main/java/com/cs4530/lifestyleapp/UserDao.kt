package com.cs4530.lifestyleapp

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserTable)

    @Query("SELECT * FROM user_table")
    suspend fun getAll(): List<UserTable>
//
//    @Query("SELECT * FROM user_table WHERE uid IN (:userIds)")
//    suspend fun loadAllByIds(userIds: IntArray): List<UserTable>
//
//    @Query("SELECT * FROM user_table WHERE uid = :userId")
//    suspend fun loadById(userId: Int): UserTable
//
//    /**
//     * Update the specified users in the database.
//     */
//    @Update
//    suspend fun update(vararg users: UserTable)
//
//    /**
//     * Delete the specified users from the database.
//     */
//    @Delete
//    suspend fun delete(vararg users: UserTable)
}
