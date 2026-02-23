package com.ghosts.of.history.domain.models

import com.ghosts.of.history.domain.models.DraftSection

data class DraftFile(
    val filePath: String,
    val sections: List<DraftSection>
)
