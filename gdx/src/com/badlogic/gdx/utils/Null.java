package com.badlogic.gdx.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates the element may have a {@code null} value. This removes the need for "Can be null" javadoc and can be used by
 * static analysis tools to warn about probable runtime errors or contract violations.
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface Null {
}
