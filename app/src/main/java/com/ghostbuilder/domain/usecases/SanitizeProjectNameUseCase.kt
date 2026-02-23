package com.ghostbuilder.domain.usecases

import javax.inject.Inject

class SanitizeProjectNameUseCase @Inject constructor() {

    operator fun invoke(projectName: String): String {
        return projectName
            .lowercase()
            .replace(" ", "-")
            .replace(Regex("[^a-z0-9-]"), "")
            .replace(Regex("-+"), "-")
            .trim('-')
    }
}
