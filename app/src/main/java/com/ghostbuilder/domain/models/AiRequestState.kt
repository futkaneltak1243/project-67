package com.ghostbuilder.domain.models

import com.ghostbuilder.domain.models.AiResponse

sealed class AiRequestState {
    object Idle : AiRequestState()
    object Requesting : AiRequestState()
    data class Success(val response: AiResponse) : AiRequestState()
    data class Error(val throwable: Throwable) : AiRequestState()
}
