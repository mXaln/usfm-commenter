package org.mxaln.compose.usfm

import kotlin.reflect.KClass

open class KtMarker(val wrapper: JsMarker) {
    val contents: List<KtMarker> = wrapper.contents.toList().map { it.toKt() }
    fun getIdentifier(): String = wrapper.getIdentifier()
    fun getPosition(): Int = wrapper.getPosition()
    fun setPosition(value: Int) { wrapper.setPosition(value) }
    fun getAllowedContents(): List<Any> = wrapper.getAllowedContents().toList()
    fun preProcess(input: String): String = wrapper.preProcess(input)
    fun tryInsert(input: KtMarker): Boolean = wrapper.tryInsert(input.wrapper)
    fun getTypesPathToLastMarker(): List<Any> = wrapper.getTypesPathToLastMarker().toList()
    fun getHierarchyToMarker(target: KtMarker): List<KtMarker> =
        wrapper.getHierarchyToMarker(target.toPlatform()).toList().map { it.toKt() }
    fun getHierarchyToMultipleMarkers(targets: List<KtMarker>): Map<KtMarker, List<KtMarker>> =
        TODO("Not yet implemented")
    fun getChildMarkers(clazz: KClass<out KtMarker>): List<KtMarker> =
        wrapper.getChildMarkers(clazz.toClassName()).toList().map { it.toKt() }
    fun getChildMarkers(clazz: KClass<out KtMarker>, ignoredParents: List<KClass<out KtMarker>>): List<KtMarker> {
        val ignored = ignoredParents.map { it.toClassName().toJsString() }.toJsArray()
        return wrapper.getChildMarkers(clazz.toClassName(), ignored).toList().map { it.toKt() }
    }
    fun getLastDescendant(): KtMarker = wrapper.getLastDescendant().toKt()
}

fun JsMarker.toKt(): KtMarker {
    return when (this::class.simpleName) {
        "JsUsfmDocument" -> KtDocument(this)
        "JsTOC3Marker" -> KtTOC3Marker(this)
        "JsHMarker" -> KtHMarker(this)
        "JsCMarker" -> KtCMarker(this)
        "JsVMarker" -> KtVMarker(this)
        "JsTextBlock" -> KtTextBlock(this)
        "JsFMarker" -> KtFMarker(this)
        "JsXMarker" -> KtXMarker(this)
        else -> KtMarker(this)
    }
}
fun JsUsfmDocument.toKt(): KtDocument {
    return KtDocument(this)
}
fun JsTOC3Marker.toKt(): KtTOC3Marker {
    return KtTOC3Marker(this)
}
fun JsHMarker.toKt(): KtHMarker {
    return KtHMarker(this)
}
fun JsCMarker.toKt(): KtCMarker {
    return KtCMarker(this)
}
fun JsVMarker.toKt(): KtVMarker {
    return KtVMarker(this)
}
fun JsTextBlock.toKt(): KtTextBlock {
    return KtTextBlock(this)
}
fun JsFMarker.toKt(): KtFMarker {
    return KtFMarker(this)
}
fun JsXMarker.toKt(): KtXMarker {
    return KtXMarker(this)
}

fun KtMarker.toPlatform(): JsMarker {
    return wrapper
}

fun KClass<out KtMarker>.toClassName(): String {
    return when (this) {
        KtDocument::class -> "JsUsfmDocument"
        KtTOC3Marker::class -> "JsTOC3Marker"
        KtHMarker::class -> "JsHMarker"
        KtCMarker::class -> "JsCMarker"
        KtVMarker::class -> "JsVMarker"
        KtTextBlock::class -> "JsTextBlock"
        KtFMarker::class -> "JsFMarker"
        KtXMarker::class -> "JsXMarker"
        else -> throw IllegalArgumentException("Unknown marker type $this")
    }
}

data class KtDocument(private val marker: JsMarker) : KtMarker(marker) {
    fun insert(marker: KtMarker) {
        wrapper.unsafeCast<JsUsfmDocument>().insert(marker.toPlatform())
    }
}
data class KtTOC3Marker(private val marker: JsMarker) : KtMarker(marker) {
    val bookAbbreviation: String = wrapper.unsafeCast<JsTOC3Marker>().bookAbbreviation
}
data class KtHMarker(private val marker: JsMarker) : KtMarker(marker) {
    val headerText: String = wrapper.unsafeCast<JsHMarker>().headerText
}
data class KtCMarker(private val marker: JsMarker) : KtMarker(marker) {
    val number: String = wrapper.unsafeCast<JsCMarker>().number
}
data class KtVMarker(private val marker: JsMarker) : KtMarker(marker) {
    val number: String = wrapper.unsafeCast<JsVMarker>().number
    val startingVerse: Int = wrapper.unsafeCast<JsVMarker>().number.toIntOrNull() ?: -1
    val endingVerse: Int = wrapper.unsafeCast<JsVMarker>().number.toIntOrNull() ?: -1
}
data class KtTextBlock(private val marker: JsMarker) : KtMarker(marker) {
    val text: String = wrapper.unsafeCast<JsTextBlock>().text
}
data class KtFMarker(private val marker: JsMarker) : KtMarker(marker) {
    val footNoteCaller: String = wrapper.unsafeCast<JsFMarker>().footNoteCaller
}
data class KtXMarker(private val marker: JsMarker) : KtMarker(marker) {
    val crossRefCaller: String = wrapper.unsafeCast<JsXMarker>().crossRefCaller
}