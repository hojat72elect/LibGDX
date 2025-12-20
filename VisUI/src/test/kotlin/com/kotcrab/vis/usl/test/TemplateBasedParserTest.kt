package com.kotcrab.vis.usl.test

import com.kotcrab.vis.usl.USL
import org.junit.Assert
import org.junit.Test
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.Scanner

class TemplateBasedParserTest {

    @Test
    @Throws(Exception::class)
    fun testParserUsingVisUITemplate() {
        testUsingTemplate("/test-visui.usl", "/test-visui-expected.json")
    }

    @Test
    @Throws(Exception::class)
    fun testParserUsingGdxTemplate() {
        testUsingTemplate("/test-gdx.usl", "/test-gdx-expected.json")
    }

    @Test
    @Throws(Exception::class)
    fun testParserUsingTintedTemplate() {
        testUsingTemplate("/test-tinted.usl", "/test-tinted-expected.json")
    }

    @Test
    @Throws(Exception::class)
    fun testParserAlias() {
        testUsingTemplate("/test-alias.usl", "/test-alias-expected.json")
    }

    @Test
    @Throws(Exception::class)
    fun testParserComments() {
        testUsingTemplate("/test-comments.usl", "/test-comments-expected.json")
    }

    @Test
    @Throws(Exception::class)
    fun testMinus() {
        testUsingTemplate("/test-minus.usl", "/test-minus-expected.json")
    }

    @Throws(Exception::class)
    private fun testUsingTemplate(uslPath: String, jsonPath: String) {
        val expected = readFile(jsonPath)
        val result = USL.parse(null, readFile(uslPath))
        compareResult(expected, result)
    }

    private fun compareResult(expectedJson: String, resultJson: String) {
        val expected = expectedJson.replace("\r\n", "\n").split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val result = resultJson.replace("\r\n", "\n").split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        for (i in expected.indices) {
            check(i < result.size) { "Unexpected end of result string at line " + (i + 1) }
            Assert.assertEquals("Json mismatch at line " + (i + 1), expected[i], result[i])
        }
    }

    @Throws(IOException::class)
    private fun readFile(path: String): String {
        return Scanner(TemplateBasedParserTest::class.java.getResourceAsStream(path), StandardCharsets.UTF_8).useDelimiter("\\A").next()
    }
}
