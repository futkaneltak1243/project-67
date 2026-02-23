package com.ghostbuilder.data.local.converter

import androidx.room.TypeConverter
import com.ghostbuilder.domain.model.ProjectStage

class ProjectStageConverter {

    @TypeConverter
    fun fromProjectStage(value: ProjectStage): String {
        return value.name
    }

    @TypeConverter
    fun toProjectStage(value: String): ProjectStage {
        return ProjectStage.valueOf(value)
    }
}
