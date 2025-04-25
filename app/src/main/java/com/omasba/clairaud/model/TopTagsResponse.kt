package com.omasba.clairaud.model

import kotlinx.serialization.Serializable

@Serializable
data class TopTagsResponse(
    val toptags: TopTags = TopTags(setOf())
)
