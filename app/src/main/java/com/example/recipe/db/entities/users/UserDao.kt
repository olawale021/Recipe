package com.example.recipe.db.entities.users

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.recipe.db.entities.users.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users WHERE userID = :userId")
    fun getUserById(userId: Int): User?

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>

    @Query(
        "SELECT * FROM users where email = :email"
    )
    fun getUser(email: String): User?
}