// data/local/entities/RepositoryEntity.kt
package com.selflearning.composefirstapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
data class RepositoryEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val fullName: String,
    val htmlUrl: String,
    val description: String?,
    val stargazersCount: Int,
    val forksCount: Int,
    val language: String?,
    val ownerName: String
)
