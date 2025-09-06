package com.xyz.tkm.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val Id: Int = 0,
    var firebaseId: String = "",
    var title: String = "",
    var description: String = "",
    var status: String = "Pending",
    var synced: Boolean = false
)
