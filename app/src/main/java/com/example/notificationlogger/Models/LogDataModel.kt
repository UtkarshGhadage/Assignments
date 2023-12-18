package com.example.notificationlogger.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "LogTable", indices = [Index(value = ["id"], unique = true)])
data class LogDataModel(

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo("title")
    var title: String? ,
    @ColumnInfo("message")
    var message: String? ,
    @ColumnInfo("packageName")
    var packageName: String? ,
    @ColumnInfo("timestamp")
    var timestamp: String?
)