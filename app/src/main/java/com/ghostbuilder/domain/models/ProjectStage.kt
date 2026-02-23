package com.ghostbuilder.domain.models

sealed class ProjectStage {
    object SeedsUnverified : ProjectStage()
    object SeedsVerified : ProjectStage()
    object WalkthroughInProgress : ProjectStage()
    object WalkthroughComplete : ProjectStage()
    object BrainInProgress : ProjectStage()
    object BrainComplete : ProjectStage()
    object CodeInProgress : ProjectStage()
    object CodeComplete : ProjectStage()
}
