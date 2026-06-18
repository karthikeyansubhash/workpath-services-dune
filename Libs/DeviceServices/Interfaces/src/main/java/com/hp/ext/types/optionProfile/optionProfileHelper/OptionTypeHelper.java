package com.hp.ext.types.optionProfile.optionProfileHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OptionTypeHelper {
    static Boolean isE2EnumerationType(Type type) throws ClassNotFoundException {
        Class<?> typeClass = Class.forName(type.getTypeName());
        if (typeClass.getSuperclass().getName().equalsIgnoreCase("com.hp.ext.types.common.E2Enumeration")) {
            return true;
        } else {
            return false;
        }
    }

    static List<String> getE2Enumerations(Type e2EnumerationType) throws ClassNotFoundException {
        List<String> e2Enumerations = new ArrayList<String>();
        Class<?> typeClass = Class.forName(e2EnumerationType.getTypeName());
        List<Field> fields = Arrays.stream(typeClass.getDeclaredFields())
                .filter(f -> Modifier.isStatic(f.getModifiers()) && Modifier.isPublic(f.getModifiers())
                        && f.getType().getName().equalsIgnoreCase(e2EnumerationType.getTypeName()))
                .collect(Collectors.toList());
        if (null != fields) {
            for (var f : fields) {
                e2Enumerations.add(f.getName().toString());
            }
        }

        return e2Enumerations;
    }

    static <T> T parseE2Enumeration(String name, Class<T> classT) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
        T value = null;
        if (isE2EnumerationType(classT)) {
            List<Method> methods = Arrays.stream(classT.getDeclaredMethods())
                    .filter(f -> Modifier.isPublic(f.getModifiers())).collect(Collectors.toList());
            Method info = null;
            for (Method m : methods) {
                // Looking for either getter or bool
                if (m.getName().equalsIgnoreCase("Parse")) {
                    info = m;
                    break;
                }
            }
            // All E2Enumerations actually support a Parse method (although it's not part of
            // the IE2Enumeration interface)
            if ((null != info) && Modifier.isStatic(info.getModifiers()) && Modifier.isPublic(info.getModifiers())) {
                value = ((T) (info.invoke(null, new Object[] { name })));
            }

        }

        return value;
    }

    static Boolean isE2BindablePolymorph(Type type)
            throws NoSuchFieldException, SecurityException, ClassNotFoundException, NoSuchMethodException {
        Boolean result = false;
        Class<?> typeClass = Class.forName(type.getTypeName());
        Class<?> polyClass = Class.forName("com.hp.ext.types.common.IE2Polymorph");
        if (polyClass.isAssignableFrom(typeClass)) {
            Class<?>[] parameterType = null;
            if (null != typeClass.getMethod("isExpression", parameterType)
                    && null != typeClass.getMethod("isExplicit", parameterType)) {
                result = true;
            }
        }

        return result;
    }

    static <T> List<T> castValues(List<String> optionValues, Class<T> classT) {
        List<T> result = new ArrayList<T>();
        if (null != optionValues) {
            for (String optionValue : optionValues) {
                try {
                    if (true == classT.isEnum()) {
                        for (T candidate : classT.getEnumConstants()) {
                            if (candidate.toString().equals(optionValue)) {
                                result.add(candidate);
                            }
                        }
                    }
                    if (true == isE2EnumerationType(classT)) {
                        T value1 = parseE2Enumeration(optionValue, classT);
                        if (null != value1) {
                            result.add(value1);
                        }
                    } else {
                        var o = optionValue;

                        result.add((T) o);
                    }
                } catch (Exception e) {
                    // eat
                }
            }
        }

        return result;
    }
}
