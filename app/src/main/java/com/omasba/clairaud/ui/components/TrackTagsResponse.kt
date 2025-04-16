package com.omasba.clairaud.ui.components
import com.google.gson.*
import java.lang.reflect.Type

data class TrackTagsResponse (
    val tags: Set<String>
)
class TrackTagsResponseDeserializer : JsonDeserializer<TrackTagsResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): TrackTagsResponse {
        val tags = mutableSetOf<String>()

        try {
            val tagArray = json?.asJsonObject
                ?.getAsJsonObject("toptags")
                ?.getAsJsonArray("tag")

            tagArray?.forEach { tagElement ->
                val name = tagElement.asJsonObject["name"]?.asString
                name?.let { tags.add(it.lowercase()) }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return TrackTagsResponse(tags)
    }
}
