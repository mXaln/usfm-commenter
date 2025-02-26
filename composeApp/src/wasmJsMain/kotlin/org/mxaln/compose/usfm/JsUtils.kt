@file:JsModule("./usfm.mjs")

package org.mxaln.compose.usfm

external fun convertBook(obj: JsAny): JsUsfmDocument

@JsFun("(output) => console.log(output)")
external fun consoleLog(vararg output: JsAny?)

@JsFun("(output) => console.log(output)")
external fun consoleLog2(vararg output: String?)

open external class JsMarker : JsAny {
    val contents: JsArray<JsMarker>
    open fun getIdentifier(): String
    fun getPosition(): Int
    fun setPosition(value: Int)
    open fun getAllowedContents(): JsArray<JsString>
    open fun preProcess(input: String): String
    open fun tryInsert(input: JsMarker): Boolean
    fun getTypesPathToLastMarker(): JsArray<JsString>
    fun getHierarchyToMarker(target: JsMarker): JsArray<JsMarker>
    fun getHierarchyToMultipleMarkers(targets: JsArray<JsMarker>): JsArray<JsMarker>
    fun getChildMarkers(clazz: String): JsArray<JsMarker>
    @JsName("getChildMarkersWithoutIgnored")
    fun getChildMarkers(clazz: String, ignoredParents: JsArray<JsString>): JsArray<JsMarker>
    fun getLastDescendant(): JsMarker
}

external class JsUsfmDocument: JsMarker {
    override fun getIdentifier(): String
    override fun getAllowedContents(): JsArray<JsString>
    fun insert(marker: JsMarker)
}

external class JsTOC3Marker(bookAbbreviation: String): JsMarker {
    val bookAbbreviation: String
    override fun getIdentifier(): String
    override fun preProcess(input: String): String
}

external class JsHMarker(headerText: String): JsMarker {
    val headerText: String
    override fun getIdentifier(): String
    override fun preProcess(input: String): String
}

external class JsCMarker(number: String): JsMarker {
    val number: String
    override fun getIdentifier(): String
    override fun preProcess(input: String): String
    override fun getAllowedContents(): JsArray<JsString>
}

external class JsVMarker(number: String): JsMarker {
    val number: String
    override fun getIdentifier(): String
    override fun preProcess(input: String): String
    override fun tryInsert(input: JsMarker): Boolean
    override fun getAllowedContents(): JsArray<JsString>
}

external class JsFMarker(footNoteCaller: String): JsMarker {
    val footNoteCaller: String
    override fun getIdentifier(): String
    override fun preProcess(input: String): String
}

external class JsXMarker(crossRefCaller: String): JsMarker {
    val crossRefCaller: String
    override fun getIdentifier(): String
    override fun preProcess(input: String): String
}

external class JsTextBlock(text: String): JsMarker {
    val text: String
    override fun getIdentifier(): String
}
