package org.mxaln.compose.usfm

import kotlin.reflect.KClass

internal class JsonParser {
    fun parse(input: String): KtDocument {
        val json = toJSON(input)
        return convertBook(json).toKt()
    }
}

actual class AppUsfmParser : IUSFMParser {
    actual val wrapper: Any

    actual constructor() {
        wrapper = JsonParser()
    }

    actual constructor(tagsToIgnore: List<String>) {
        wrapper = JsonParser()
    }

    actual constructor(
        tagsToIgnore: List<String>,
        ignoreUnknownMarkers: Boolean
    ) {
        wrapper = JsonParser()
    }

    actual override fun parseFromString(input: String): UsfmDocument {
        val document = (wrapper as JsonParser).parse(input)
        return UsfmDocument(document)
    }
}

actual class UsfmDocument(
    override val wrapper: Any
) : MarkerWrapper<UsfmDocument>(wrapper) {
    actual override fun getIdentifier(): String {
        return (wrapper as KtDocument).getIdentifier()
    }

    actual override fun getAllowedContents(): List<Any> {
        return (wrapper as KtDocument).getAllowedContents().toList()
    }

    actual fun insertMarker(marker: IMarker) {
        (wrapper as KtDocument).insert(marker.toPlatform())
    }

    actual fun insertDocument(document: UsfmDocument) {
        TODO("Not yet implemented")
    }

    actual fun insertMultiple(iterable: Iterable<IMarker>) {
        TODO("Not yet implemented")
    }
}

actual open class MarkerWrapper<T>(
    actual open val wrapper: Any
) : IMarker {
    actual override val contents: List<IMarker>
        get() = (wrapper as KtMarker).contents.toList().map { MarkerFactory.create(it) }

    actual override fun getIdentifier(): String {
        return (wrapper as KtMarker).getIdentifier()
    }

    actual override fun getPosition(): Int {
        return (wrapper as KtMarker).getPosition()
    }

    actual override fun setPosition(value: Int) {
        return (wrapper as KtMarker).setPosition(value)
    }

    actual override fun getAllowedContents(): List<Any> {
        return (wrapper as KtMarker).getAllowedContents().toList()
    }

    actual override fun preProcess(input: String): String {
        return (wrapper as KtMarker).preProcess(input)
    }

    actual override fun tryInsert(input: IMarker): Boolean {
        return (wrapper as KtMarker).tryInsert(input.toPlatform())
    }

    actual override fun getTypesPathToLastMarker(): List<Any> {
        return (wrapper as KtMarker).getTypesPathToLastMarker().toList()
    }

    actual override fun getHierarchyToMarker(target: IMarker): List<IMarker> {
        return (wrapper as KtMarker).getHierarchyToMarker(target.toPlatform())
            .toList()
            .map { MarkerFactory.create(it) }
    }

    actual override fun getHierarchyToMultipleMarkers(targets: List<IMarker>): Map<IMarker, List<IMarker>> {
        TODO("Not yet implemented")
    }

    actual override fun <U : IMarker> getChildMarkers(clazz: KClass<U>): List<U> {
        return getPlatformMarkerClass(clazz)?.let { outClass ->
            val platformMarkers = (wrapper as KtMarker).getChildMarkers(outClass)
            platformMarkers.toList().map { MarkerFactory.create(it) as U }
        } ?: emptyList()
    }

    actual override fun <U : IMarker, P : IMarker> getChildMarkers(
        clazz: KClass<U>,
        ignoredParents: List<KClass<out P>>
    ): List<U> {
        return getPlatformMarkerClass(clazz)?.let { outClass ->
            val ignored = ignoredParents.mapNotNull {
                getPlatformMarkerClass(it)
            }
            val platformMarkers = (wrapper as KtMarker).getChildMarkers(outClass, ignored)
            platformMarkers.toList().mapNotNull { MarkerFactory.create(it) as? U }
        } ?: emptyList()
    }

    actual override fun getLastDescendant(): IMarker {
        return MarkerFactory.create((wrapper as KtMarker).getLastDescendant())
    }

    private fun <T: IMarker> getPlatformMarkerClass(clazz: KClass<T>): KClass<out KtMarker>? {
        return try {
            when (clazz) {
                HMarker::class -> KtHMarker::class
                TOC3Marker::class -> KtTOC3Marker::class
                CMarker::class -> KtCMarker::class
                VMarker::class -> KtVMarker::class
                TextBlock::class -> KtTextBlock::class
                FMarker::class -> KtFMarker::class
                XMarker::class -> KtXMarker::class
                else -> null
            }
        } catch (e: ClassCastException) {
            null
        }
    }
}

actual object MarkerFactory {
    actual fun create(marker: Any): IMarker {
        return when (marker) {
            is KtDocument -> UsfmDocument(marker)
            is KtTOC3Marker -> TOC3Marker(marker)
            is KtHMarker -> HMarker(marker)
            is KtCMarker -> CMarker(marker)
            is KtVMarker -> VMarker(marker)
            is KtTextBlock -> TextBlock(marker)
            is KtFMarker -> FMarker(marker)
            is KtXMarker -> XMarker(marker)
            else -> throw IllegalArgumentException("Unknown marker type ${(marker as KtMarker).getIdentifier()}")
        }
    }
}

actual class TOC3Marker(
    override val wrapper: Any
) : MarkerWrapper<TOC3Marker>(wrapper) {
    actual val bookAbbreviation
        get() = (wrapper as KtTOC3Marker).bookAbbreviation
}

actual class HMarker(
    override val wrapper: Any
) : MarkerWrapper<HMarker>(wrapper) {
    actual val headerText
        get() = (wrapper as KtHMarker).headerText
}

actual class CMarker(
    override val wrapper: Any
) : MarkerWrapper<CMarker>(wrapper) {
    actual val number
        get() = (wrapper as KtCMarker).number.toIntOrNull() ?: -1
}

actual class VMarker(
    override val wrapper: Any
) : MarkerWrapper<VMarker>(wrapper) {
    actual val verseNumber
        get() = (wrapper as KtVMarker).number
    actual val startingVerse
        get() = (wrapper as KtVMarker).startingVerse
    actual val endingVerse
        get() = (wrapper as KtVMarker).endingVerse
}

actual class FMarker(
    override val wrapper: Any
) : MarkerWrapper<FMarker>(wrapper) {
    actual val footNoteCaller
        get() = (wrapper as KtFMarker).footNoteCaller
}

actual class XMarker(
    override val wrapper: Any
) : MarkerWrapper<XMarker>(wrapper) {
    actual val crossRefCaller
        get() = (wrapper as KtXMarker).crossRefCaller
}

actual class TextBlock(
    override val wrapper: Any
) : MarkerWrapper<TextBlock>(wrapper) {
    actual val text
        get() = (wrapper as KtTextBlock).text
}

private fun IMarker.toPlatform(): KtMarker {
    return (this as MarkerWrapper<*>).wrapper as KtMarker
}
