package com.omasba.clairaud.model

import kotlinx.serialization.Serializable

@Serializable
data class TopTags(
    val tag: Set<Tag>
)
