package com.cs4530.lifestyleapp

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserTable) : Long

    @Query("SELECT * FROM user_table")
    suspend fun getAll(): List<UserTable>
//
//    @Query("SELECT * FROM user_table WHERE uid IN (:userIds)")
//    suspend fun loadAllByIds(userIds: IntArray): List<UserTable>
//
    @Query("SELECT * FROM user_table WHERE id = :userId")
    suspend fun loadById(userId: Long): UserTable

    @Delete
    suspend fun delete(user: UserTable)
//    /**
//     * Delete the specified users from the database.
//     */
//    @Delete
//    suspend fun delete(vararg users: UserTable)
}
