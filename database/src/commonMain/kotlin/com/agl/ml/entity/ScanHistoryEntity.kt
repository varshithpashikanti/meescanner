package com.appgolive.meescanner.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_history")
data class ScanHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val scanType: String,

    val title: String,

    val data: String,

    val createdAt: Long
)
