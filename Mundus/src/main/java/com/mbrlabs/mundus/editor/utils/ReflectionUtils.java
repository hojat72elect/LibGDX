package com.mbrlabs.mundus.editor.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ReflectionUtils {
    public static boolean hasMethodAnnotation(Method method, Class annotationClass) {
        final Annotation[] annotations = method.getAnnotations();
        for (Annotation a : annotations) {
            if (a.annotationType().equals(annotationClass)) {
                return true;
            }
        }

        return false;
    }
}
