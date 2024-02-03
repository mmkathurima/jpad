package io.jpad.resultset;

class CollectionUtils {

    static Class<?> getType(Iterable c) {

        Class<?> t = null;

        for (Object o : c) {

            if (o != null) {

                if (t == null) {

                    t = o.getClass();
                    continue;
                }

                if (!t.equals(o.getClass())) {

                    return Object.class;
                }
            }
        }

        return (t != null) ? t : Object.class;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\resultset\CollectionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */