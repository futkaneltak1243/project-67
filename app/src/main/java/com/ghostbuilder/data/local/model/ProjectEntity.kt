package com.ghostbuilder.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ghostbuilder.domain.model.ProjectStage

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val stage: ProjectStage,
    val createdAt: Long,
    val updatedAt: Long
)
