package com.ghostbuilder.domain.model

import java.time.Instant

data class TimelineEvent(
    val timestamp: Instant,
    val event_description: String
)
