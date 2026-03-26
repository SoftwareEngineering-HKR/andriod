package se.hkr.andriod.data.language

import se.hkr.andriod.R


enum class AppLanguage(
    val languageTag: String,
    val nativeNameRes: Int,
    val displayNameRes: Int
) {
    SWEDISH(
        languageTag = "sv",
        nativeNameRes = R.string.language_swedish_native,
        displayNameRes = R.string.language_swedish_display
    ),
    ENGLISH(
        languageTag = "en",
        nativeNameRes = R.string.language_english_native,
        displayNameRes = R.string.language_english_display
    ),
    HUNGARIAN(
        languageTag = "hu",
        nativeNameRes = R.string.language_hungarian_native,
        displayNameRes = R.string.language_hungarian_display
    )
}
