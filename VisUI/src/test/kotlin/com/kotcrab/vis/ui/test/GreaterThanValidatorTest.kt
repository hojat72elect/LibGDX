package com.kotcrab.vis.ui.test

import com.kotcrab.vis.ui.util.Validators.GreaterThanValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GreaterThanValidatorTest {

    @Test
    fun testValidateInput() {
        val validator = GreaterThanValidator(5F)
        val validatorEquals = GreaterThanValidator(5F, true)

        assertFalse(validator.validateInput("A"))
        assertFalse(validator.validateInput(""))
        assertFalse(validator.validateInput("5"))
        assertTrue(validator.validateInput("6"))
        assertTrue(validator.validateInput(Float.MAX_VALUE.toString()))
        assertFalse(validator.validateInput(Float.MIN_VALUE.toString()))

        assertFalse(validatorEquals.validateInput("A"))
        assertFalse(validatorEquals.validateInput(""))
        assertTrue(validatorEquals.validateInput("5"))
        assertTrue(validatorEquals.validateInput("6"))
        assertTrue(validatorEquals.validateInput(Float.MAX_VALUE.toString()))
        assertFalse(validatorEquals.validateInput(Float.MIN_VALUE.toString()))
    }

    @Test
    fun testSetUseEquals() {
        val validator = GreaterThanValidator(5F)
        assertFalse(validator.validateInput("5"))
        validator.setUseEquals(true)
        assertTrue(validator.validateInput("5"))
    }

    @Test
    fun testSetGreaterThan() {
        val validator = GreaterThanValidator(5F)
        assertTrue(validator.validateInput("6"))
        assertFalse(validator.validateInput("4"))

        validator.setGreaterThan(10F)

        assertFalse(validator.validateInput("6"))
        assertTrue(validator.validateInput("11"))
    }
}