package com.omasba.clairaud.presentation.store.state

import kotlinx.serialization.Serializable

@Serializable
data class TopTags(
    val tag: Set<Tag>
)
