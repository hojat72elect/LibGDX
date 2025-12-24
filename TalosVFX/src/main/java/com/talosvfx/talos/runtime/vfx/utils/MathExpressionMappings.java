
package com.talosvfx.talos.runtime.vfx.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.talosvfx.talos.runtime.vfx.Expression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MathExpressionMappings {

    private static final Logger logger = LoggerFactory.getLogger(MathExpressionMappings.class);

    private static final ObjectMap<String, Expression> names = new ObjectMap<>();

    static {
        Array<String> namesArr = new Array<>();
        // get list of possible interpolations

        Field[] fields = ClassReflection.getFields(Expression.class);
        for (int i = 0; i < fields.length; i++) {
            try {
                Expression interp = (Expression) fields[i].get(null);
                names.put(fields[i].getName(), interp);
                namesArr.add(fields[i].getName());
            } catch (ReflectionException e) {
                logger.error("Problem getting fields for Math expressions", e);
                e.printStackTrace();
            }
        }
    }

    public static Expression getMathExpressionForName(String name) {
        return names.get(name);
    }

    public static String getNameForMathExpression(Expression expression) {
        for (ObjectMap.Entry<String, Expression> name : names) {
            if (name.value == expression) {
                return name.key;
            }
        }
        return "fade";
    }

    public static void getAvailableMathExpressions(Array<String> interps) {
        for (String key : names.keys()) {
            interps.add(key);
        }
    }
}
