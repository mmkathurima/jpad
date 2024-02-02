package io.jpad.model;


import java.util.HashMap;
import java.util.Map;


public class Mem {
    private static final Map<String, Object> m = new HashMap<>();


    public static Object put(String key, Object value) {

        return m.put(key, value);

    }


    public static Object get(String key) {

        return m.get(key);

    }

}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\Mem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */