package com.omasba.clairaud.state

import kotlinx.serialization.Serializable

@Serializable
data class TopTags(
    val tag: Set<Tag>
)
