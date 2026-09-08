package com.example.newsly.data.model

import com.google.gson.annotations.SerializedName

data class MyMemoryTranslationResponse(
    @SerializedName("responseData") val responseData: TranslationData?,
    @SerializedName("responseStatus") val responseStatus: Any?,
    @SerializedName("responseDetails") val responseDetails: String?,
    @SerializedName("matches") val matches: List<TranslationMatch>?
)

data class TranslationData(
    @SerializedName("translatedText") val translatedText: String?,
    @SerializedName("match") val match: Double?
)

data class TranslationMatch(
    @SerializedName("id") val id: Any?,
    @SerializedName("translation") val translation: String?,
    @SerializedName("quality") val quality: Any?,
    @SerializedName("reference") val reference: String?
)

data class TranslationResult(
    val originalText: String,
    val translatedText: String,
    val sourceLang: String,
    val targetLang: String,
    val isUrdu: Boolean
)
