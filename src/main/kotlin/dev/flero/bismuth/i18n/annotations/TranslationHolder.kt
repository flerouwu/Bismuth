package dev.flero.bismuth.i18n.annotations

/**
 * @param file the file name that contains translations for this class
 */
annotation class TranslationHolder(val file: String, val table: String = "")
