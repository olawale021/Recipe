package com.example.recipe.db.entities.users

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Query(
        "SELECT * FROM user"
    )
    fun getAllUsers(): LiveData<List<User>>

    @Query(
        "SELECT * FROM user where email = :email"
    )
    fun getUser(email: String): User?

    @Query(
        "SELECT * FROM user where id = :userId"
    )
    suspend fun getUserById(userId: Int): User?
}