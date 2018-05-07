
package com.songdexv.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author songdexv
 *
 */
public class GenericTypeTest {
    public static void main(String[] args) {
        //此处实际为匿名内部类
        Map<String, Integer> map = new HashMap<String, Integer>() {
        };
        Type type = map.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
        for (Type typeArgument : parameterizedType.getActualTypeArguments()) {
            System.out.println(typeArgument.getTypeName());
        }
    }
}
