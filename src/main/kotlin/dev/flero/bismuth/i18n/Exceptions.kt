package dev.flero.bismuth.i18n

import kotlin.reflect.KClass

class CreateLanguageDirectory(tag: I18nTag) : RuntimeException("Unable to create language directory for $tag")
class NoInfoFile(tag: I18nTag) : RuntimeException("No info file found for language $tag.")
class InvalidInfoFile(reason: String) : RuntimeException("Invalid info file ($reason).")
class ClassMustBeObject(clazz: KClass<*>) : RuntimeException("$clazz must be have an object instance.")
class NoTranslationHolder(clazz: KClass<*>) : RuntimeException("$clazz must be annotated with @TranslationHolder.")
class NoSubTableFound(clazz: KClass<*>, table: String) :
    RuntimeException("$clazz requested sub-table $table, but it wasn't found.")
