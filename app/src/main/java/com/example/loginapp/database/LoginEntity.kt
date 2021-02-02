package com.example.loginapp.database
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registered_users_table")
data class LoginEntity(
        @PrimaryKey(autoGenerate = true)
        var userId:Long=0L,
        @ColumnInfo(name = "businessName")
        val businessName:String,
        @ColumnInfo(name = "firstName")
        val firstName:String,
        @ColumnInfo(name = "lastName")
        val lastName:String,
        @ColumnInfo(name = "email")
        val email:String,
        @ColumnInfo(name = "cin")
        val cin:String,
        @ColumnInfo(name = "phone")
        val phone:String,
        @ColumnInfo(name = "password")
        val password:String,
        @ColumnInfo(name = "customerType")
        val customerType:String
)