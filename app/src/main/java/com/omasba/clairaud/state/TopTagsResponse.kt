package com.omasba.clairaud.state

import kotlinx.serialization.Serializable

@Serializable
data class TopTagsResponse(
    val toptags: TopTags = TopTags(setOf())
)
