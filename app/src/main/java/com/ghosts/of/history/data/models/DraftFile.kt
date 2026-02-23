package com.ghosts.of.history.data.models

import com.ghosts.of.history.data.models.DraftSection

data class DraftFile(
    val originalPath: String,
    val sections: List<DraftSection>
)
