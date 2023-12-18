package com.example.notificationlogger.Models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
public interface LogDataDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(data: LogDataModel): Long

    @Query("SELECT * FROM LogTable WHERE id = :id")
    suspend fun getDataById(id: Int): LogDataModel?

    @Query("DELETE FROM LogTable WHERE id = :id")
    suspend fun deleteData(id: Int): Int

    @Query("SELECT * FROM LogTable")
    suspend fun getAllData(): List<LogDataModel>




}