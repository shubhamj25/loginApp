package com.example.loginapp.database
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
@Dao
interface LoginDatabaseDao{
    @Insert
    fun insertUser(user:LoginEntity)

    @Update
    fun updateUser(user:LoginEntity)

    @Query("SELECT * FROM registered_users_table WHERE userId=:key")
    fun getById(key:Long):LoginEntity?

    @Query("SELECT * FROM registered_users_table WHERE email=:email")
    fun getByEmail(email: String):LoginEntity?

    @Query("SELECT * FROM registered_users_table WHERE email=:email AND password=:password")
    fun checkCredentials(email:String,password:String):LoginEntity?

    @Query("DELETE FROM registered_users_table WHERE userId=:key")
    fun deleteById(key:Long)

    @Query("DELETE FROM registered_users_table")
    fun clearDatabase()

    @Query("SELECT * FROM registered_users_table")
    fun getAllUsers(): LiveData<MutableList<LoginEntity>>
}
