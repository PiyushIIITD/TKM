package com.xyz.tkm.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    var status: String = "Pending",  // Pending, In Progress, Completed
    val imageRes: Int? = null       // Optional icon for task card
)
