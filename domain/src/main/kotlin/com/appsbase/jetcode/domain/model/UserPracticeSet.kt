package com.appsbase.jetcode.domain.model

data class UserPracticeSet(
    val practiceSet: PracticeSet,
    val practiceSetResult: PracticeSetResult?,
) {
    val isCompleted: Boolean get() = practiceSetResult != null
}
