package com.ghostbuilder.data.local.model

import androidx.room.TypeConverter
import com.ghostbuilder.domain.model.ProjectStage

class ProjectStageConverter {

    @TypeConverter
    fun fromProjectStage(stage: ProjectStage): String {
        return stage.name
    }

    @TypeConverter
    fun toProjectStage(value: String): ProjectStage {
        return ProjectStage.valueOf(value)
    }
}
