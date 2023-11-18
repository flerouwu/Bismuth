package dev.flero.bismuth.i18n

enum class I18nTag(val language: String, val country: String?) {
    EnglishUnitedStates("en", "US");

    fun getTag(): String {
        return if (country != null) "${language}_$country"
        else language
    }

    companion object {
        fun byTag(tag: String): I18nTag? {
            return entries.find { it.getTag().equals(tag, true) }
        }
    }
}