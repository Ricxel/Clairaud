package com.omasba.clairaud.core.network

import com.omasba.clairaud.presentation.store.state.TopTags
import kotlinx.serialization.Serializable

@Serializable
data class TopTagsResponse(
    val toptags: TopTags = TopTags(setOf())
)
